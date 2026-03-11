package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;

import lml.snir.parkinglogickit.metier.entity.Access;
import lml.snir.parkinglogickit.physique.data.AccessDataService;
import lml.snir.parkinglogickit.physique.data.PhysiqueDataFactory;

/**
 *
 * @author Phily Seck
 */
public class AccessServiceImpl implements AccessService {

    private final AccessDataService accessDataSrv;
    public AccessServiceImpl() throws Exception {
        this.accessDataSrv = PhysiqueDataFactory.getAccessDataService();
    }
    
    @Override
    public Access getById(long id) throws Exception {
          return this.accessDataSrv.getById( id);
    }

    @Override
    public Access getByDriver(String contenu) throws Exception {
        return this.accessDataSrv.getByDriver(contenu);

    }

    @Override
    public Access getByDateTime(String contenu) throws Exception {
      return this.accessDataSrv.getByDateTime(contenu);
    }
    
      @Override
    public Access getByBadge(String contenu) throws Exception {
      return this.accessDataSrv.getByBadge(contenu);
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

    @Override
    public Access getByContenu(String contenu) throws Exception {
      return this.accessDataSrv.getByContenu(contenu);
    }

}
