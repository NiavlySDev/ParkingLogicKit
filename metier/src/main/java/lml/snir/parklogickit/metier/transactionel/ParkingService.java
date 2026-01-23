/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package lml.snir.parklogickit.metier.transactionel;

import lml.snir.parklogickit.metier.entity.Parking;
import lml.snir.persistence.CrudService;
/**
 *
 * @author phily
 */
   public interface ParkingService extends CrudService<Parking> {

   
    public Parking getByLogin(String contenu) throws Exception;
    public Parking getByPassword(String contenu) throws Exception;
    public Parking getByIsFull(boolean attribue) throws Exception;
     public Parking getByMaxPlace(String contenu) throws Exception;
   }