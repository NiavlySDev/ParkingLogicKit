package lml.snir.parkinglogickit.metier.rest.serveur;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;

/**
 *
 * @author Phily Seck
 */
@Path("/MaintenanceService")
@Consumes("application/json")
@Produces("application/json")
public class MaintenanceServiceRestServeurImpl {

    public MaintenanceServiceRestServeurImpl() throws Exception {
        MetierFactory.getMaintenanceService();
    }
}
