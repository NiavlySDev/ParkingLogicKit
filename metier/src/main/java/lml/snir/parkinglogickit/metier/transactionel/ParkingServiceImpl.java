package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;

import lml.snir.parkinglogickit.metier.entity.Parking;
//import lml.snir.parklogickit.data.ParkingDataService;

/**
 *
 * @author Phily Seck
 */
public final class ParkingServiceImpl implements ParkingService {
//    private final ParkingDataService prkDataSrv;
//    
//    public ParkingServiceImpl() throws Exception {
//        this.prkDataSrv = PhysiqueDataFactory.getParkingDataService();
//    }

    @Override
    public Parking getById(long id) throws Exception {
        return null;
//       return this.prkDataSrv.getById(id);
    }

    @Override
    public Parking getByIsFull(boolean attribue) throws Exception {
        return null;
//      return this.prkDataSrv.getByIsFull();
    }

    @Override
    public Parking getByUsername(String contenu) throws Exception {
        return null;
//      return this.prkDataSrv.getByUsername();
    }

    @Override
    public Parking add(Parking t) throws Exception {
        return null;
//      return this.prkDataSrv.add();
    }

    @Override
    public void remove(Parking t) throws Exception {
//      return this.prkDataSrv.remove();

    }

    @Override
    public void update(Parking t) throws Exception {
//      return this.prkDataSrv.update();

    }

    @Override
    public Parking getById(Long id) throws Exception {
        return null;
//      return this.prkDataSrv.getById();
    }

    @Override
    public long getCount() throws Exception {
           return 0;
//      return this.prkDataSrv.getCount();
    }

    @Override
    public List<Parking> getAll() throws Exception {
        return null;
//      return this.prkDataSrv.getAll();
    }

    @Override
    public List<Parking> getAll(int begin, int count) throws Exception {
        return null;
//      return this.prkDataSrv.getAll(int begin, int count);
    }

}
