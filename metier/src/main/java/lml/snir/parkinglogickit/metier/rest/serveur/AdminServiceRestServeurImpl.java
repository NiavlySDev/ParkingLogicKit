/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lml.snir.parkinglogickit.metier.rest.serveur;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import lml.snir.parkinglogickit.metier.transactionel.AdminService;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;

/**
 *
 * @author phily
 */
@Path("/AdminService")
@Consumes("application/json")
@Produces("application/json")
public class AdminServiceRestServeurImpl {
    

    public AdminServiceRestServeurImpl() throws Exception {
        MetierFactory.getAdminService();
    }

}
