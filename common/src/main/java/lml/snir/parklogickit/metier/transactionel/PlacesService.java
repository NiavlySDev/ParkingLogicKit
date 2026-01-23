/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package lml.snir.parklogickit.metier.transactionel;

import lml.snir.parklogickit.metier.entity.Places;
import lml.snir.persistence.CrudService;
/**
 *
 * @author phily
 */
   public interface PlacesService extends CrudService<Places> {

   
    public Places getByIsOccuped(boolean attribue) throws Exception;
     
   }