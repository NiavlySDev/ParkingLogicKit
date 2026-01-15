package lml.snir.rest.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lml.snir.rest.json.DateAdapter;
import lml.snir.rest.json.GSonAdapter;

/**
 *
 * @author fanou
 * (c) LML 2025
 * @param <T>
 */
public class ClientRest<T> {

    protected String BASE_URI;
    private String url;
    protected Type listType;
    private Gson gson;
    private boolean xdebugEnabled;
    private Class<T> clazz; // NEW
    private List<Token> tokens = null;

    public ClientRest() {
        try {
            ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
            String cl = pt.getActualTypeArguments()[0].toString().split("\\s")[1];
            Class c = Class.forName(cl);
            this.clazz = (Class<T>) c;
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     *
     * @param service : containt sub url for this service class
     * @param config
     * @param tokens
     */
    protected final void init(String service, RestServerConfig config, Token... tokens) {
        if (tokens.length != 0) {
            this.tokens = new ArrayList<>();
            for (Token token : tokens) {
                this.tokens.add(token);
            }
        }
        GsonBuilder builder = new GsonBuilder();
        builder = builder.registerTypeAdapter(Date.class, new DateAdapter());
        this.gson = builder.registerTypeAdapter(this.clazz, new GSonAdapter()).create(); //
        this.BASE_URI = config + "/" + service;
    }

    /**
     * in case of polymorphic service
     *
     * @param classes contain all the child classes that concern the service
     */
    public final void polymorphicInit(List<Class> classes) {
        GsonBuilder builder = new GsonBuilder();
        builder = builder.registerTypeAdapter(this.clazz, new GSonAdapter());
        for (Class c : classes) {
            builder = builder.registerTypeAdapter(c, new GSonAdapter());
        }
        builder = builder.registerTypeAdapter(Date.class, new DateAdapter());
        //this.gson = builder.registerTypeAdapter(this.clazz, new GSonAdapter()).create();
        this.gson = builder.create();
    }

    /**
     * add sub url for the method mapping
     *
     * @param url
     */
    protected final void setPath(String url) {
        this.url = BASE_URI;
        if (this.url != null) {
            this.url += "/" + url;
        }

        if (this.xdebugEnabled) {
            this.url += "?XDEBUG_SESSION_START=netbeans-xdebug";
        }

        if (this.tokens != null) {
            this.url += "?";
            boolean first = true;
            for (Token token : this.tokens) {
                if (first) {
                    first = false;
                } else {
                    this.url += "&";
                }
                this.url += token.name + "=" + token.value;
            }
        }
    }

    protected final String getUrl() {
        return this.url;
    }

    public final void setXdebug(boolean xdebugEnabled) {
        this.xdebugEnabled = xdebugEnabled;
    }

    /**
     * Get one entity on the REST Server
     *
     * @return
     * @throws Exception
     */
    protected final T getEntity() throws Exception {
        String data = this.createRequest(null, "GET");
        return this.getAnEntity(data);
    }


    protected final List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    protected final T getAnEntity(String data) throws Exception {
        JsonObject json = (JsonObject) JsonParser.parseString(data);
        Class actualClass = this.getClassName(json.toString());
        Object entity = Class.forName(actualClass.getCanonicalName()).newInstance();
        List<Field> fields = new ArrayList<>();
        fields = this.getAllFields(fields, this.clazz); //this.clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            Class type = field.getType();

            Object o;
            try {
                JsonPrimitive fieldJson = json.getAsJsonPrimitive(name);
                o = gson.fromJson(fieldJson, type);
            } catch (java.lang.ClassCastException ex) {
                JsonObject fieldJson = json.getAsJsonObject(name);
                Class fieldClass = this.getClassName(fieldJson.toString());
                if (type.getCanonicalName().equals(fieldClass.getCanonicalName())) {
                    o = gson.fromJson(fieldJson, type);
                } else  {
                    o = gson.fromJson(fieldJson, fieldClass);
                }
            }

            field.set(entity, o);
        }

        return (T) entity;
    }

    /**
     * Get an entity List on the REST Server
     *
     * @return
     * @throws Exception
     */
    protected final List<T> getEntitys() throws Exception {
        List<T> result = null;
        String data = this.createRequest(null, "GET");
        result = this.gson.fromJson(data, new TypeToken<ArrayList<T>>() {
        }.getType());
        ParameterizedType superType = (ParameterizedType) getClass().getGenericSuperclass();
        Type innerT = superType.getActualTypeArguments()[0];
        for (int i = 0, size = result.size(); i < size; i++) {
            result.set(i, this.getAnEntity(this.gson.toJson(result.get(i))));
        }

        return result;
    }

    /**
     * Add an entity on server (persist)
     *
     * @param entity
     * @return
     * @throws Exception
     */
    protected final T addEntity(T entity) throws Exception {
        String data = this.createRequest(entity, "POST");
        return (T) this.gson.fromJson(data, this.getClassName(data));
    }

    /**
     * Update
     *
     * @param entity
     * @throws Exception
     */
    protected final void updateEntity(T entity) throws Exception {
        this.createRequest(entity, "PUT");
    }

    // doesn't work at this time
    protected final void removeEntity(T entity) throws Exception {
        this.createRequest(entity, "DELETE");
    }

    protected final void removeEntityById() throws Exception {
        this.createRequest(null, "DELETE");
    }

    /**
     * Get the number of entity
     *
     * @return
     * @throws Exception
     */
    protected final int getCountEntity() throws Exception {
        String data = this.createRequest(null, "GET"); //this.doGet();
        return Integer.parseInt(data);
    }

    private static TrustManager[] trustAllCerts = null;

    /**
     * This method permit bypass certificate valitation for using self signed
     * one
     */
    private static void disableSslVerification() {
        if (trustAllCerts == null) {
            try {
                // Create a trust manager that does not validate certificate chains
                trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {

                    }
                }
                };

                // Install the all-trusting trust manager
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

                // Create all-trusting host name verifier
                HostnameVerifier allHostsValid = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };

                // Install the all-trusting host verifier
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Connect with REST Server in HTTP ou HTTPS with selfsigned certificate
     * accepted
     *
     * @return
     * @throws Exception
     */
    private HttpURLConnection connect() throws Exception {
        HttpURLConnection conn = null;
        URL url = new URL(this.url);
        String protocol = url.getProtocol();
        // if use SSL layer bypass trust validation certificate
        if (protocol.equalsIgnoreCase("HTTPS")) {
            disableSslVerification();

            // Install the all-trusting trust manager
            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
            }
            conn = (HttpsURLConnection) url.openConnection();
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }

        return conn;
    }

    /**
     * get an array of byte for image for exemple
     *
     * @return the byte array
     * @throws Exception
     */
    protected final byte[] getRaw() throws Exception {
        byte[] data = null;
        HttpURLConnection conn = this.connect();
        URL url = new URL(this.url);
        conn.setRequestMethod("GET");
        int status = conn.getResponseCode();

        switch (status) {
            case 200:
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream is = null;
                try {
                    is = conn.getInputStream();
                    byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
                    int n;

                    while ((n = is.read(byteChunk)) > 0) {
                        baos.write(byteChunk, 0, n);
                    }

                    data = baos.toByteArray();
                } catch (IOException e) {
                    System.err.printf("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }

            case 204:
                break;

            default:
                throw new RuntimeException("Failed : HTTP error code : "
                        + status
                        + "\n Because : "
                        + conn.getResponseMessage()// response.getStatusLine().getReasonPhrase()
                        + "\n URI = "
                        + this.url);
        }

        conn.disconnect();

        return data;
    }

    /**
     * Main method to realise an HTTP request to the server and get the response
     *
     * @param entity : simple POJO to transfert in JSON to the server
     * @param requestType : type of HTTP request (GET, POST, PUT or DELETE) with
     * corect URL set
     * @return : the JSON stream returned by the server (POST and GET request
     * only)
     * @throws Exception
     */
    private String createRequest(T entity, String requestType) throws Exception {

        URL serverUrl = new URL(this.url);
        HttpURLConnection conn = this.connect();
        //HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
        conn.setRequestMethod(requestType);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        if (entity != null) {
            conn.setDoOutput(true);
            String json = this.gson.toJson(entity);
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();
        }

        int status = conn.getResponseCode();
        String output = null;

        InputStream in = null;

        switch (status) {
            case 200:
                in = conn.getInputStream();
                output = this.getContent(in);

            case 204:
                break;

            default:
                in = conn.getErrorStream();
                output = this.getContent(in);
                throw new Exception(output);
        }

        conn.disconnect();
        return output;
    }

    private String getContent(InputStream in) throws Exception {
        String output;
        BufferedReader br = new BufferedReader(new InputStreamReader((in)));
        StringBuilder buffer = new StringBuilder();
        while ((output = br.readLine()) != null) {
            buffer.append(output);
        }
        output = buffer.toString();

        return output;
    }

    /**
     * Method retreive class name into the JSON stream send by the server
     *
     * @param data : the JSON stream
     * @return the Class corresponding of the JSON parameter className
     * @throws ClassNotFoundException
     */
    protected final Class getClassName(String data) throws ClassNotFoundException {
        int index = data.lastIndexOf("class");
        int begin = data.indexOf(":\"", index) + 2;
        int end = data.indexOf("\"", begin);
        String name = data.substring(begin, end);
        return Class.forName(name);
    }
}