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
import lml.snir.parkinglogickit.metier.entity.Maintenance;
import lml.snir.parkinglogickit.metier.transactionel.MaintenanceService;
import lml.snir.rest.server.RestException;

/**
 *
 * @author fanou
 */
@Path("/MaintenanceService")
@Consumes("application/json")
@Produces("application/json")
public class MaintenanceServiceRestServeurImpl {
    private final MaintenanceService MaintenanceSrv;

    public MaintenanceServiceRestServeurImpl() throws Exception {
        this.MaintenanceSrv = MetierFactory.getMaintenanceService();
    }

    @POST
    @Path("/")
    public Maintenance add(Maintenance t) throws Exception {
        try {
            return this.MaintenanceSrv.add(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @DELETE
    @Path("/")
    public void remove(Maintenance t) throws Exception {
        try {
            this.MaintenanceSrv.remove(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @PUT
    @Path("/")
    public void update(Maintenance t) throws Exception {
        try {
            this.MaintenanceSrv.update(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Maintenance getById(@PathParam("id") Long id) throws Exception {
        try {
            return this.MaintenanceSrv.getById(id);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/Count")
    public long getCount() throws Exception {
        try {
            return this.MaintenanceSrv.getCount();
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/")
    public List<Maintenance> getAll() throws Exception {
        try {
            return this.MaintenanceSrv.getAll();
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/{begin}/{count}")
    public List<Maintenance> getAll(@PathParam("begin") int begin, @PathParam("count") int count) throws Exception {
        try {
            return this.MaintenanceSrv.getAll(begin, count);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

}
