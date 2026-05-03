package finance.forms;

import jakarta.servlet.http.HttpServletRequest;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class Required<R> implements Mapping<R> {
    private final Mapping<? extends R> mapping;
    private static final String defaultErrorMessage = "The value is required.";
    private String errorMessage;

    public Required(final Mapping<? extends R> mapping) {
        this.mapping = mapping;
    }

    @Override
    public R map(HttpServletRequest req, String key, ValidationResult validation) {
        final String input = req.getParameter(key);

        if (!isBlank(input))
            return mapping.map(req, key, validation);

        validation.addError(key, defaultIfBlank(errorMessage, defaultErrorMessage));

        return null;
    }
}
