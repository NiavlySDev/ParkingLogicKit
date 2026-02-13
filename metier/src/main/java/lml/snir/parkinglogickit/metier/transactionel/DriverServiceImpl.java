package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;

import lml.snir.parkinglogickit.metier.entity.Driver;
//import lml.snir.parklogickit.data.DriverDataService;

/**
 *
 * @author Phily Seck
 */
public final class DriverServiceImpl implements DriverService {
//    private final DriverDataService drvDataSrv;
//    
//    public DriverServiceImpl() throws Exception {
//        this.drvDataSrv = PhysiqueDataFactory.getDriverDataService();
//    }

    public Driver getByLogin(String login) throws Exception {
        return null;
//        return this.drvDataSrv.getByLogin(login);
    }

    @Override
    public Driver getById(Long id) throws Exception {
        return null;
//        return this.drvDataSrv.getById(id);
    }

    @Override
    public Driver getById(long id) throws Exception {
         return null;
//        return this.drvDataSrv.getById(id);
    }

    @Override
    public Driver add(Driver t) throws Exception {
  return null;
//      return this.drvDataSrv.add();
    }
    
    @Override
    public void remove(Driver t) throws Exception {
//      return this.drvDataSrv.remove();
        
    }

    @Override
    public void update(Driver t) throws Exception {
//        return this.drvDataSrv.update();
        
    }

    @Override
    public long getCount() throws Exception {
           return 0;
//        return this.drvDataSrv.getCount(Count);
     
        
    }

    @Override
    public List<Driver> getAll() throws Exception {
          return null;
//      return this.drvDataSrv.add();
    }

    @Override
    public List<Driver> getAll(int begin, int count) throws Exception {
         return null;
//      return this.drvDataSrv.add();
    }

    @Override
    public Driver getByUsername(String contenu) throws Exception {
         return null;
//      return this.drvDataSrv.getByUsername(usr);
    }

}
