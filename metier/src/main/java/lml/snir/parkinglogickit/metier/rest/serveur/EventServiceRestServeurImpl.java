package lml.snir.parkinglogickit.metier.rest.serveur;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import java.util.List;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;
import lml.snir.parkinglogickit.metier.entity.Event;
import lml.snir.parkinglogickit.metier.transactionel.EventService;
import lml.snir.rest.server.RestException;

/**
 *
 * @author fanou
 */
@Path("/EventService")
@Consumes("application/json")
@Produces("application/json")
public class EventServiceRestServeurImpl {

    private final EventService EventSrv;

    public EventServiceRestServeurImpl() throws Exception {
        this.EventSrv = MetierFactory.getEventService();
    }

    @POST
    @Path("/")
    public Event add(Event t) throws Exception {
        try {
            return this.EventSrv.add(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @DELETE
    @Path("/")
    public void remove(Event t) throws Exception {
        try {
            this.EventSrv.remove(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @PUT
    @Path("/")
    public void update(Event t) throws Exception {
        try {
            this.EventSrv.update(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Event getById(@PathParam("id") Long id) throws Exception {
        try {
            return this.EventSrv.getById(id);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/Count")
    public long getCount() throws Exception {
        try {
            return this.EventSrv.getCount();
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/")
    public List<Event> getAll() throws Exception {
        try {
            return this.EventSrv.getAll();
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/{begin}/{count}")
    public List<Event> getAll(@PathParam("begin") int begin, @PathParam("count") int count) throws Exception {
        try {
            return this.EventSrv.getAll(begin, count);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    

  
    @GET
    @Path("/{isEntered}")
    public Event getByIsEntered(@PathParam("") boolean isEntered) throws Exception {
        try {
            return this.EventSrv.getByIsEntered(isEntered);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
}
}
