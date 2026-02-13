package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Vehicle;

//import lml.snir.parkinglogickit.data.PlacesDataService;
/**
 *
 * @author Phily Seck
 */
public final class VehicleServiceImpl implements VehicleService {

//    private final VehicleDataService PlacesDataSrv;
//
//    public VehicleServiceImpl() throws Exception {
//       this.VehicleDataSrv = PhysiqueDataFactory.getVehicleDataService();
//    }
    @Override
    public Vehicle getByContent(String contenu) throws Exception {
        return null;
//      return this.VehicleDataSrv.getByContent();
    }

    @Override
    public Vehicle add(Vehicle t) throws Exception {
        return null;
//      return this.VehicleDataSrv.add();
    }

    @Override
    public void remove(Vehicle t) throws Exception {

//      return this.VehicleDataSrv.remove();
    }

    @Override
    public void update(Vehicle t) throws Exception {

//      return this.VehicleDataSrv.update();
    }

    @Override
    public Vehicle getById(Long id) throws Exception {
        return null;
//      return this.VehicleDataSrv.getById();
    }

    @Override
    public long getCount() throws Exception {
        return 0;
//      return this.VehicleDataSrv.getCount();
    }

    @Override
    public List<Vehicle> getAll() throws Exception {
        return null;
//      return this.VehicleDataSrv.getAll();
    }

    @Override
    public List<Vehicle> getAll(int begin, int count) throws Exception {
        return null;
//      return this.VehicleDataSrv.getAll();
    }

    @Override
    public List<Vehicle> getByAssociate(boolean attribue) throws Exception {
        return null;
//      return this.VehicleDataSrv.getByAssociate();
    }
}
