package finance.forms;

import java.util.HashMap;
import java.util.Map;

public class TemplateForm implements Form {
    private final Map<String, String> key2Value = new HashMap<>();

    public TemplateForm() {}

    public void template(String key, String value) {
        key2Value.put(key, value);
    }

    @Override
    public String requestValue(String key) {
        return key2Value.get(key);
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
}
