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
import lml.snir.parkinglogickit.metier.entity.Places;
import lml.snir.parkinglogickit.metier.transactionel.PlacesService;
import lml.snir.rest.server.RestException;

/**
 *
 * @author Phily Seck
 */
@Path("/PlacesService")
@Consumes("application/json")
@Produces("application/json")
public class PlacesServiceRestServeurImpl {

    private final PlacesService PlacesSrv;

    public PlacesServiceRestServeurImpl() throws Exception {
        this.PlacesSrv = MetierFactory.getPlacesService();
    }

    @POST
    @Path("/")
    public Places add(Places t) throws Exception {
        try {
            return this.PlacesSrv.add(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @DELETE
    @Path("/")
    public void remove(Places t) throws Exception {
        try {
            this.PlacesSrv.remove(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @PUT
    @Path("/")
    public void update(Places t) throws Exception {
        try {
            this.PlacesSrv.update(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Places getById(@PathParam("id") Long id) throws Exception {
        try {
            return this.PlacesSrv.getById(id);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/Count")
    public long getCount() throws Exception {
        try {
            return this.PlacesSrv.getCount();
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/")
    public List<Places> getAll() throws Exception {
        try {
            return this.PlacesSrv.getAll();
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/{begin}/{count}")
    public List<Places> getAll(@PathParam("begin") int begin, @PathParam("count") int count) throws Exception {
        try {
            return this.PlacesSrv.getAll(begin, count);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/{IsOccuped}")
    public Places getByIsOccuped(@PathParam("") boolean isOccuped) throws Exception {
        try {
            return this.PlacesSrv.getByIsOccuped(isOccuped);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }
}
