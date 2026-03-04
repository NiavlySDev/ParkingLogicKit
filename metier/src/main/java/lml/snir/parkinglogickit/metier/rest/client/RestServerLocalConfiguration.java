package lml.snir.parkinglogickit.metier.rest.client;

import lml.snir.rest.client.RestServerConfig;
import lml.snir.tools.ConfigReader;

/**
 *
 * @author Phily Seck
 */
public class RestServerLocalConfiguration extends RestServerConfig {

    // http://localhost:8080/ParkingLogicKitServeur /rest/BadgeService
    // http://localhost:8080/ParkingLogicKitServeur/rest/application.wadl
    private static String url = "http://localhost:8080"; // default value

    static {
        url = ConfigReader.getInstance("./rest.properties").getProperty("server");
    }

    public RestServerLocalConfiguration() {
        super(url, "ParkingLogicKitServeur", "rest");
    }

}
