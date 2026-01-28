package lml.snir.parklogickit.metier.transactionel;

//import lml.snir.parklogickit.metier.entity.Associate;
import lml.snir.parklogickit.metier.entity.Badge;
import lml.snir.parklogickit.metier.entity.Driver;
import lml.snir.persistence.CrudService;

/**
 *
 * @author phily
 *  extends CrudService<Associate> 
 */
public interface AssociateService{
//    public Associate getByBadge(Badge badge) throws Exception;
//    public Associate getByUtilisateur(Driver drv) throws Exception;
    public Boolean isBadgeAttribue(Driver drv) throws Exception;
    public Boolean isBadgeAttribue(Badge badge) throws Exception;
}
