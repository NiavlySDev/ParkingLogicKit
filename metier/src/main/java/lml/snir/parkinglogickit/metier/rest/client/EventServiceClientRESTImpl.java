package lml.snir.parkinglogickit.metier.rest.client;


import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Event;
import lml.snir.parkinglogickit.metier.entity.Places;
import lml.snir.parkinglogickit.metier.transactionel.EventService;
import lml.snir.rest.client.ClientRest;

/**
 *
 * @author phily
 */
public class EventServiceClientRESTImpl extends ClientRest<Places> implements EventService {

    public EventServiceClientRESTImpl() {
        super.init("EventService", new RestServerLocalConfiguration());
    }

    @Override
    public Event getById(long id) throws Exception {
      super.setPath("getById/" );
        return null;
    }

    @Override
    public Event getByIsEntered(boolean attribue) throws Exception {
      super.setPath("getByIsEntered/" );
        return null;
    }

    @Override
    public Event add(Event t) throws Exception {
        super.setPath("add/" );
        return null;   
    }

    @Override
    public void remove(Event t) throws Exception {
      super.setPath("remove/" );
    }
    
    @Override
    public void update(Event t) throws Exception {
    super.setPath("update/" );
    }

    @Override
    public Event getById(Long id) throws Exception {
        super.setPath("getById/" );
        return null;
    }

    @Override
    public long getCount() throws Exception {
        super.setPath("getCount/" );
        return 0;
    }

    @Override
    public List<Event> getAll() throws Exception {
       super.setPath("getAll/" );
        return null;
    }

    @Override
    public List<Event> getAll(int begin, int count) throws Exception {
       super.setPath("" + begin + "/" + count);
        return null;
    }
}
   