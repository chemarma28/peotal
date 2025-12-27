package finance.forms.rules;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.List;
import java.util.Objects;

public class ValidationResult<R> {
    private final R value;
    private final boolean isValid;
    private final List<String> details;
    private String message = "";

    public ValidationResult(boolean isValid, final R value, final List<String> details) {
        this.isValid = isValid;
        this.value = value;
        this.details = details;
    }

    public ValidationResult<R> message(final String message) {
        this.message = message;

        return this;
    }

    public R value() {
        return value;
    }

    public boolean isValid() {
        return isValid;
    }

    public List<String> details() {
        return details;
    }

    public String message() {
        return message;
    }

    public static <T> ValidationResult<T> validationNegativeResult(final String message, final List<String> details) {
        return new ValidationResult<T>(false, null, Objects.requireNonNull(details)).message(message);
    }

    public static <T> ValidationResult<T> validationNegativeResult(final String message) {
        return new ValidationResult<T>(false, null, singletonList(message)).message(message);
    }

    public static <T> ValidationResult<T> validationPositiveResult(final T value) {
        return new ValidationResult<T>(true, Objects.requireNonNull(value), emptyList());
    }

    public static <T> ValidationResult<T> validationPositiveResult() {
        return new ValidationResult<T>(true, null, emptyList());
    }
}
