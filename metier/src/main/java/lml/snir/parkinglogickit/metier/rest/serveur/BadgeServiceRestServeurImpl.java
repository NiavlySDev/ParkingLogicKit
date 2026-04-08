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
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.metier.transactionel.BadgeService;
import lml.snir.rest.server.RestException;

/**
 *
 * @author Phily Seck
 */
@Path("/BadgeService")
@Consumes("application/json")
@Produces("application/json")
public class BadgeServiceRestServeurImpl {

    private final BadgeService badgeSrv;

    public BadgeServiceRestServeurImpl() throws Exception {
        this.badgeSrv = MetierFactory.getBadgeService();
    }

    @POST
    @Path("/")
    public Badge add(Badge t) throws Exception {
        try {
            return this.badgeSrv.add(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @DELETE
    @Path("/")
    public void remove(Badge t) throws Exception {
        try {
            this.badgeSrv.remove(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @PUT
    @Path("/")
    public void update(Badge t) throws Exception {
        try {
            this.badgeSrv.update(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Badge getById(@Context UriInfo uriInfo, @PathParam("id") Long id) throws Exception {
        try {
              Authenticate.authenticate(uriInfo.getQueryParameters());
            return this.badgeSrv.getById(id);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/Count")
    public long getCount() throws Exception {
        try {
            return this.badgeSrv.getCount();
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/")
    public List<Badge> getAll() throws Exception {
        try {
            return this.badgeSrv.getAll();
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/{begin}/{count}")
    public List<Badge> getAll(@PathParam("begin") int begin, @PathParam("count") int count) throws Exception {
        try {
            return this.badgeSrv.getAll(begin, count);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/getByContent/{content}")
    public Badge getByContent(@Context UriInfo uriInfo,@PathParam("content") String content) throws Exception {
        try {
             Authenticate.authenticate(uriInfo.getQueryParameters());
            return this.badgeSrv.getByContent(content);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

}
