package lml.snir.parklogickit.metier.transactionel;

import lml.snir.parklogickit.metier.entity.Maintenance;
import lml.snir.persistence.CrudService;

/**
 *
 * @author phily
 */
public interface MaintenanceService extends CrudService<Maintenance> {

    public Maintenance getById(long id) throws Exception;

    public Maintenance getByUsername(String contenu) throws Exception;

    public Maintenance getByPassword(String contenu) throws Exception;

    public Maintenance getByIsAdmin(boolean attribue) throws Exception;

}
