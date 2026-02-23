package finance.forms;

import static java.util.Collections.emptyList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ValidationResult {
    private final int DEFAULT_KEY = -1;
    private Map<Object, Set<ValidationMessage>> messages = new HashMap<>();

    public ValidationResult addMessage(final String message) {
        messages.putIfAbsent(DEFAULT_KEY, new HashSet<>());
        messages.get(DEFAULT_KEY).add(ValidationMessage.success(message));

        return this;
    }

    public ValidationResult addMessage(final Object key, final String message) {
        messages.putIfAbsent(key, new HashSet<>());
        messages.get(key).add(ValidationMessage.success(message));

        return this;
    }

    public ValidationResult addError(final String message) {
        messages.putIfAbsent(DEFAULT_KEY, new HashSet<>());
        messages.get(DEFAULT_KEY).add(ValidationMessage.error(message));

        return this;
    }

    public ValidationResult addError(Object key, final String message) {
        messages.putIfAbsent(key, new HashSet<>());
        messages.get(key).add(ValidationMessage.error(message));

        return this;
    }

    public boolean hasErrors(Object key) {

        return false;
    }

    public boolean hasError() {
        return false;
    }

    public List<ValidationMessage> getErrors() {
        return emptyList();
    }
}
