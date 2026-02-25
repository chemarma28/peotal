package finance.forms;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

public class SimpleForm implements Form {
    private final Map<String, Mapping<?>> mappings = new HashMap<>();
    private final Map<Object, Object> results = new HashMap<>();
    protected ValidationResult validation;
    private HttpServletRequest req;

    public Object id() {
        return 1;
    }

    public SimpleForm field(final String key, final Mapping<?> mapping) {
        mappings.put(key, mapping);

        return this;
    }

    public void bindFromRequest(final HttpServletRequest req) {
        this.req = req;
        this.validation = new ValidationResult();

        for (String key : mappings.keySet()) {
            final Object result = mappings.get(key).map(req, key, validation);

            if (null == result)
                continue;

            results.put(key, result);
        }
    }

    @Override
    public String requestValue(final String parameter) {
        return req.getParameter(parameter);
    }

    @Override
    public <T> T value(Object key, Class<? extends T> cl) {
        checkBindFromRequestCall();

        return cl.cast(results.get(key));
    }

    @Override
    public boolean hasErrors(Object key) {
        checkBindFromRequestCall();

        return validation.hasErrors(key);
    }

    public boolean hasErrors() {
        checkBindFromRequestCall();

        return validation.hasErrors();
    }

    public boolean isValid() {
        checkBindFromRequestCall();

        return !validation.hasErrors();
    }

    private void checkBindFromRequestCall() {
        if (null == req)
            throw new IllegalAccessError("No http request is binded to this form");
    }
}
