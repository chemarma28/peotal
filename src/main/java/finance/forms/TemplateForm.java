package finance.forms;

import static java.util.Collections.emptyList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateForm implements Form {
    private final Map<String, String> ordinary = new HashMap<>();

    public TemplateForm() {
    }

    public TemplateForm ordinary(String key, String value) {
        ordinary.put(key, value);

        return this;
    }

    @Override
    public String requestValue(String key) {
        return ordinary.get(key);
    }

    @Override
    public <T> T value(Object key, Class<? extends T> c) {
        return null;
    }

    @Override
    public boolean hasErrors(Object key) {
        return false;
    }

    @Override
    public boolean hasErrors() {
        return false;
    }

    @Override
    public List<String> errors(Object key) {
        return emptyList();
    }
}
