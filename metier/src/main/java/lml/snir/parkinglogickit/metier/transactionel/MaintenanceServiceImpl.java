package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;


import lml.snir.parkinglogickit.metier.entity.Maintenance;
//import lml.snir.parklogickit.data.MaintenanceDataService;

/**
 *
 * @author phily
 */
public final class MaintenanceServiceImpl implements MaintenanceService {
//    private final MaintenanceDataService MaintenanceDataSrv;
    
//    public MaintenanceServiceImpl() throws Exception {
//        this.MaintenanceDataSrv = PhysiqueDataFactory.getLocalDataService();
//    }


  

    @Override
    public Maintenance add(Maintenance t) throws Exception {
          return null;
//      return this.prkDataSrv.add();
    }
    @Override
    public void remove(Maintenance t) throws Exception {
//      return this.prkDataSrv.remove();
        
    }

    @Override
    public void update(Maintenance t) throws Exception {
//      return this.prkDataSrv.update();
        
    }

    @Override
    public Maintenance getById(Long id) throws Exception {
          return null;
//      return this.prkDataSrv.getById();
    }

    @Override
    public long getCount() throws Exception {
        return 0;
    }

    @Override
    public List<Maintenance> getAll() throws Exception {
         return null;
//      return this.prkDataSrv.getAll();
    }

    @Override
    public List<Maintenance> getAll(int begin, int count) throws Exception {
         return null;
//      return this.prkDataSrv.getAll();
    }



    
}
