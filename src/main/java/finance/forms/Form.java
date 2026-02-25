package finance.forms;

public interface Form {
    String requestValue(String key);

    <T> T value(Object key, Class<? extends T> c);

    boolean hasErrors(Object key);

    boolean hasErrors();

    default boolean isValid() {
        return !hasErrors();
    }
}
