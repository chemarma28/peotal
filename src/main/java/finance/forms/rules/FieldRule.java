package finance.forms.rules;

public interface FieldRule<T, R> {
    public ValidationResult<R> apply(T value);
}
