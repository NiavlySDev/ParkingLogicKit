package lml.snir.parkinglogickit.metier.rest.client;

import java.util.List;

import lml.snir.parkinglogickit.metier.entity.Parking;
import lml.snir.parkinglogickit.metier.transactionel.ParkingService;
import lml.snir.rest.client.ClientRest;

/**
 *
 * @author Phily Seck
 */
public class ParkingServiceClientRESTImpl extends ClientRest<Parking> implements ParkingService {

    public ParkingServiceClientRESTImpl() {
        super.init("ParkingService", new RestServerLocalConfiguration());
    }

    @Override
    public Parking getById(long id) throws Exception {
        super.setPath("getByCountPLace");
        return super.getEntity();
    }

    @Override
    public Parking getByUsername(String contenu) throws Exception {
        super.setPath("getByUsername");
        return super.getEntity();
    }

    @Override
    public Parking getByIsFull(boolean attribue) throws Exception {
        super.setPath("getByIsFull");
        return super.getEntity();
    }

    @Override
    public Parking add(Parking t) throws Exception {
        super.setPath("");
        return super.getEntity();
    }

    @Override
    public void remove(Parking t) throws Exception {
        super.setPath("remove");

    }

    @Override
    public void update(Parking t) throws Exception {
        super.setPath("update");
    }

    @Override
    public Parking getById(Long id) throws Exception {
        super.setPath("getById");
        return super.getEntity();
    }

    @Override
    public long getCount() throws Exception {
        super.setPath("getByCount");
        return 0;
    }

    @Override
    public List<Parking> getAll() throws Exception {
        super.setPath("getAll");
        return null;
    }

    @Override
    public List<Parking> getAll(int begin, int count) throws Exception {
        super.setPath("" + begin + "/" + count);
        return super.getEntitys();
    }

}
