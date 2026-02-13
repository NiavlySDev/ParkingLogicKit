package lml.snir.parkinglogickit.metier.rest.serveur;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;

/**
 *
 * @author Phily Seck
 */
@Path("/AdminService")
@Consumes("application/json")
@Produces("application/json")
public class AdminServiceRestServeurImpl {

    public AdminServiceRestServeurImpl() throws Exception {
        MetierFactory.getAdminService();
    }

}
