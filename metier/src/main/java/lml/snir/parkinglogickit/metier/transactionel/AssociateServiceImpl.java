package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Associate;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.physique.data. AssociateDataService;
import lml.snir.parkinglogickit.physique.data.PhysiqueDataFactory;

/**
 *
 * @author Phily Seck
 */
public final class AssociateServiceImpl implements AssociateService {
    private final  AssociateDataService  AssociateDataSrv;

    public AssociateServiceImpl() throws Exception {
        this.AssociateDataSrv = PhysiqueDataFactory.getAssociateDataService();
    }

   
    @Override
    public Associate add(Associate t) throws Exception {
        return this.AssociateDataSrv.add(t);
    }

    @Override
    public void remove(Associate t) throws Exception {
        this.AssociateDataSrv.remove(t);
    }

    @Override
    public void update(Associate t) throws Exception {
         this.AssociateDataSrv.update(t);
    }
    @Override
    public Associate getById(Long id) throws Exception {
       return this.AssociateDataSrv.getById(id);
    }

    @Override
    public long getCount() throws Exception {
        return this.AssociateDataSrv.getCount();
    }
    @Override
    public List<Associate> getAll() throws Exception {
         return this.AssociateDataSrv.getAll();
    }

    @Override
    public List<Associate> getAll(int begin, int count) throws Exception {
        return this.AssociateDataSrv.getAll(begin,count);
    }

    @Override
    public Associate getByBadge(Badge badge) throws Exception {
         return this.AssociateDataSrv.getByBadge(badge);
    }
    @Override
    public Associate getByUtilisateur(Driver drv) throws Exception {
        return this.AssociateDataSrv.getByUtilisateur(drv);
    }
  
}