package lml.snir.rest.rest.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * JsonSerializer and JsonDeserializer with add / extract class name used for
 * polymorphic support
 *
 * @author (c) Stéphane ALONSO 2017
 */
public class GSonAdapter implements JsonSerializer, JsonDeserializer {

    @Override
    public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssz").create();
        JsonElement serialize = gson.toJsonTree(src);
        JsonObject obj = (JsonObject) serialize;
        Iterator<Entry<String, JsonElement>> it = obj.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, JsonElement> entry = it.next();
            JsonElement o = entry.getValue();
            if (o instanceof JsonObject) {
                obj = o.getAsJsonObject();
                // recup attribut de src qui a le nom entry.getKey();
                String attributeName = entry.getKey();
                try {
                    Object attr = null;
                    List<Field> fields = this.getFields(src.getClass()); //src.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if (field.getName().equals(attributeName)) {
                            field.setAccessible(true);
                            attr = field.get(src);
                            break;
                        }
                    }
                    if (attr != null) {
                        Class clazz = attr.getClass();
                        obj.addProperty("class", clazz.getName());
                    } else {
                        System.err.println("attr is null : " + attributeName);
                    }

                } catch (Exception ex) {
                    Logger.getLogger(GSonAdapter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        JsonObject o = (JsonObject) serialize;
        o.addProperty("class", src.getClass().getName());
        return serialize;
    }

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Class actualClass;
        if (json.isJsonObject()) {
            JsonObject jsonObject = json.getAsJsonObject();
            String className = jsonObject.get("class").getAsString();
            try {
                actualClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e.getMessage());
            }
        } else {
            actualClass = typeOfT.getClass();
        }

        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Object ret = gson.fromJson(json, actualClass);
        return ret;
    }

    private List<Field> getFields(Class clazz) {
        List<Field> fields = new ArrayList<>();

        while (clazz != Object.class) {
            for (Field f : clazz.getDeclaredFields()) {
                fields.add(f);
            }
            clazz = clazz.getSuperclass();
        }

        return fields;
    }
}
