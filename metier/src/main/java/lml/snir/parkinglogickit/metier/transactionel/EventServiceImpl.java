package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;

import lml.snir.parkinglogickit.metier.entity.Event;
//import lml.snir.parklogickit.data.EventDataService;

/**
 *
 * @author Phily Seck
 */
public class EventServiceImpl implements EventService {
//    private final EventDataService eventDataSrv;
//    
//    public EventServiceImpl() throws Exception {
//        this.eventDataSrv = PhysiqueDataFactory.getEventDataService();
//    }

    @Override
    public Event getById(long id) throws Exception {
        return null;
//         return this.eventDataSrv.getById(id);
    }

    @Override
    public Event add(Event t) throws Exception {
        return null;
//         return this.eventDataSrv.add();
    }

    @Override
    public void remove(Event t) throws Exception {
//         return this.eventDataSrv.remove();
    }

    @Override
    public void update(Event t) throws Exception {
//         return this.eventDataSrv.update();
    }

    @Override
    public Event getById(Long id) throws Exception {
        return null;
//         return this.eventDataSrv.getById(Long id);
    }

    @Override
    public long getCount() throws Exception {
                return 0;
//         return this.eventDataSrv.getById(id);
    }

    @Override
    public List<Event> getAll() throws Exception {
        return null;
//         return this.eventDataSrv.All();
    }

    @Override
    public List<Event> getAll(int begin, int count) throws Exception {
         return null;
//         return this.eventDataSrv.All(int begin, int count);
    }

}
