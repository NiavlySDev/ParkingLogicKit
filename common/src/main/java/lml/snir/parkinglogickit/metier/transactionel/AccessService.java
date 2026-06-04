package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Access;
import lml.snir.persistence.CrudService;

/**
 *
 * @author Virgile Alari
 */
public interface AccessService extends CrudService<Access> {

    public List<Access> getByDriver(Long driver) throws Exception;
    public List<Access> getByDate(String date) throws Exception;
    public List<Access> getByIsOpen(boolean attribue) throws Exception;
}
