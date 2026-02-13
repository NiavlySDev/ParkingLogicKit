package lml.snir.parkinglogickit.metier.rest.client;

import java.util.List;

import lml.snir.parkinglogickit.metier.entity.Vehicle;
import lml.snir.parkinglogickit.metier.transactionel.VehicleService;
import lml.snir.rest.client.ClientRest;

/**
 *
 * @author Phily Seck
 */
public class VehicleServiceClientRESTImpl extends ClientRest<Vehicle> implements VehicleService {

    public VehicleServiceClientRESTImpl() {
        super.init("ParkingService", new RestServerLocalConfiguration());
    }

    @Override
    public Vehicle getByContent(String contenu) throws Exception {
        super.setPath("getByContent");
        return super.getEntity();
    }

    @Override
    public List<Vehicle> getByAssociate(boolean attribue) throws Exception {
        super.setPath("getByAssociate");
        return null;
    }

    @Override
    public Vehicle add(Vehicle t) throws Exception {
        super.setPath("add");
        return super.getEntity();
    }

    @Override
    public void remove(Vehicle t) throws Exception {
        super.setPath("remove");
    }

    @Override
    public void update(Vehicle t) throws Exception {
        super.setPath("update");

    }

    @Override
    public Vehicle getById(Long id) throws Exception {
        super.setPath("getById");
        return super.getEntity();
    }

    @Override
    public long getCount() throws Exception {
        super.setPath("getCount");
        return 0;

    }

    @Override
    public List<Vehicle> getAll() throws Exception {
        super.setPath("add");
        return null;

    }

    @Override
    public List<Vehicle> getAll(int begin, int count) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
