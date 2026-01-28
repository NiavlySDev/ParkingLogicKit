/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lml.snir.parklogickit.metier.transactionel;

import java.util.List;

import lml.snir.parklogickit.metier.entity.Access;
import lml.snir.parklogickit.physique.data.AccessDataService;

/**
 *
 * @author fanou
 */
public class AccessServiceImpl implements AccessService {

//    private final AccessDataService accessDataSrv;

//    public AccessServiceImpl() throws Exception {
//        this.accessDataSrv = PhysiqueDataFactory.getAccessDataService();
//    }

    @Override
    public Access getById(long id) throws Exception {
        return null;
//        return this.accessDataSrv.getById(id);
    }

    @Override
    public Access getByDriver(String contenu) throws Exception {
        return null;
//       return this.accessDataSrv.getByDriver();
    }

    @Override
    public Access getByBadge(String contenu) throws Exception {
        return null;
//        return this.accessDataSrv.getByBadge();
    }

    @Override
    public Access getByDateTime(String contenu) throws Exception {
        return null;
//     return this.accessDataSrv.getByDateTime();
    }

    @Override
    public Access getByEvent(String contenu) throws Exception {
        return null;
//     return this.accessDataSrv.getByEvent();
    }

    @Override
    public Access add(Access t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void remove(Access t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(Access t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Access getById(Long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public long getCount() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Access> getAll() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Access> getAll(int begin, int count) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

  
}


