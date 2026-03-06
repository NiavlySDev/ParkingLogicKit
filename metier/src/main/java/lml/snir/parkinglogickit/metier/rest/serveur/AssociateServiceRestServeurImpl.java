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
import lml.snir.parkinglogickit.metier.entity.Associate;
import lml.snir.parkinglogickit.metier.transactionel.AssociateService;
import lml.snir.rest.server.RestException;

/**
 *
 * @author Phily Seck
 */
@Path("/AssociateService")
@Consumes("application/json")
@Produces("application/json")
public class AssociateServiceRestServeurImpl {

    private final AssociateService AssociateSrv;

    public AssociateServiceRestServeurImpl() throws Exception {
        this.AssociateSrv = MetierFactory.getAssociateService();
    }

    @POST
    @Path("/")  
    public Associate add(Associate t) throws Exception {
        try {
            return this.AssociateSrv.add(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @DELETE
    @Path("/")
    public void remove(Associate t) throws Exception {
        try {
            this.AssociateSrv.remove(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @PUT
    @Path("/")
    public void update(Associate t) throws Exception {
        try {
            this.AssociateSrv.update(t);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Associate getById(@PathParam("id") Long id) throws Exception {
        try {
            return this.AssociateSrv.getById(id);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/Count")
    public long getCount() throws Exception {
        try {
            return this.AssociateSrv.getCount();
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/")
    public List<Associate> getAll() throws Exception {
        try {
            return this.AssociateSrv.getAll();
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    @GET
    @Path("/{begin}/{count}")
    public List<Associate> getAll(@PathParam("begin") int begin, @PathParam("count") int count) throws Exception {
        try {
            return this.AssociateSrv.getAll(begin, count);
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

   
    }


