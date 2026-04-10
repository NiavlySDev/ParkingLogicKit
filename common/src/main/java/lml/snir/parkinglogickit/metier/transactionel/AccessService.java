package lml.snir.parkinglogickit.metier.transactionel;

import lml.snir.parkinglogickit.metier.entity.Access;
import lml.snir.persistence.CrudService;

/**
 *
 * @author Phily Seck
 */
public interface AccessService extends CrudService<Access> {

    public Access getByDriver(String driver) throws Exception;

    public Access getByContenu(String contenu) throws Exception;

    public Access getByDateTime(String date) throws Exception;

    public Access getByBadge(String date) throws Exception;

}
