package lml.snir.parkinglogickit.metier.transactionel;

import lml.snir.parkinglogickit.metier.entity.Access;
import lml.snir.persistence.CrudService;

/**
 *
 * @author Phily Seck
 */
public interface AccessService extends CrudService<Access> {

    public Access getById(long id) throws Exception;

    public Access getByDriver(String contenu) throws Exception;

    public Access getByBadge(String contenu) throws Exception;

    public Access getByDateTime(String contenu) throws Exception;

    public Access getByEvent(String contenu) throws Exception;
}
