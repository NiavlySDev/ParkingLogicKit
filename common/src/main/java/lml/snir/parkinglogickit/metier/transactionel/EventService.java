package lml.snir.parkinglogickit.metier.transactionel;

import lml.snir.parkinglogickit.metier.entity.Event;
import lml.snir.persistence.CrudService;

/**
 *
 * @author Phily Seck
 */
public interface EventService extends CrudService<Event> {

    public Event getById(long id) throws Exception;

}
