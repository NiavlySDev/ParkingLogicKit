package lml.snir.parkinglogickit.metier.transactionel;

import lml.snir.parkinglogickit.metier.entity.Parking;
import lml.snir.persistence.CrudService;

/**
 *
 * @author Phily Seck
 */
public interface ParkingService extends CrudService<Parking> {

    public Parking getById(long id) throws Exception;

    public Parking getByUsername(String contenu) throws Exception;

    public Parking getByCountPlace(int contenu) throws Exception;

    public Parking getByIsFull(boolean attribue) throws Exception;

}
