package lml.snir.persistence;

import java.util.List;

/**
 * Interface that contain all methods needs for CRUD operation
 * @author (c) Stéphane ALONSO 2017
 * @param <T> 
 */
public interface CrudService<T> {
    public T add(T t) throws Exception;
    public void remove(T t) throws Exception;
    public void update(T t) throws Exception;
    public T getById(Long id) throws Exception;
    public long getCount() throws Exception;
    public List<T> getAll() throws Exception;
    public List<T> getAll(int begin, int count) throws Exception;
}
