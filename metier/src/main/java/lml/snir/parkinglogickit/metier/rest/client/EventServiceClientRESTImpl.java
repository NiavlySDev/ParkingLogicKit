package lml.snir.parkinglogickit.metier.rest.client;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Event;
import lml.snir.parkinglogickit.metier.entity.Places;
import lml.snir.parkinglogickit.metier.transactionel.EventService;
import lml.snir.rest.client.ClientRest;

/**
 *
 * @author Phily Seck
 */
public class EventServiceClientRESTImpl extends ClientRest<Event> implements EventService {

    public EventServiceClientRESTImpl() {
        super.init("EventService", new RestServerLocalConfiguration());
    }

    @Override
    public Event add(Event t) throws Exception {
        super.setPath("");
        return super.addEntity(t);
    }

    @Override
    public void remove(Event t) throws Exception {
        super.setPath("");
        super.removeEntity(t);
    }

    @Override
    public void update(Event t) throws Exception {
        super.setPath("");
        super.updateEntity(t);
    }

    @Override
    public Event getById(Long id) throws Exception {
        super.setPath("" + id);
        return super.getEntity();
    }

    @Override
    public long getCount() throws Exception {
        super.setPath("Count");
        return super.getCountEntity();
    }

    @Override
    public List<Event> getAll() throws Exception {
        super.setPath("");
        return super.getEntitys();
    }

    @Override
    public List<Event> getAll(int begin, int count) throws Exception {
        super.setPath("" + begin + "/" + count);
        return super.getEntitys();
    }
}
