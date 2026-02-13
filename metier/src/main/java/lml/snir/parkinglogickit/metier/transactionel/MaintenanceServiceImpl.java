package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;

import lml.snir.parkinglogickit.metier.entity.Maintenance;
//import lml.snir.parklogickit.data.MaintenanceDataService;

/**
 *
 * @author Phily Seck
 */
public final class MaintenanceServiceImpl implements MaintenanceService {
//    private final MaintenanceDataService MaintenanceDataSrv;

//    public MaintenanceServiceImpl() throws Exception {
//        this.MaintenanceDataSrv = PhysiqueDataFactory.getLocalDataService();
//    }
    @Override
    public Maintenance add(Maintenance t) throws Exception {
        return null;
//      return this.MaintenanceDataSrv.add();
    }

    @Override
    public void remove(Maintenance t) throws Exception {
//      return this.MaintenanceDataSrv.remove();

    }

    @Override
    public void update(Maintenance t) throws Exception {
//      return this.MaintenanceDataSrv.update();

    }

    @Override
    public Maintenance getById(Long id) throws Exception {
        return null;
//      return this.MaintenanceDataSrv.getById();
    }

    @Override
    public long getCount() throws Exception {
        return 0;
    }

    @Override
    public List<Maintenance> getAll() throws Exception {
        return null;
//      return this.MaintenanceDataSrv.getAll();
    }

    @Override
    public List<Maintenance> getAll(int begin, int count) throws Exception {
        return null;
//      return this.MaintenanceDataSrv.getAll(int begin, int count);
    }

}
