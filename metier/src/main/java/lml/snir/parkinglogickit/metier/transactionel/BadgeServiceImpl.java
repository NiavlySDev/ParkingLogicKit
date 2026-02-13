package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;

import lml.snir.parkinglogickit.metier.entity.Badge;
//import lml.snir.parklogickit.data.BadgeDataService;

/**
 *
 * @author Phily Seck
 */
public class BadgeServiceImpl implements BadgeService {
//    private final BadgeDataService badgeDataSrv;
//    
//    public BadgeServiceImpl() throws Exception {
//        this.badgeDataSrv = PhysiqueDataFactory.getBadgeDataService();
//    

    @Override
    public Badge getById(Long id) throws Exception {
        return null;
//        return this.badgeDataSrv.getById(id);
    }

    @Override
    public Badge getByContent(String content) throws Exception {
        return null;
//        return this.badgeDataSrv.getByContent(content);
    }

    @Override
    public Badge add(Badge t) throws Exception {
       return null;
//        return this.badgeDataSrv.add();
    }


    @Override
    public void remove(Badge t) throws Exception {
//        return this.badgeDataSrv.remove();
        
    }


    @Override
    public void update(Badge t) throws Exception {
//        return this.badgeDataSrv.update();
        
    }

    @Override
    public long getCount() throws Exception {
        return 0;
//        return this.badgeDataSrv.getCount(count);
    }

    @Override
    public List<Badge> getAll() throws Exception {
       return null;
//        return this.badgeDataSrv.All();
    }

    @Override
    public List<Badge> getAll(int begin, int count) throws Exception {
       return null;
//        return this.badgeDataSrv.All();
    }

}
