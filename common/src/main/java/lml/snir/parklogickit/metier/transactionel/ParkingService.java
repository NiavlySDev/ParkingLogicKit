package lml.snir.parklogickit.metier.transactionel;

import lml.snir.parklogickit.metier.entity.Parking;
import lml.snir.persistence.CrudService;

/**
 *
 * @author phily
 */
public interface ParkingService extends CrudService<Parking> {

    public Parking getById(long id) throws Exception;

    public Parking getByUsername(String contenu) throws Exception;

    public Parking getByCountPlace(int contenu) throws Exception;

    public Parking getByIsFull(boolean attribue) throws Exception;

}
