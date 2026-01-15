package lml.snir.persistence.jdbc;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import lml.snir.persistence.CrudService;

/**
 *
 * @author (c) Stéphane ALONSO 2017
 * @param <T>
 */
public abstract class AbstractCrudServiceJDBC<T> implements CrudService<T> {

    public static boolean verbose = false;

    private Class<T> clazz;
    private DBType dbType;

    protected abstract T createEntity(Map map) throws Exception;

    private void verbose(String msg) {
        if (AbstractCrudServiceJDBC.verbose) {
            System.err.println(msg);
        }
    }

    public AbstractCrudServiceJDBC() throws Exception {
        verbose("lml.persistence.jdbc.AbstractCrudServiceJDBC.<init>() for ");

        try {
            ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
            String cl = pt.getActualTypeArguments()[0].toString().split("\\s")[1];

            Class c = Class.forName(cl);
            this.clazz = (Class<T>) c;    
//            System.out.println(this.clazz.getSimpleName());
            ConnexionDBService cnx = ConnexionDBService.getInstance(AbstractCrudServiceJDBC.verbose);
            this.dbType = cnx.getPU().getDBType();

        } catch (Throwable e) {
            verbose(e.toString());
            throw new Exception(e.toString());
        }
    }

    protected final String getEntityName() {
        return this.clazz.getSimpleName().toUpperCase();
    }

    protected final long executeAdd(String query, ByteArrayInputStream... bais) throws Exception {
        long id = -1;
        ConnexionDBService cnx = ConnexionDBService.getInstance(AbstractCrudServiceJDBC.verbose);
        Statement st = cnx.connect();

        try {
            PreparedStatement statement = cnx.getSqlConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < bais.length; i++) {
                ByteArrayInputStream stream = bais[i];
                int nbBytes = stream.available();
                statement.setBinaryStream(i + 1, stream, nbBytes);
            }
            statement.executeUpdate();

            // récupération de la clé primaire
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (SQLException sqle) {
            int errorCode = -1;
            while (sqle != null) {
                String message = sqle.getMessage();
                String sqlState = sqle.getSQLState();
                errorCode = sqle.getErrorCode();
                System.out.println("Message = " + message);
                System.out.println("SQLState = " + sqlState);
                System.out.println("ErrorCode = " + errorCode);
                if (sqle instanceof DataTruncation) {
                    //on fait ici la vérification sur la classe et non sur le SQLState 
                    //car la spécification n'est pas toujours suivie à la lettre 
                    DataTruncation dt = (DataTruncation) sqle;
                    System.out.println("Taille qui aurait dû être transférée (bytes) : " + dt.getDataSize());
                    System.out.println("Taille effectivement transférée (bytes) : " + dt.getTransferSize());
                    //etc. 
                }
                sqle = sqle.getNextException();
            }
            throw new Exception("Erreur SQL : " + errorCode);

        } finally {
            cnx.close();
        }

        return id;
    }

    protected final void executeQuery(String query, ByteArrayInputStream... bais) throws Exception {
        ConnexionDBService cnx = ConnexionDBService.getInstance(AbstractCrudServiceJDBC.verbose);
        Statement st = cnx.connect();
        try {
            PreparedStatement statement = cnx.getSqlConnection().prepareStatement(query);
            for (int i = 0; i < bais.length; i++) {
                ByteArrayInputStream stream = bais[i];
                int nbBytes = stream.available();
                statement.setBinaryStream(i + 1, stream, nbBytes);
            }
            statement.executeUpdate();
            //st.executeUpdate(query);
        } finally {
            cnx.close();
        }
    }

    protected final List<List<Object>> executeQueryWithResult(String query) throws Exception {
        List<List<Object>> data = new ArrayList<>();
        ConnexionDBService cnx = ConnexionDBService.getInstance(AbstractCrudServiceJDBC.verbose);
        Statement st = cnx.connect();
        try {
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            while (rs.next()) {
                List<Object> values = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    values.add(rs.getObject(i));
                }
                data.add(values);
            }
        } finally {
            cnx.close();
        }

        return data;
    }

    protected final T getSingleResult(String query) throws Exception {
        T t = null;
        List<Map> maps = this.getResultSet(query);
        for (Map map : maps) {
            t = (this.createEntity(map));
        }
        return t;
    }

    private List<Map> getResultSet(String query) throws Exception {
        ConnexionDBService cnx = ConnexionDBService.getInstance(AbstractCrudServiceJDBC.verbose);
        Statement st = cnx.connect();
        ResultSet rs = null;
        List<Map> maps = new ArrayList<>();

        try {
            rs = st.executeQuery(query);
            while (rs.next()) {
                ResultSetMetaData metadata = rs.getMetaData();
                int columnCount = metadata.getColumnCount();
                Map map = new HashMap();
                for (int i = 1; i <= columnCount; i++) {
                    map.put(metadata.getColumnName(i), rs.getObject(i));
                }
                maps.add(map);
            }
        } finally {
            cnx.close();
        }

        return maps;
    }

    protected final List<T> getResults(String query) throws Exception {
        List<T> t = new ArrayList<>();
        List<Map> maps = this.getResultSet(query);
        for (Map map : maps) {
            t.add(this.createEntity(map));
        }
        return t;
    }

    protected final long getCount(String query) throws Exception {
        long count = 0;
        List<Map> maps = this.getResultSet(query);
        Object o = maps.get(0).get("COUNT(*)");
        if (o instanceof Integer) {
            count = (int) o;
        } else {
            count = (long) o;
        }
        return count;
    }

    protected final ByteArrayInputStream getBlobByImage(Image image) {
        if (image != null) {
            BufferedImage buff = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
            buff.getGraphics().drawImage(image, 0, 0, null);

            byte[] imageBytes;
            BufferedImage bImage = buff;
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            try {
                ImageIO.write(bImage, "jpeg", baos);
            } catch (Exception e) {
                throw new IllegalStateException(e.toString());
            }
            imageBytes = baos.toByteArray();

            ByteArrayInputStream inStream = new ByteArrayInputStream(imageBytes);
            return inStream;
        } else {
            System.out.println("PCD error : the image is null");
            byte[] imageNullBytes = new byte[0];
            return new ByteArrayInputStream(imageNullBytes);
        }
    }
    
    @Override
    public void remove(T t) throws Exception {
        Field field = this.clazz.getField("id");
        Long id = field.getLong(t);
        this.remove(id);        
    }
    
    @Override
    public T getById(Long id) throws Exception {
        String query = "SELECT * FROM `" + this.getEntityName() + "` WHERE id = " + id;
        return this.getSingleResult(query);
    }

    @Override
    public final long getCount() throws Exception {
        String query = "SELECT COUNT(*) FROM `" + this.getEntityName() + "`";
        return this.getCount(query);
    }

    @Override
    public final List<T> getAll() throws Exception {
        String query = "SELECT * FROM `" + this.getEntityName() + "`";
        //System.out.println(query);
        return this.getResults(query);
    }

    @Override
    public final List<T> getAll(int begin, int count) throws Exception {
        String query = "SELECT * FROM `" + this.getEntityName() + "` LIMIT " + begin + ", " + count;
        return this.getResults(query);
    }

    public final void remove(long id) throws Exception {
        String query = "DELETE FROM `" + this.getEntityName() + "` WHERE id = " + id;
        this.executeQuery(query);
    }

    protected final DBType getDBType() {
        return this.dbType;
    }

    protected long getFieldAsLong(String key, Map map) {
        long value;
        Object o = map.get(key);
        if (o instanceof Long) {
            value = (long) o;
        } else {
            int v = (int)o;
            value = v;
        }
        
        return value;
    }
    
    protected float getFieldAsFloat(String key, Map map) {
        float value;
        Object o = map.get(key);
        if (o instanceof Float) {
            value = (float) o;
        } else {
            double v = (double)o;
            value = (float) v;
        }
        
        return value;
    }
    
    protected boolean getFieldAsBoolean(String key, Map map) {
        boolean value;
        Object o = map.get(key);
        if (o instanceof Boolean) {
            value = (boolean) o;
        } else {
            int v = (int)o;
            value = (v == 1);
        }
        
        return value;
    }
}
