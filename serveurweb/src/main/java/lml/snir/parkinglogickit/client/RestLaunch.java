package lml.snir.parkinglogickit.client;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lml.snir.parkinglogickit.metier.rest.serveur.*;

/**
 * Classe de démarrage REST du serveur web.
 *
 * @author Sylvain Crocquevieille
 */
@ApplicationPath("rest")
public class RestLaunch extends Application {

    /**
     * Construit une instance de RestLaunch.
     */
    public RestLaunch() {
        System.out.println("lml.snir.test.client.App.<init>()");
    }

    /**
     * Retourne classes.
     *
     * @return Set<Class<?>> : valeur retournée par la méthode
     */
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<>();
        classes.add(AccessServiceRestServeurImpl.class);
        //classes.add(AdminServiceRestServeurImpl.class);
        classes.add(AssociateServiceRestServeurImpl.class);
        classes.add(BadgeServiceRestServeurImpl.class);
        classes.add(DriverServiceRestServeurImpl.class);
        classes.add(ParkingServiceRestServeurImpl.class);
        classes.add(VehicleServiceRestServeurImpl.class);

        return classes;
    }

    /**
     * Retourne properties.
     *
     * @return Object> : valeur retournée par la méthode
     */
    @Override
    public Map<String, Object> getProperties() {
        System.out.println(">>>>>>>>>>>>>>>> get properties");
        Map<String, Object> props = new HashMap<>();
        props.put("org.jboss.resteasy.wadl.ResteasyWadlServlet", "/application.wadl");

        props.put("com.sun.jersey.api.json.POJOMappingFeature", true);
        props.put("jersey.config.server.provider.packages", "lml.snir.rest.server");
        return props;
    }

}
