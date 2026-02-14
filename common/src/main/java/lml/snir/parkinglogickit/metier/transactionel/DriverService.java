package lml.snir.parkinglogickit.metier.transactionel;

import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.persistence.CrudService;

/**
 *
 * @author Phily Seck
 */
public interface DriverService extends CrudService<Driver> {

    public Driver getByUsername(String contenu) throws Exception;

}
