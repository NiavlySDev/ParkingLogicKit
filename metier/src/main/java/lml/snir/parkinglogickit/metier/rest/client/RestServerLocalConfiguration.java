package lml.snir.parkinglogickit.metier.rest.client;

import lml.snir.rest.client.RestServerConfig;
import lml.snir.tools.ConfigReader;

/**
 *
 * @author Phily Seck
 */
public class RestServerLocalConfiguration extends RestServerConfig {

    // http://localhost:8080/ParkingLogicKitServeur/rest/BadgeService
    // http://localhost:8080/ParkingLogicKitServeur/rest/application.wadl
    // http://localhost:8080/ParkingLogicKitServeur/rest/DriverService/getById/HS?login=PLK&pass=PASSPLK
//    http://localhost:8080/ParkingLogicKitServeur/rest/BadgeService/getByContent/0014511054?login=PLK&pass=PASSPLK
//  http://localhost:8080/ParkingLogicKitServeur/rest/ParkingService/getByIsFull/0?login=PLK&pass=PASSPLK
    private static String url = "http://localhost:8080"; // default value

    static {
        url = ConfigReader.getInstance("./rest.properties").getProperty("server");
    }

    public RestServerLocalConfiguration() {
        super(url, "ParkingLogicKitServeur", "rest");
    }

}
