package lml.snir.parkinglogickit.metier.transactionel;

import java.util.List;
import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.persistence.CrudService;

/**
 *
 * @author Phily Seck
 */
public interface BadgeService extends CrudService<Badge> {
    public Badge getByContent(String content) throws Exception;
    public List<Badge> getByAssociate(boolean attribue) throws Exception;
}
