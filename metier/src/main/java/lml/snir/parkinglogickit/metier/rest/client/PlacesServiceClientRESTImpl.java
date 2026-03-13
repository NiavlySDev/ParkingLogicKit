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
        super.setPath("getByIsOccuped/" + attribue);
        return super.getEntity();
    }

    @Override
    public Places add(Places t) throws Exception {
        super.setPath("");
        return super.addEntity(t);
    }

    @Override
    public void remove(Places t) throws Exception {
        super.setPath("");
        super.removeEntity(t);
    }

    @Override
    public void update(Places t) throws Exception {
        super.setPath("");
        super.updateEntity(t);
    }

    @Override
    public Places getById(Long id) throws Exception {
         super.setPath("" + id);
        return super.getEntity();
    }

    @Override
    public long getCount() throws Exception {
        super.setPath("getCount");
          return super.getCountEntity();
    }

    @Override
    public List<Places> getAll() throws Exception {
      super.setPath("");
        return super.getEntitys();
    }

    @Override
    public List<Places> getAll(int begin, int count) throws Exception {
        super.setPath("" + begin + "/" + count);
        return super.getEntitys();
    }
}
