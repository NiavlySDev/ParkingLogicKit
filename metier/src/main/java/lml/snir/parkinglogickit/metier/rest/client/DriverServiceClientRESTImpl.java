package lml.snir.parkinglogickit.metier.rest.client;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.metier.transactionel.DriverService;
import lml.snir.rest.client.ClientRest;

/**
 *
 * @author Phily Seck
 */
public class DriverServiceClientRESTImpl extends ClientRest<Driver> implements DriverService {

    public DriverServiceClientRESTImpl() {
        super.init("DriverService", new RestServerLocalConfiguration());
    }

    @Override
    public Driver getByUsername(String login) throws Exception {
        super.setPath("getByUsername/" + login);
        return super.getEntity();
    }

    @Override
    public Driver add(Driver t) throws Exception {
        super.setPath("");
        return super.addEntity(t);
    }

    @Override
    public void remove(Driver t) throws Exception {
        super.setPath("");
        super.removeEntity(t);
    }

    @Override
    public void update(Driver t) throws Exception {
        super.setPath("");
        super.updateEntity(t);
    }

    @Override
    public Driver getById(Long id) throws Exception {
        super.setPath("" + id);
        return super.getEntity();
    }

    @Override
    public long getCount() throws Exception {
        super.setPath("Count");
        return super.getCountEntity();
    }

    @Override
    public List<Driver> getAll() throws Exception {
        super.setPath("");
        return super.getEntitys();
    }

    @Override
    public List<Driver> getAll(int begin, int count) throws Exception {
        super.setPath("" + begin + "/" + count);
        return super.getEntitys();
    }
}
