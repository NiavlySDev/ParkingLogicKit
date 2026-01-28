package lml.snir.parklogickit.metier.transactionel;

import java.util.List;


import lml.snir.parklogickit.metier.entity.Badge;
import lml.snir.parklogickit.data.BadgeDataService;


/**
 *
 * @author fanou
 */
public class BadgeServiceImpl implements BadgeService {
    private final BadgeDataService badgeDataSrv;
    
    public BadgeServiceImpl() throws Exception {
        this.badgeDataSrv = PhysiqueDataFactory.getBadgeDataService();
    }

    @Override
    public Badge add(Badge t) throws Exception {
        // TO DO ADD business rules here
        return this.badgeDataSrv.add(t);
    }

    @Override
    public void remove(Badge t) throws Exception {
        // TO DO ADD business rules here
        this.badgeDataSrv.remove(t);
    }

    @Override
    public void update(Badge t) throws Exception {
        // TO DO ADD business rules here
        this.badgeDataSrv.update(t);
    }

    @Override
    public Badge getById(Long id) throws Exception {
        return this.badgeDataSrv.getById(id);
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

    public Badge getByContenu(String contenu) throws Exception {
        return this.badgeDataSrv.getByContenu(contenu);
    }

    public List<Badge> getByAttribution(boolean attribue) throws Exception {
        return this.badgeDataSrv.getByAttribution(attribue);
    }

    
}
