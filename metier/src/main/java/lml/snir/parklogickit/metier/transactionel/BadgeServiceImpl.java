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
    public Badge getById(Long id) throws Exception {
        return this.badgeDataSrv.getById(id);
    }


    public Badge getByContent(String content) throws Exception {
        return this.badgeDataSrv.getByContent(content);
    }


    
}
