package lml.snir.parkinglogickit.metier.transactionel;

import lml.snir.parkinglogickit.metier.entity.Parking;
import lml.snir.persistence.CrudService;

/**
 *
 * @author Phily Seck
 */
public interface ParkingService extends CrudService<Parking> {

    public Parking getByUsername(String username) throws Exception;

    public Parking getByIsFull(boolean isFull) throws Exception;

}
