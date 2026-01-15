package lml.snir.rest.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public JsonElement serialize(Date date,
                                 Type type,
                                 JsonSerializationContext jsonSerializationContext) {

        String strDate = date.toString();
        strDate = "\"" + strDate + "\"";
        return new JsonPrimitive(strDate); // 2024-11-15T11:56:26CET

    }

    @Override
    public Date deserialize(JsonElement jsonElement,
                                 Type type,
                                 JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        String strDate = jsonElement.toString();
        strDate = strDate.substring(1, strDate.length()-4);

        Date d = null;
        try {
            d = this.dateFormat.parse(strDate);
        } catch (Exception ex) {
            throw new JsonParseException(ex.getMessage());
        }
        return d;
    }

}