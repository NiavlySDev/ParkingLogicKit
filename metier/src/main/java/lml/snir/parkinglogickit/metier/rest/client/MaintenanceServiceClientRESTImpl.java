package lml.snir.parkinglogickit.metier.rest.client;

import lml.snir.parkinglogickit.metier.entity.Maintenance;
import lml.snir.parkinglogickit.metier.transactionel.MaintenanceService;
import lml.snir.rest.client.ClientRest;

/**
 *
 * @author Phily Seck
 */
public class MaintenanceServiceClientRESTImpl extends ClientRest<Maintenance> implements MaintenanceService {

    public MaintenanceServiceClientRESTImpl() {
        super.init("DriverService", new RestServerLocalConfiguration());
    }
}
