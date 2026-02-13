package lml.snir.parkinglogickit.metier.transactionel;

import lml.snir.parkinglogickit.metier.entity.Associate;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.persistence.CrudService;

/**
 *
 * @author Phily Seck
 */
public interface AssociateService extends CrudService<Associate>{
    public Associate getByBadge(Badge badge) throws Exception;
    public Associate getByUtilisateur(Driver drv) throws Exception;
    public Boolean isBadgeAttribue(Driver drv) throws Exception;
    public Boolean isBadgeAttribue(Badge badge) throws Exception;
}
