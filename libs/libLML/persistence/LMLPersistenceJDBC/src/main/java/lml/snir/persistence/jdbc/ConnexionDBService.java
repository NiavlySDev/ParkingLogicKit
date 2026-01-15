package lml.snir.persistence.jdbc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.Enumeration;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.Unmarshaller;

//import org.jboss.vfs.*;
/**
 * Manage connection to db with JDB, use persistence.json file for the connection
 * information (db type, db name, user and password)
 *
 * @author (c) Stéphane ALONSO 2020
 */
class ConnexionDBService {

    private static PersistenceUnit persistence = null;
    private Statement statement;
    private Connection cnx;
    private boolean connected;
    private String location = "./persistence.json";

    static synchronized ConnexionDBService getInstance(boolean verbose) throws Exception {
        return new ConnexionDBService();
    }

    private ConnexionDBService() throws Exception {
        this.getPersistenceInfo();
    }

    private void verbose(String msg) {
        if (AbstractCrudServiceJDBC.verbose) {
            System.err.println(msg);
        }
    }

    private synchronized void getPersistenceInfo() throws Exception {
//        JAXBContext jc = JAXBContext.newInstance(PersistenceUnit.class);
//        Unmarshaller unmarshaller = jc.createUnmarshaller();
        //System.out.println("lml.snir.persistence.jdbc.ConnexionDBService.getPersistenceInfo()");
        if (ConnexionDBService.persistence == null) {
            this.verbose("lml.persistence.jdbc.ConnexionDBService.getPersistenceInfo()");
            this.location = "./persistence.json"; // default location directory where is the jar file
            try {
                InitialContext context = new InitialContext();
                Context xmlNode = (Context) context.lookup("java:comp/env");
                String dbhost = (String) xmlNode.lookup("dbhost");
                // get path for tomcat 7.x
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                URL url = classloader.getResource("../persistence.json");
                // if null test for wildfly
                if (url == null) {
                    Enumeration<URL> urls = this.getClass().getClassLoader().getResources("./"); //source("./persistence.json");
                    boolean find = false;
                    while (urls.hasMoreElements() && !find) {
                        URL u = urls.nextElement();
                        // vfs:/opt/wildfly-23.0.0.Final/standalone/deployments/ControleAccessServeurAvecClientWebPF-1.0.war/WEB-INF/classes/
                        //  vfs:/content/ControleAccessServeurAvecClientWebPF-1.0.war/WEB-INF/classes/
                        System.err.println("URL : " + u.toString());
                        if (u.toString().contains("WEB-INF")) {
                            String str = u.toString();
                            int index = str.indexOf("WEB-INF");
                            str = str.substring(0, index);
                            str += "WEB-INF/persistence.json";
                            url = new URL(str);
                            find = true;
                            //System.err.println(u);
                        }

                    }
                }
                this.location = url.getPath();
                System.out.println("FIND LOCATION IN : " + this.location + " try to read !");
                URLConnection conn = new URL("vfs:" + this.location).openConnection();
                System.out.println("URLConnection conn = new URL(\"vfs:\" + this.location).openConnection();");
//                FileInputStream vf = (FileInputStream) conn.getContent();
//                System.out.println("VirtualFile vf = (VirtualFile) conn.getContent();");
                //InputStream is = vf.openStream();
                //System.out.println("InputStream is = vf.openStream();");
//                ConnexionDBService.persistence = (PersistenceUnit) unmarshaller.unmarshal(vf);
                Gson gson = new Gson();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                ConnexionDBService.persistence =  gson.fromJson(reader, PersistenceUnit.class);
                System.out.println("ConnexionDBService.persistence = (PersistenceUnit) unmarshaller.unmarshal(is);");
            } catch (Exception ex) {
                // do nothing
                System.err.println(ex.getMessage());
            }
            System.err.println("ConnexionDBService => this.location : " + this.location);
            this.verbose("ConnexionDBService => this.location : " + this.location);

            if (ConnexionDBService.persistence == null) {
                File f = new File(this.location);
                Gson gson = new Gson();
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                ConnexionDBService.persistence =  gson.fromJson(reader, PersistenceUnit.class);
//                ConnexionDBService.persistence = (PersistenceUnit) unmarshaller.unmarshal(f);
            }
//            String drv = ConnexionDBService.persistence.getDriver();
//            drv = drv.substring(drv.lastIndexOf(".")+1);
//            drv = drv.toLowerCase();
//            ConnexionDBService.persistence.setDriver(drv);
            ConnexionDBService.persistence.update();
            System.out.println("ConnexionDBService.persistence = " + ConnexionDBService.persistence.toString());
        }
    }

    Statement connect() throws Exception {
        //System.out.println("lml.persistence.jdbc.ConnexionDBService.connect()");
        if (this.isConnected()) {
            System.err.println("lml.persistence.jdbc.ConnexionDBService.connect() must close previous connection ");
            this.close();
        }

        if (ConnexionDBService.persistence == null) {
            this.getPersistenceInfo();
        }

        Driver drv = null;
        try {
            drv = (Driver) Class.forName(this.persistence.getDriver()).newInstance();
        } catch (ClassNotFoundException ex) {
            String err = "Driver JDBC non trouvé.\nVeillez verifier la présence du jar " + this.persistence.getDriver() + " dans votre pom";
            this.verbose(err);
            throw new Exception(err);
        }

        Properties properties = new Properties();
        properties.setProperty("user", this.persistence.getUser());
        properties.setProperty("password", this.persistence.getPasssword());
        properties.setProperty("useSSL", "true");
        properties.setProperty("autoReconnect", "true");
        DriverManager.registerDriver(drv);
        String urlDb = this.persistence.toString();
        this.verbose(urlDb);
        this.cnx = DriverManager.getConnection(urlDb, properties);

        this.statement = this.cnx.createStatement();
        this.connected = true;

        return this.statement;
    }

    public Connection getSqlConnection() {
        return this.cnx;
    }

    boolean isConnected() {
        return connected;
    }

    void close() throws Exception {
        this.statement.close();
        this.connected = false;
    }

    PersistenceUnit getPU() {
        return this.persistence;
    }
}
