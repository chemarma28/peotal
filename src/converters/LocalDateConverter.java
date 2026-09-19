package converters;

import static java.lang.String.format;
import java.lang.reflect.Type;
import java.time.DateTimeException;
import java.time.LocalDate;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import java.util.Optional;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import io.jooby.value.ConversionHint;
import io.jooby.value.Converter;
import io.jooby.value.Value;

public class LocalDateConverter implements Converter, JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    @Override
    public LocalDate convert(Type type, Value value, ConversionHint hint) {
        if (type == LocalDate.class) {
            return this.fromString(value.name(), value.value());
        }

        return null;
    }

    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return fromString(null, json.getAsString());
    }

    private LocalDate fromString(String name, String val) {
        try {
            return LocalDate.parse(val, ISO_LOCAL_DATE);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(format("Expecting '%s' as ISO-8601. Got: '%s'", Optional.ofNullable(name).orElse("<unknown property>"), val));
        }
    }
}
