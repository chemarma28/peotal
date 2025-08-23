package finance.web.forms;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Form {
    private Map<String, List<String>> errors = new HashMap<>();
    private Map<String, String> lastValues = new HashMap<>();

    public boolean hasAnyErrors() {
        return errors.size() > 0;
    }

    public boolean hasError(String field) {
        return errors.containsKey(field) && errors.get(field) != null && !errors.get(field).isEmpty();
    }

    public List<String> errors(String field) {
        return errors.getOrDefault(field, Collections.emptyList());
    }

    public void addError(String field, String error) {
        List<String> history = errors.getOrDefault(field, new LinkedList<>());

        history.add(error);

        errors.put(field, history);
    }

    public void setLastValue(String field, String value) {
        lastValues.put(field, value);
    }

    public String lastValue(String field, String def) {
        return lastValues.getOrDefault(field, def);
    }

    public String lastValue(String field) {
        return lastValue(field, "");
    }
}
