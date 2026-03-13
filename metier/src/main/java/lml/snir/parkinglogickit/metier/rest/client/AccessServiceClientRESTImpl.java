package lml.snir.parkinglogickit.metier.rest.client;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Access;
import lml.snir.parkinglogickit.metier.transactionel.AccessService;
import lml.snir.rest.client.ClientRest;

/**
 *
 * @author Phily Seck
 */
public class AccessServiceClientRESTImpl extends ClientRest<Access> implements AccessService {

    public AccessServiceClientRESTImpl() {
        super.init("AccessService", new RestServerLocalConfiguration());
    }

    @Override
    public Access add(Access t) throws Exception {
        super.setPath("");
        return super.addEntity(t);
    }

    @Override
    public void remove(Access t) throws Exception {
        super.setPath("");
        super.removeEntity(t);
    }

    @Override
    public void update(Access t) throws Exception {
        super.setPath("");
        super.updateEntity(t);
    }

    @Override
    public Access getById(Long id) throws Exception {
        super.setPath("" + id);
        return super.getEntity();
    }

    @Override
    public long getCount() throws Exception {
        super.setPath("Count");
        return super.getCountEntity();
    }

    @Override
    public List<Access> getAll() throws Exception {
        super.setPath("");
        return super.getEntitys();
    }

    @Override
    public List<Access> getAll(int begin, int count) throws Exception {
        super.setPath("" + begin + "/" + count);
        return super.getEntitys();
    }

    @Override
    public Access getByDriver(String contenu) throws Exception {
        super.setPath("getByDriver/" + contenu);
        return super.getEntity();
    }

    @Override
    public Access getByDateTime(String contenu) throws Exception {
        super.setPath("getByDateTime/" + contenu);
        return super.getEntity();
    }

    @Override
    public Access getByContenu(String contenu) throws Exception {
        super.setPath("getByContenu/" + contenu);
        return super.getEntity();
    }
    
     @Override
    public Access getByBadge(String contenu) throws Exception {
        super.setPath("getByBadge/" + contenu);
        return super.getEntity();
    }
}
