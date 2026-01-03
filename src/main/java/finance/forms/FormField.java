package finance.forms;

import static finance.forms.rules.ValidationResult.validationPositiveResult;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

import finance.forms.rules.FieldRule;
import finance.forms.rules.ValidationResult;
import jakarta.servlet.http.HttpServletRequest;

public final class FormField<T> {
    private final String name;
    private String value;

    private final FieldRule<String, T> rule;
    private ValidationResult<T> validationResult;

    private boolean optional = false;
    private T defaultValue;

    public FormField(final String name, final FieldRule<String, T> rule) {
        this.name = requireNonNull(name);
        this.rule = requireNonNull(rule);
        this.validationResult = validationPositiveResult();
    }

    public FormField(final String name, final String initial, final FieldRule<String, T> rule) {
        this(name, rule);
        this.value = initial;
    }

    public String name() {
        return name;
    }

    public FormField<T> optional(T value) {
        optional = true;
        defaultValue = requireNonNull(value);

        return this;
    }

    public void match(final HttpServletRequest req) {
        String val = req.getParameter(name);

        if (isBlank(val) && optional)
            validationResult = validationPositiveResult(defaultValue);
        else {
            this.value = val;

            validationResult = rule.apply(val);
        }
    }

    public String value() {
        return value;
    }

    public ValidationResult<T> validationResult() {
        return validationResult;
    }
}
