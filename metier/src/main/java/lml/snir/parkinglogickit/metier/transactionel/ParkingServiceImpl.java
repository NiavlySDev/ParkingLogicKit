package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;

import lml.snir.parkinglogickit.metier.entity.Parking;
import lml.snir.parkinglogickit.physique.data.ParkingDataService;
import lml.snir.parkinglogickit.physique.data.PhysiqueDataFactory;

/**
 *
 * @author Phily Seck
 */
public final class ParkingServiceImpl implements ParkingService {

    private final ParkingDataService prkDataSrv;

    public ParkingServiceImpl() throws Exception {
        this.prkDataSrv = PhysiqueDataFactory.getParkingDataService();
    }

    @Override
    public Parking getByIsFull(boolean attribue) throws Exception {
        return this.prkDataSrv.getByIsFull(attribue);
    }

    @Override
    public Parking getByPlaceCount(int placeCount) throws Exception {
        return this.prkDataSrv.getByPlaceCount(placeCount);
    }

    @Override
    public Parking getByTotalPlace(int totalPlace) throws Exception {
        return this.prkDataSrv.getByTotalPlace(totalPlace);
    }

    @Override
    public Parking add(Parking t) throws Exception {
        return this.prkDataSrv.add(t);
    }

    @Override
    public void remove(Parking t) throws Exception {
        this.prkDataSrv.remove(t);
    }

    @Override
    public void update(Parking t) throws Exception {
        this.prkDataSrv.update(t);
    }

    @Override
    public Parking getById(Long id) throws Exception {
        return this.prkDataSrv.getById(id);
    }

    @Override
    public long getCount() throws Exception {
        return this.prkDataSrv.getCount();
    }

    @Override
    public List<Parking> getAll() throws Exception {
        return this.prkDataSrv.getAll();
    }

    @Override
    public List<Parking> getAll(int begin, int count) throws Exception {
        return this.prkDataSrv.getAll(begin, count);
    }

}
