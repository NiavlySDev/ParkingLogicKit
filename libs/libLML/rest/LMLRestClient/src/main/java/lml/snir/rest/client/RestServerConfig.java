package lml.snir.rest.client;

public abstract class RestServerConfig {

    protected final String URL_SERVER_BASE;
    protected final String URL_SERVER_SITE;
    protected final String URL_SERVER_REST;

    protected RestServerConfig(String URL_SERVER_BASE, String URL_SERVER_SITE, String URL_SERVER_REST) {
        this.URL_SERVER_BASE = URL_SERVER_BASE;
        this.URL_SERVER_REST = URL_SERVER_REST;
        this.URL_SERVER_SITE = URL_SERVER_SITE;
    }

    @Override
    public String toString() {
        String st = this.URL_SERVER_BASE;
        if (this.URL_SERVER_SITE.length() > 0) {
            st += "/" + this.URL_SERVER_SITE;
        }
        st += "/" + this.URL_SERVER_REST;
        
        System.out.println("RestServerConfiguration.toString() = " + st);
        return st;
    }

}
