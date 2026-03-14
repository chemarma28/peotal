package finance.forms;

import java.util.List;

public interface Form {
    String requestValue(String key);

    <T> T value(Object key, Class<? extends T> c);

    boolean hasErrors(Object key);

    boolean hasErrors();

    List<String> errors(Object key);

    default boolean isValid() {
        return !hasErrors();
    }
}
