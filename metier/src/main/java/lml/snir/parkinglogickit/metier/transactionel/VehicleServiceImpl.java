package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Parking;
import lml.snir.parkinglogickit.metier.entity.Vehicle;

//import lml.snir.parkinglogickit.data.PlacesDataService;


/**
 *
 * @author phily
 */
public final class VehicleServiceImpl implements VehicleService {

//    private final PlacesDataService PlacesDataSrv;
//
//    public PlacesServiceImpl() throws Exception {
//       this.PlacesDataSrv = PhysiqueDataFactory.getPlacesDataService();
//    }


    @Override
    public Vehicle getByContent(String contenu) throws Exception {
       return null;
//      return this.prkDataSrv.getByContent();
    }

    @Override
    public Vehicle add(Vehicle t) throws Exception {
        return null;
//      return this.prkDataSrv.add();
    }

    @Override
    public void remove(Vehicle t) throws Exception {
      
//      return this.prkDataSrv.remove();
    }

    @Override
    public void update(Vehicle t) throws Exception {
        
//      return this.prkDataSrv.update();
    }

    @Override
    public Vehicle getById(Long id) throws Exception {
       return null;
//      return this.prkDataSrv.getById();
    }
    @Override
    public long getCount() throws Exception {
        return 0;
//      return this.prkDataSrv.getCount();
    }

    @Override
    public List<Vehicle> getAll() throws Exception {
           return null;
//      return this.prkDataSrv.getAll();
    }

    @Override
    public List<Vehicle> getAll(int begin, int count) throws Exception {
         return null;
//      return this.prkDataSrv.getAll();
    }

    @Override
    public List<Vehicle> getByAssociate(boolean attribue) throws Exception {
      return null;
//      return this.prkDataSrv.getByAssociate();
    }
}