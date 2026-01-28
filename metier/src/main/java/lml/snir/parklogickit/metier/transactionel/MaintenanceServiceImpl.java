package lml.snir.parklogickit.metier.transactionel;

import java.util.List;


import lml.snir.parklogickit.metier.entity.Maintenance;
import lml.snir.parklogickit.data.MaintenanceDataService;

/**
 *
 * @author fanou
 */
public final class MaintenanceServiceImpl implements MaintenanceService {
    private final MaintenanceDataService MaintenanceDataSrv;
    
    public MaintenanceServiceImpl() throws Exception {
        this.MaintenanceDataSrv = PhysiqueDataFactory.getLocalDataService();
    }


    @Override
    public Maintenance getById(long id) throws Exception {
         return this.MaintenanceDataSrv.getById(id);
    }

    @Override
    public Maintenance getByUsername(String contenu) throws Exception {
        return this.MaintenanceDataSrv.getByUsername();
    }

    @Override
    public Maintenance getByPassword(String contenu) throws Exception {
        return this.MaintenanceDataSrv.getByPassword();
    }

    @Override
    public Maintenance getByIsAdmin(boolean attribue) throws Exception {
            return this.MaintenanceDataSrv.getByIsAdmin();
    }



    
}
