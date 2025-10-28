package finance.web.forms;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import jakarta.servlet.http.HttpServletRequest;

public class Form {
    private final Map<String, FormField<?>> fields = new HashMap<>();
    private final Map<String, String> rawValues = new HashMap<>();
    private final Map<String, Result> validations = new HashMap<>();

    public static enum Result {
        EMPTY,
        FAIL,
        SUCCESS
    };

    public Form(final HttpServletRequest req, final FormField<?>... fields) {
        Arrays.stream(fields).forEach(field -> {
            final String name = field.name();

            this.fields.put(name, field);
            this.validations.put(name, Result.EMPTY);

            final String value = req.getParameter(name);

            if (null != value) {
                rawValues.put(name, value);

                boolean validation = field.rule().question(value);

                if (validation)
                    validations.put(name, Result.SUCCESS);
                else
                    validations.put(name, Result.FAIL);
            }
        });
    }

    public boolean failedQ() {
        return 0 < validations.entrySet().stream().filter(e -> Result.FAIL == e.getValue()).count();
    }

    public Result validation(String field) {
        return validations.getOrDefault(field, Result.EMPTY);
    }

    public List<String> whyFailed(String field) {
        if (!fields.containsKey(field))
            return Collections.emptyList();

        return fields.get(field).rule().why();
    }

    public String rawValue(String field) {
        if (!fields.containsKey(field))
            throw new NoSuchElementException("no field '" + field + "' declaration");

        return rawValues.get(field);
    }

    @SuppressWarnings("unchecked")
    public <R> R value(String name, Class<? extends R> type) {
        if (!validations.containsKey(name))
            throw new NoSuchElementException(
                    "no suitable rule for field '" + name + "' to cast it to '" + type.getName() + "'");

        final Result validation = validations.get(name);

        if (Result.FAIL == validation)
            throw new IllegalArgumentException("field '" + name + "' is not valid: " + fields.get(name).rule().why());

        return (R) fields.get(name).rule().produce(rawValues.get(name));
    }
}
