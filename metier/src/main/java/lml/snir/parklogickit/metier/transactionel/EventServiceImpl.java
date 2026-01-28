package lml.snir.parklogickit.metier.transactionel;

import java.util.List;


import lml.snir.parklogickit.metier.entity.Event;
import lml.snir.parklogickit.data.EventDataService;


/**
 *
 * @author fanou
 */
public class EventServiceImpl implements EventService {
    private final EventDataService eventDataSrv;
    
    public EventServiceImpl() throws Exception {
        this.eventDataSrv = PhysiqueDataFactory.getEventDataService();
    }





    @Override
    public Event getById(long id) throws Exception {
         return this.eventDataSrv.getById(id);
    }
    @Override
    public Event getByIsEntered(boolean attribue) throws Exception {
      return this.eventDataSrv.getByEntered();
    }


 
    
}
