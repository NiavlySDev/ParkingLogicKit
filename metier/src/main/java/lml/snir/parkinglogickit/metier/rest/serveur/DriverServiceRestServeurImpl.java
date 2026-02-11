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
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.metier.transactionel.DriverService;
import lml.snir.rest.server.RestException;

/**
 *
 * @author phily
 */
@Path("/DriverService")
@Consumes("application/json")
@Produces("application/json")
public class DriverServiceRestServeurImpl {
    private final DriverService DriverSrv;

    public DriverServiceRestServeurImpl() throws Exception {
        this.DriverSrv = MetierFactory.getDriverService();
    }

    @POST
    @Path("/")
    public Driver add(Driver t) throws Exception {
        try {
            return this.DriverSrv.add(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @DELETE
    @Path("/")
    public void remove(Driver t) throws Exception {
        try {
            this.DriverSrv.remove(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @PUT
    @Path("/")
    public void update(Driver t) throws Exception {
        try {
            this.DriverSrv.update(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Driver getById(@PathParam("id") Long id) throws Exception {
        try {
            return this.DriverSrv.getById(id);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/Count")
    public long getCount() throws Exception {
        try {
            return this.DriverSrv.getCount();
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/")
    public List<Driver> getAll() throws Exception {
        try {
            return this.DriverSrv.getAll();
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/{begin}/{count}")
    public List<Driver> getAll(@PathParam("begin") int begin, @PathParam("count") int count) throws Exception {
        try {
            return this.DriverSrv.getAll(begin, count);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }
    
    @GET
    @Path("/getByUsername/{username}")
    public Driver getByusername(@PathParam("username") String username) throws Exception {
        try {
            return this.DriverSrv.getByUsername(username);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

}
