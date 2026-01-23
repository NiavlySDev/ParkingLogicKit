/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package lml.snir.parklogickit.metier.transactionel;

import lml.snir.parklogickit.metier.entity.Event;
import lml.snir.persistence.CrudService;
/**
 *
 * @author phily
 */
   public interface EventService extends CrudService<Event> {

    public Event getById(long id) throws Exception;
    public Event getByIsEntered(boolean attribue) throws Exception;
   
   }