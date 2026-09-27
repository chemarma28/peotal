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
import transactions.Currency;

public class CurrencyConverter implements Converter, JsonDeserializer<Currency> {

    @Override
    public Object convert(Type type, Value value, ConversionHint hint) {
        if (type == Currency.class) {
            return fromString(value.name(), value.value());
        }

        return null;
    }

    @Override
    public Currency deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return fromString(null, json.getAsString());
    }

    private Currency fromString(String name, String val) {
        try {
            return Currency.valueOf(val);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    format("Expecting '%s' as value one of %s. Got: '%s'",
                            Optional.ofNullable(name).orElse("<unknown property>"),
                            Arrays.toString(Currency.values()),
                            val
                    )
            );
        }
    }
}
