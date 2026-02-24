package finance.forms;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

public class Form {
    private final Map<Object, Mapping<?>> mappings = new HashMap<>();
    private final Map<Object, Object> results = new HashMap<>();
    protected ValidationResult validation;

    public Object id() {
        return 1;
    }

    public Form field(final Object key, final Mapping<?> mapping) {
        mappings.put(key, mapping);

        return this;
    }

    public void bindFromRequest(final HttpServletRequest req) {
        this.validation = new ValidationResult();

        for (Object key : mappings.keySet()) {
            final Object result = mappings.get(key).map(req, key, validation);

            if (null == result)
                continue;

            results.put(key, result);
        }
    }

    public <T> T value(Object key, Class<? extends T> cl) {
        return cl.cast(results.get(key));
    }

    public boolean hasError(Object key) {
        if (null == validation)
            return true;

        return validation.hasErrors(key);
    }

    public boolean hasErrors() {
        if (null == validation)
            return true;

        return validation.hasErrors();
    }

    public boolean isValid() {
        if (null == validation)
            return false;

        return !validation.hasErrors();
    }
}
