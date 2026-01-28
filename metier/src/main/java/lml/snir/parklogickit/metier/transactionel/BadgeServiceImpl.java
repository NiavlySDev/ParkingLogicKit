package lml.snir.parklogickit.metier.transactionel;

import java.util.List;


import lml.snir.parklogickit.metier.entity.Badge;
//import lml.snir.parklogickit.data.BadgeDataService;


/**
 *
 * @author fanou
 */
public class BadgeServiceImpl implements BadgeService {
//    private final BadgeDataService badgeDataSrv;
//    
//    public BadgeServiceImpl() throws Exception {
//        this.badgeDataSrv = PhysiqueDataFactory.getBadgeDataService();
//    }

    

    @Override
    public Badge getById(Long id) throws Exception {
        return null;
//        return this.badgeDataSrv.getById(id);
    }


    public Badge getByContent(String content) throws Exception {
        return null;
//        return this.badgeDataSrv.getByContent(content);
    }

    @Override
    public Badge getById(long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Badge getByContent(boolean attribue) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Badge add(Badge t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void remove(Badge t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(Badge t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public long getCount() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Badge> getAll() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Badge> getAll(int begin, int count) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    
}
