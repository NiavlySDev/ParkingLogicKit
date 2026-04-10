package lml.snir.parkinglogickit.metier.transactionel;

import lml.snir.parkinglogickit.metier.entity.Badge;
import lml.snir.persistence.CrudService;

/**
 *
 * @author Phily Seck
 */
public interface BadgeService extends CrudService<Badge> {

    public Badge getByContent(String content) throws Exception;
}
