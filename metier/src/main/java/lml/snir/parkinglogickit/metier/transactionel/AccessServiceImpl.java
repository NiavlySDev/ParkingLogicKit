package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Access;
import lml.snir.parkinglogickit.physique.data.AccessDataService;
import lml.snir.parkinglogickit.physique.data.PhysiqueDataFactory;

/**
 *
 * @author Phily Seck
 */
public final class AccessServiceImpl implements AccessService {
    private final AccessDataService accessDataSrv;
    
    public AccessServiceImpl() throws Exception {
        this.accessDataSrv = PhysiqueDataFactory.getAccessDataService();
    }
    
    @Override
    public List<Access> getByDriver(String driver) throws Exception {
        return this.accessDataSrv.getByDate(driver);
    }

    @Override
    public List<Access> getByDate(String date) throws Exception {
        return this.accessDataSrv.getByDate(date);
    }

    @Override
    public List<Access> getByIsOpen(boolean attribue) throws Exception {
        return this.accessDataSrv.getByIsOpen(attribue);
    }

    @Override
    public Access add(Access t) throws Exception {
        return this.accessDataSrv.add(t);
    }

    @Override
    public void remove(Access t) throws Exception {
      this.accessDataSrv.remove(t);
    }

    @Override
    public void update(Access t) throws Exception {
        this.accessDataSrv.update(t);
    }

    @Override
    public Access getById(Long id) throws Exception {
         return this.accessDataSrv.getById( id);
    }

    @Override
    public long getCount() throws Exception {
          return this.accessDataSrv.getCount();
    }

    @Override
    public List<Access> getAll() throws Exception {
           return this.accessDataSrv.getAll();
    }

    @Override
    public List<Access> getAll(int begin, int count) throws Exception {
          return this.accessDataSrv.getAll(begin,count);
    }
}
