package lml.snir.parklogickit.metier.transactionel;

import java.util.List;


import lml.snir.parklogickit.metier.entity.Parking;
import lml.snir.parklogickit.data.ParkingDataService;


/**
 *
 * @author fanou
 */
public final class ParkingServiceImpl implements ParkingService {
    private final ParkingDataService prkDataSrv;
    
    public ParkingServiceImpl() throws Exception {
        this.prkDataSrv = PhysiqueDataFactory.getParkingDataService();
    }

    @Override
    public Parking getById(long id) throws Exception {
       return this.prkDataSrv.getById(id);
    }

   
     
    @Override
    public Parking getByIsFull(boolean attribue) throws Exception {
      return this.prkDataSrv.getByIsFull();
    }

    @Override
    public Parking getByCountPlace(int contenu) throws Exception {
      return this.prkDataSrv.getByCountPlace();
    }


    
  

    
}
