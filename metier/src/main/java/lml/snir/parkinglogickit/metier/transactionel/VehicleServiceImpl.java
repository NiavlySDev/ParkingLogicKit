package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Vehicle;
import lml.snir.parkinglogickit.physique.data.PhysiqueDataFactory;

import lml.snir.parkinglogickit.physique.data.VehicleDataService;
/**
 *
 * @author Phily Seck
 */
public final class VehicleServiceImpl implements VehicleService {

    private final VehicleDataService VehicleDataSrv;

   public VehicleServiceImpl() throws Exception {
      this.VehicleDataSrv = PhysiqueDataFactory.getVehicleDataService();
      
   }
    @Override
    public Vehicle getByContent(String contenu) throws Exception {
     return this.VehicleDataSrv.getByContent(contenu);
    }

    @Override
    public Vehicle add(Vehicle t) throws Exception {
      return this.VehicleDataSrv.add(t);
    }

    @Override
    public void remove(Vehicle t) throws Exception {
    this.VehicleDataSrv.remove(t);
    }

    @Override
    public void update(Vehicle t) throws Exception {
 this.VehicleDataSrv.update(t);
    }

    @Override
    public Vehicle getById(Long id) throws Exception {
       return this.VehicleDataSrv.getById(id);
    }

    @Override
    public long getCount() throws Exception {
      return this.VehicleDataSrv.getCount();
    }

    @Override
    public List<Vehicle> getAll() throws Exception {
      return this.VehicleDataSrv.getAll();
    }

    @Override
    public List<Vehicle> getAll(int begin, int count) throws Exception {
       return this.VehicleDataSrv.getAll(begin, count);
  
    }

    @Override
    public List<Vehicle> getByAssociate(boolean attribue) throws Exception {
      return this.VehicleDataSrv.getByAssociate(attribue);
    }
}
