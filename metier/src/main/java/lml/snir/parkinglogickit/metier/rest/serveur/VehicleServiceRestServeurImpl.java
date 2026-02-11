
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
import lml.snir.parkinglogickit.metier.entity.Vehicle;
import lml.snir.parkinglogickit.metier.transactionel.PlacesService;
import lml.snir.parkinglogickit.metier.transactionel.VehicleService;
import lml.snir.rest.server.RestException;

/**
 *
 * @author phily
 */
@Path("/VehicleService")
@Consumes("application/json")
@Produces("application/json")
public class VehicleServiceRestServeurImpl {
    private final VehicleService VehicleSrv;

    public VehicleServiceRestServeurImpl() throws Exception {
        this.VehicleSrv = MetierFactory.getVehicleService();
    }
    @POST
    @Path("/")
    public Vehicle add(Vehicle t) throws Exception {
        try {
            return this.VehicleSrv.add(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @DELETE
    @Path("/")
    public void remove(Vehicle t) throws Exception {
        try {
            this.VehicleSrv.remove(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @PUT
    @Path("/")
    public void update(Vehicle t) throws Exception {
        try {
            this.VehicleSrv.update(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Vehicle getById(@PathParam("id") Long id) throws Exception {
        try {
            return this.VehicleSrv.getById(id);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/Count")
    public long getCount() throws Exception {
        try {
            return this.VehicleSrv.getCount();
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/")
    public List<Vehicle> getAll() throws Exception {
        try {
            return this.VehicleSrv.getAll();
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/{begin}/{count}")
    public List<Vehicle> getAll(@PathParam("begin") int begin, @PathParam("count") int count) throws Exception {
        try {
            return this.VehicleSrv.getAll(begin, count);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }
      
    
}