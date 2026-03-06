package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;

import lml.snir.parkinglogickit.metier.entity.Event;
import lml.snir.parkinglogickit.physique.data.EventDataService;
import lml.snir.parkinglogickit.physique.data.PhysiqueDataFactory;

/**
 *
 * @author Phily Seck
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
    public Event add(Event t) throws Exception {
         return this.eventDataSrv.add(t);
    }

    @Override
    public void remove(Event t) throws Exception {
         this.eventDataSrv.remove(t);
    }

    @Override
    public void update(Event t) throws Exception {
        this.eventDataSrv.update(t);
    }

    @Override
    public Event getById(Long id) throws Exception {
         return this.eventDataSrv.getById( id);
    }

    @Override
    public long getCount() throws Exception {
         return this.eventDataSrv.getCount();
    }

    @Override
    public List<Event> getAll() throws Exception {
      return this.eventDataSrv.getAll();
    }

    @Override
    public List<Event> getAll(int begin, int count) throws Exception {
        return this.eventDataSrv.getAll(begin,count);
    }

}
