package lml.snir.parkinglogickit.metier.rest.client;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Associate;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.metier.transactionel.AssociateService;
import lml.snir.rest.client.ClientRest;

/**
 *
 * @author Phily Seck
 */
public class AssociateServiceClientRESTImpl extends ClientRest<Associate> implements AssociateService {

    public AssociateServiceClientRESTImpl() {
        super.init("AssociateService", new RestServerLocalConfiguration());
    }




    @Override
    public void remove(Associate t) throws Exception {
        super.setPath("");
        super.removeEntity(t);
    }

    @Override
    public void update(Associate t) throws Exception {
        super.setPath("");
        super.updateEntity(t);
    }

    @Override
    public Associate getById(Long id) throws Exception {
        super.setPath("" + id);
        return super.getEntity();
    }

    @Override
    public long getCount() throws Exception {
        super.setPath("getCount");
        return super.getCountEntity();
    }

    @Override
    public List<Associate> getAll() throws Exception {
        super.setPath("getAll");
        return super.getEntitys();
    }

    @Override
    public List<Associate> getAll(int begin, int count) throws Exception {
        super.setPath("" + begin + "/" + count);
        return super.getEntitys();
    }

    @Override
    public Associate getByBadge(Badge badge) throws Exception {
          super.setPath("getByBadge");
        return super.getEntity();
    }

    @Override
    public Associate getByUtilisateur(Driver drv) throws Exception {
         super.setPath("getByUtilisateur");
        return super.getEntity();
    }

    @Override
    public Associate add(Associate t) throws Exception {
          super.setPath("add");
        return super.getEntity();
    }

}