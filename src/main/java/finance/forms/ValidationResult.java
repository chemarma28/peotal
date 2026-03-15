package finance.forms;

import java.util.*;

import static java.util.Collections.emptyList;

public final class ValidationResult {
    private final int DEFAULT_KEY = -1;
    private final Map<Object, Set<ValidationMessage>> messages = new HashMap<>();

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
        this.addError(DEFAULT_KEY, message);

        return this;
    }

    public ValidationResult addError(Object key, final String message) {
        messages.putIfAbsent(key, new HashSet<>());
        messages.get(key).add(ValidationMessage.error(message));

        return this;
    }

    public boolean hasErrors(final Object key) {
        final Set<ValidationMessage> m = messages.get(key);

        if (null == m || m.isEmpty())
            return false;

        return m.stream().filter(Objects::nonNull).anyMatch(ValidationMessage::error);
    }

    public boolean hasErrors() {
        return messages.values().stream().flatMap(Collection::stream).filter(Objects::nonNull)
                .anyMatch(ValidationMessage::error);
    }

    public List<ValidationMessage> getErrors() {
        return getErrors(DEFAULT_KEY);
    }

    public List<ValidationMessage> getErrors(final Object key) {
        final Set<ValidationMessage> m = messages.get(key);

        if (null == m || m.isEmpty())
            return emptyList();

        return m.stream().filter(Objects::nonNull).filter(ValidationMessage::error).toList();
    }
}
