package lml.snir.parklogickit.metier.transactionel;

import lml.snir.parklogickit.metier.entity.Event;
import lml.snir.persistence.CrudService;

/**
 *
 * @author phily
 */
public interface EventService extends CrudService<Event> {

    public Event getById(long id) throws Exception;

    public Event getByIsEntered(boolean attribue) throws Exception;

}
