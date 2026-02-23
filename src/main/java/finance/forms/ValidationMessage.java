package finance.forms;

import java.util.Objects;

public final class ValidationMessage {
    public Object key;
    public final String message;
    public final boolean error;

    private ValidationMessage(final String message, final boolean error) {
        this.message = message;
        this.error = error;
    }

    public static ValidationMessage error(final String message) {
        return new ValidationMessage(message, true);
    }

    public static ValidationMessage success(final String message) {
        return new ValidationMessage(message, false);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, message, error);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other)
            return true;

        if (other instanceof ValidationMessage o)
            return Objects.equals(message, o.message) && Objects.equals(key, o.key) && Objects.equals(error, o.error);

        return false;
    }
}
