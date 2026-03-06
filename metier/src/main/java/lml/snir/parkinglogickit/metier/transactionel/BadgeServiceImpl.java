package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;

import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.physique.data.BadgeDataService;
import lml.snir.parkinglogickit.physique.data.PhysiqueDataFactory;

/**
 *
 * @author Phily Seck
 */
public class BadgeServiceImpl implements BadgeService {
   private final BadgeDataService badgeDataSrv;
   
  public BadgeServiceImpl() throws Exception {
      this.badgeDataSrv = PhysiqueDataFactory.getBadgeDataService();
  }   

    @Override
    public Badge getById(Long id) throws Exception {
        return this.badgeDataSrv.getById(id);
    }

    @Override
    public Badge getByContent(String content) throws Exception {
        return this.badgeDataSrv.getByContent(content);
    }

    @Override
    public Badge add(Badge t) throws Exception {
         return this.badgeDataSrv.add(t);
    }


    @Override
    public void remove(Badge t) throws Exception {
     this.badgeDataSrv.remove(t);
        
    }


    @Override
    public void update(Badge t) throws Exception {
     this.badgeDataSrv.update(t);
        
    }

    @Override
    public long getCount() throws Exception {
         return this.badgeDataSrv.getCount();
    }

    @Override
    public List<Badge> getAll() throws Exception {
       return this.badgeDataSrv.getAll();
    }

    @Override
    public List<Badge> getAll(int begin, int count) throws Exception {
        return this.badgeDataSrv.getAll(begin, count);
    }

}
