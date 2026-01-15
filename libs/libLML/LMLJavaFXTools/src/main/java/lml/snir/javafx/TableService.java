package lml.snir.javafx;

import java.util.List;

/**
 *
 * @author fanou
 * @param <T>
 */
public interface TableService<T> {
    public long getCount() throws Exception;
    public List<T> getAll() throws Exception;
    public List<T> getAll(int begin, int count) throws Exception;
}
