package lml.snir.parkinglogickit.metier.transactionel;

import lml.snir.parkinglogickit.metier.entity.Places;
import lml.snir.persistence.CrudService;

/**
 *
 * @author Phily Seck
 */
public interface PlacesService extends CrudService<Places> {

    public Places getByIsOccuped(boolean attribue) throws Exception;

}
