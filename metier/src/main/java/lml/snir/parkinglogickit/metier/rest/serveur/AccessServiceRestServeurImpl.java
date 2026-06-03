package lml.snir.parkinglogickit.metier.rest.serveur;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import java.util.List;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;
import lml.snir.parkinglogickit.metier.entity.Access;
import lml.snir.parkinglogickit.metier.transactionel.AccessService;
import lml.snir.rest.server.RestException;

/**
 *
 * @author Phily Seck
 */
@Path("/AccessService")
@Consumes("application/json")
@Produces("application/json")
public class AccessServiceRestServeurImpl {

    private final AccessService AccessSrv;

    public AccessServiceRestServeurImpl() throws Exception {
        this.AccessSrv = MetierFactory.getAccessService();
    }

    @POST
    @Path("/")
    public Access add(Access t) throws Exception {
        try {
            return this.AccessSrv.add(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @DELETE
    @Path("/")
    public void remove(Access t) throws Exception {
        try {
            this.AccessSrv.remove(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @PUT
    @Path("/")
    public void update(Access t) throws Exception {
        try {
            this.AccessSrv.update(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Access getById(@Context UriInfo uriInfo, @PathParam("id") Long id) throws Exception {
        try {
              Authenticate.authenticate(uriInfo.getQueryParameters());
            return this.AccessSrv.getById(id);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/Count")
    public long getCount() throws Exception {
        try {
            return this.AccessSrv.getCount();
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/")
    public List<Access> getAll() throws Exception {
        try {
            return this.AccessSrv.getAll();
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/{begin}/{count}")
    public List<Access> getAll(@PathParam("begin") int begin, @PathParam("count") int count) throws Exception {
        try {
            return this.AccessSrv.getAll(begin, count);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }
    
    @GET
    @Path("/getByDriver/{Driver}")
    public List<Access> getByDriver(@Context UriInfo uriInfo,@PathParam("Driver") String Driver) throws Exception {
        try {
              Authenticate.authenticate(uriInfo.getQueryParameters());
            return this.AccessSrv.getByDriver(Driver);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }
    
    @GET
    @Path("/getByDate/{Date}")
    public List<Access> getByDate(@Context UriInfo uriInfo,@PathParam("Date") String Date) throws Exception {
        try {
              Authenticate.authenticate(uriInfo.getQueryParameters());
            return this.AccessSrv.getByDate(Date);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/getByIsOpen/{IsOpen}")
    public List<Access> getByIsOpen(@Context UriInfo uriInfo, @PathParam("IsOpen") boolean IsOpen) throws Exception {
        try {
            Authenticate.authenticate(uriInfo.getQueryParameters());
            return this.AccessSrv.getByIsOpen(IsOpen);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

}
