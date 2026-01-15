package lml.snir.javafx;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author (2021) S.ALONSO LML
 * @param <T>
 */
public abstract class LMLModel<T> {

    public abstract void setObjectModel(T t);

    public abstract T getAsObject();

    protected String[] getPropertys() {
        Class<? extends LMLModel> componentClass = getClass();
        List<String> lines = new ArrayList<>();
        Field[] fields = componentClass.getDeclaredFields();
        for (Field field : fields) {            
            lines.add(field.getName());
        }

        return lines.toArray(new String[lines.size()]);
    }
}
