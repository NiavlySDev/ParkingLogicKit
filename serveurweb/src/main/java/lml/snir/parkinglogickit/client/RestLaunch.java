package lml.snir.parkinglogickit.client;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lml.snir.parkinglogickit.metier.rest.serveur.*;

@ApplicationPath("rest")
/**
 * WARNING Wildfly Server Use RESTEASY Lib WE MUST Configure web.xml file and properties to create wadl file and add class parameter
 * for testing
 * http://localhost:8080/ParkingLogicKitServeur/rest/TemperatureService
 * Class for register Web Service
 */
public class RestLaunch extends Application {
    // the entry class - deliberately empty in this basic demo
    public RestLaunch() {
        System.out.println("lml.snir.test.client.App.<init>()");
    }
    
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<>();
        // register root resource
        classes.add(DriverServiceRestServeurImpl.class);
        classes.add(BadgeServiceRestServeurImpl.class);
        classes.add(VehicleServiceRestServeurImpl.class);
       //sclasses.add(PlacesServiceRestServeurImpl.class);
       // classes.add(MaintenanceServiceRestServeurImpl.class);
       // classes.add(AccessServiceRestServeurImpl.class);
        //classes.add(AdminServiceRestServeurImpl.class);
        classes.add(EventServiceRestServeurImpl.class);
        classes.add(ParkingServiceRestServeurImpl.class);
        return classes;
    }
    
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