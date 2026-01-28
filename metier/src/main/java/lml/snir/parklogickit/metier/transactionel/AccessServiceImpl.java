/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lml.snir.parklogickit.metier.transactionel;

import java.util.List;

import lml.snir.parklogickit.metier.entity.Access;

/**
 *
 * @author fanou
 */
public class AccessServiceImpl implements AccessService {

    private final AccessDataService accessDataSrv;

    public AccessServiceImpl() throws Exception {
        this.accessDataSrv = PhysiqueDataFactory.getAccessDataService();
    }

    @Override
    public Access getById(long id) throws Exception {
        return this.accessDataSrv.getById(id);
    }

    @Override
    public Access getByDriver(String contenu) throws Exception {
       return this.accessDataSrv.getByDriver();
    }

    @Override
    public Access getByBadge(String contenu) throws Exception {
        return this.accessDataSrv.getByBadge();
    }

    @Override
    public Access getByDateTime(String contenu) throws Exception {
     return this.accessDataSrv.getByDateTime();
    }

    @Override
    public Access getByEvent(String contenu) throws Exception {
     return this.accessDataSrv.getByEvent();
    }

}


