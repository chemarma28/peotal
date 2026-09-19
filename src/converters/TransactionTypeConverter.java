package converters;

import static java.lang.String.format;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import io.jooby.value.ConversionHint;
import io.jooby.value.Converter;
import io.jooby.value.Value;
import transactions.TransactionType;

public class TransactionTypeConverter implements Converter, JsonDeserializer<TransactionType> {

    @Override
    public Object convert(Type type, Value value, ConversionHint hint) {
        if (type == TransactionType.class) {
            fromString(value.name(), value.value());
        }

        return null;
    }

    @Override
    public TransactionType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return fromString(null, json.getAsString());
    }

    private TransactionType fromString(String name, String val) {
        try {
            return TransactionType.valueOf(val);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    format("Expecting '%s' as value one of %s. Got: '%s'",
                            Optional.ofNullable(name).orElse("<unknown property>"),
                            Arrays.toString(TransactionType.values()),
                            val
                    )
            );
        }
    }
}
