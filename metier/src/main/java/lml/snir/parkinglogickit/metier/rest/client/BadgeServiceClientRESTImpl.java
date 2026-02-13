package lml.snir.parkinglogickit.metier.rest.client;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.metier.transactionel.BadgeService;
import lml.snir.rest.client.ClientRest;

/**
 *
 * @author Phily Seck
 */
public class BadgeServiceClientRESTImpl extends ClientRest<Badge> implements BadgeService {

    public BadgeServiceClientRESTImpl() {
        super.init("BadgeService", new RestServerLocalConfiguration());
    }

    @Override
    public Badge getByContent(String content) throws Exception {
        super.setPath("getByContent/" + content);
        return super.getEntity();
    }

    @Override
    public Badge add(Badge t) throws Exception {
        super.setPath("");
        return super.addEntity(t);
    }

    @Override
    public void remove(Badge t) throws Exception {
        super.setPath("");
        super.removeEntity(t);
    }

    @Override
    public void update(Badge t) throws Exception {
        super.setPath("");
        super.updateEntity(t);
    }

    @Override
    public Badge getById(Long id) throws Exception {
        super.setPath("" + id);
        return super.getEntity();
    }

    @Override
    public long getCount() throws Exception {
        super.setPath("Count");
        return super.getCountEntity();
    }

    @Override
    public List<Badge> getAll() throws Exception {
        super.setPath("");
        return super.getEntitys();
    }

    @Override
    public List<Badge> getAll(int begin, int count) throws Exception {
        super.setPath("" + begin + "/" + count);
        return super.getEntitys();
    }

}
