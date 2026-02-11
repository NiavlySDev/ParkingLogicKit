package lml.snir.parkinglogickit.metier.transactionel;

import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.persistence.CrudService;

/**
 *
 * @author Phily Seck
 */
public interface DriverService extends CrudService<Driver> {

    public Driver getById(long id) throws Exception;

    public Driver getByUsername(String contenu) throws Exception;

    public Driver getByFirstname(String contenu) throws Exception;

    public Driver getBySurname(String contenu) throws Exception;

    public Driver getByCountPlace(int contenu) throws Exception;

    public Driver getByIsMale(boolean attribue) throws Exception;

    public Driver getByPassword(String contenu) throws Exception;

    public Driver getById(Driver driver);

}
