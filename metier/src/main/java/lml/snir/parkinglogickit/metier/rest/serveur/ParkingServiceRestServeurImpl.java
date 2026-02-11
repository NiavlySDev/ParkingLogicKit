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
import lml.snir.parkinglogickit.metier.entity.Parking;
import lml.snir.parkinglogickit.metier.transactionel.ParkingService;
import lml.snir.rest.server.RestException;

/**
 *
 * @author phily
 */
@Path("/ParkingService")
@Consumes("application/json")
@Produces("application/json")
public class ParkingServiceRestServeurImpl {
    private final ParkingService ParkingSrv;

    public ParkingServiceRestServeurImpl() throws Exception {
        this.ParkingSrv = MetierFactory.getParkingService();
    }

    @POST
    @Path("/")
    public Parking add(Parking t) throws Exception {
        try {
            return this.ParkingSrv.add(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @DELETE
    @Path("/")
    public void remove(Parking t) throws Exception {
        try {
            this.ParkingSrv.remove(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @PUT
    @Path("/")
    public void update(Parking t) throws Exception {
        try {
            this.ParkingSrv.update(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Parking getById(@PathParam("id") Long id) throws Exception {
        try {
            return this.ParkingSrv.getById(id);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/Count")
    public long getCount() throws Exception {
        try {
            return this.ParkingSrv.getCount();
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/")
    public List<Parking> getAll() throws Exception {
        try {
            return this.ParkingSrv.getAll();
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/{begin}/{count}")
    public List<Parking> getAll(@PathParam("begin") int begin, @PathParam("count") int count) throws Exception {
        try {
            return this.ParkingSrv.getAll(begin, count);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }
      
    @GET
    @Path("/{IsFull}")
    public Parking getByIsFull(@PathParam("") boolean isFull) throws Exception {
        try {
            return this.ParkingSrv.getByIsFull (isFull);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
}
    
     @GET
    @Path("/{CountPLace}")
    public Parking getByCountPLace(@PathParam("CountPLace")  int begin ) throws Exception {
        try {
            return this.ParkingSrv.getByCountPlace(begin);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

}
