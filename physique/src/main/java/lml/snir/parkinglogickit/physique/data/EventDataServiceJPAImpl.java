/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lml.snir.parkinglogickit.physique.data;

import lml.snir.parkinglogickit.metier.entity.Event;
import lml.snir.persistence.jpa.AbstracCrudServiceJPA;

/**
 *
 * @author jupiter
 */
public class EventDataServiceJPAImpl extends AbstracCrudServiceJPA<Event> implements EventDataService {
    
        public EventDataServiceJPAImpl(String PU) {
        super(PU);
    }
}
