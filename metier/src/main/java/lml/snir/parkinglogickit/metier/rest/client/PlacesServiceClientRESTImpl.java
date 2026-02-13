package lml.snir.parkinglogickit.metier.rest.client;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Places;
import lml.snir.parkinglogickit.metier.transactionel.PlacesService;
import lml.snir.rest.client.ClientRest;

/**
 *
 * @author Phily Seck
 */
public class PlacesServiceClientRESTImpl extends ClientRest<Places> implements PlacesService {

    public PlacesServiceClientRESTImpl() {
        super.init("PlacesService", new RestServerLocalConfiguration());
    }

    @Override
    public Places getByIsOccuped(boolean attribue) throws Exception {
        super.setPath("getByIsOccuped");
        return super.getEntity();
    }

    @Override
    public Places add(Places t) throws Exception {
        super.setPath("add");
        return super.getEntity();
    }

    @Override
    public void remove(Places t) throws Exception {
        super.setPath("remove");
    }

    @Override
    public void update(Places t) throws Exception {
        super.setPath("update");
    }

    @Override
    public Places getById(Long id) throws Exception {
        super.setPath("getById");
        return super.getEntity();
    }

    @Override
    public long getCount() throws Exception {
        super.setPath("getCount");
        return 0;

    }

    @Override
    public List<Places> getAll() throws Exception {
        super.setPath("getAll");
        return null;
    }

    @Override
    public List<Places> getAll(int begin, int count) throws Exception {
        super.setPath("" + begin + "/" + count);
        return super.getEntitys();
    }
}
