package lml.snir.parkinglogickit.metier.rest.client;


import lml.snir.parkinglogickit.metier.entity.Admin;
import lml.snir.parkinglogickit.metier.transactionel. AdminService;
import lml.snir.rest.client.ClientRest;

/**
 *
 * @author fanou
 */
public class AdminServiceClientRESTImpl extends ClientRest< Admin> implements  AdminService {

    public AdminServiceClientRESTImpl() {
        super.init("DriverService", new RestServerLocalConfiguration());
    }
}