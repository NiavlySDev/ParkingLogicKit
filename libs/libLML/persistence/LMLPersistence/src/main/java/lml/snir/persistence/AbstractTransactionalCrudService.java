package lml.snir.persistence;

import java.util.List;

/**
 *
 * @author (c) Stéphane ALONSO 2020
 * @param <T>
 */
public abstract class AbstractTransactionalCrudService<T> implements CrudService<T> {
    protected final CrudService<T> dataService;
    
    protected AbstractTransactionalCrudService(CrudService<T> dataService) {
        this.dataService = dataService;
    }
    
    @Override
    public T getById(Long id) throws Exception {
        return this.dataService.getById(id);
    }

    @Override
    public long getCount() throws Exception {
        return this.dataService.getCount();
    }

    @Override
    public List<T> getAll() throws Exception {
        return this.dataService.getAll();
    }

    @Override
    public List<T> getAll(int begin, int count) throws Exception {
        return this.dataService.getAll(begin, count);
    }
    
}
