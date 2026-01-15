package lml.snir.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private Properties mProperties;
    private static ConfigReader sInstance;

    private ConfigReader(String pFilePropertiesName) throws IOException {
        readConfig(pFilePropertiesName);
    }

    public static ConfigReader getInstance(String pFilePropertiesName) {
        //if (sInstance == null) {
            try {
                sInstance = new ConfigReader(pFilePropertiesName);
            } catch (IOException e) {
                throw new RuntimeException("Impossible de lire le fichier " + pFilePropertiesName);
            }
      //  }

        return sInstance;
    }

    private void readConfig(String pFilePropertiesName) throws IOException {
        mProperties = new Properties();
        try (FileInputStream vFileInputStream = new FileInputStream(pFilePropertiesName)) {
            mProperties.load(vFileInputStream);
        }
    }

    public String getProperty(String pKey) {
        return mProperties.getProperty(pKey);
    }

    public Properties getProperties() {
        return mProperties;
    }

    public static String getClasspathResource(String pResourcePath) {
        String vPath = pResourcePath;

        if (vPath.startsWith("/")) {
            vPath = vPath.substring(1);
        }

        try {
            return Thread.currentThread().getContextClassLoader().getResource(vPath).getPath().replaceAll("%20", " ");
        } catch (Exception pException) {
            throw new RuntimeException("La ressource " + pResourcePath + " n est pas dans le classpath !");
        }
    }
}
