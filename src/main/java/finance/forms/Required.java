package finance.forms;

import jakarta.servlet.http.HttpServletRequest;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

public class Required<R> implements Mapping<R> {
    private final Mapping<? extends R> mapping;
    private static final String defaultErrorMessage = "The field is required";
    private String errorMessage;

    public Required(final Mapping<? extends R> mapping) {
        this.mapping = mapping;
    }

    @Override
    public R map(HttpServletRequest req, String key, ValidationResult validation) {
        final R result = this.mapping.map(req, key, validation);

        if (null != result)
            return result;

        validation.addError(key, defaultIfBlank(errorMessage, defaultErrorMessage));

        return null;
    }

    public Required<R> errorMessage(final String message) {
        this.errorMessage = message;

        return this;
    }
}
