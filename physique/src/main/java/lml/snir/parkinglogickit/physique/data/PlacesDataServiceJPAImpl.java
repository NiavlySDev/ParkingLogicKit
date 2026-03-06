/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lml.snir.parkinglogickit.physique.data;

import lml.snir.parkinglogickit.metier.entity.Places;
import lml.snir.persistence.jpa.AbstracCrudServiceJPA;

/**
 *
 * @author viralu
 */
public class PlacesDataServiceJPAImpl extends AbstracCrudServiceJPA<Places> implements PlacesDataService {
    
        public PlacesDataServiceJPAImpl(String PU) {
        super(PU);
    }

    @Override
    public Places getByIsOccuped(boolean attribue) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
