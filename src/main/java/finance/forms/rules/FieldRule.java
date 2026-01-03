package finance.forms.rules;

@FunctionalInterface
public interface FieldRule<T, R> {
    public ValidationResult<R> apply(T value);
}
