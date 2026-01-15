package lml.snir.persistence.jdbc;

//import javax.xml.bind.annotation.XmlRootElement;

/**
 * Contain all information for JDBC connection
 * @author (c) Stéphane ALONSO 2014
 */
//@XmlRootElement
class PersistenceUnit {
    private static final String MYSQL8 =  "com.mysql.cj.jdbc.Driver";
    private static final String MYSQL =  "org.gjt.mm.mysql.Driver";
    private static final String DERBY_CLIENT =  "org.apache.derby.jdbc.ClientDriver";
    private static final String DERBY_EMBEDED =  "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String POSTGRESQL =  "org.postgresql.Driver";
    private static final String HSQL =  "org.hsql.jdbcDriver";
    private static final String SQLITE = "org.sqlite.JDBC";
    private String dataBase;
    private String user;
    private String passsword;
    private String host;
    private String driver;
    private DBType dbType = DBType.UNKNOWN;

    /**
     * @return the dataBase
     */
    public String getDataBase() {
        return dataBase;
    }

    /**
     * @param dataBase the dataBase to set
     */
    public void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the passsword
     */
    public String getPasssword() {
        return passsword;
    }

    /**
     * @param passsword the passsword to set
     */
    public void setPasssword(String passsword) {
        this.passsword = passsword;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the driver
     */
    public String getDriver() {
        String st = "";
        
        if ("mysql".equals(this.driver)) {
            st = PersistenceUnit.MYSQL;
        }
        
        if ("mysql8".equals(this.driver)) {
            st = PersistenceUnit.MYSQL8;
        }
        
        if ("sqlite".equals(this.driver)) {
            st = PersistenceUnit.SQLITE; 
        }
        
        return st;
    }
    
    void update() {
        this.setDriver(this.driver);
    }

    /**
     * @param driver the driver to set
     */
    public void setDriver(String driver) {
        this.driver = driver;
        if ("mysql".equals(this.driver)) {
            this.dbType = DBType.MYSQL;
        }
        
        if ("mysql8".equals(this.driver)) {
            this.dbType = DBType.MYSQL;
        }
        
        if ("sqlite".equals(this.driver)) {
            this.dbType = DBType.SQLITE;
        }
    }
    
    @Override
    public String toString() {
        String st = "jdbc:";
        
        if ("mysql".equals(this.driver)) {
            st += "mysql://" + this.host + "/" + this.dataBase;
            this.dbType = DBType.MYSQL;
        }
        
        if ("mysql8".equals(this.driver)) {
            st += "mysql://" + this.host + "/" + this.dataBase;
            this.dbType = DBType.MYSQL;
        }
        
        if ("sqlite".equals(this.driver)) {
            st += "sqlite:" + this.dataBase + ".db";
            this.dbType = DBType.SQLITE;
        }
        
        return st;        
    }

    DBType getDBType() {
        return this.dbType;
    }
}
