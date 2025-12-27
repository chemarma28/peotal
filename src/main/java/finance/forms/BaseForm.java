package finance.forms;

import static finance.forms.rules.ValidationResult.validationPositiveResult;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import finance.forms.rules.ValidationResult;
import jakarta.servlet.http.HttpServletRequest;

public class BaseForm {
    private Map<String, FormField<?>> mappings = new HashMap<>();
    private Map<String, ValidationResult<?>> validations = new HashMap<>();

    protected BaseForm() {
    }

    protected void register(final FormField<?> field) {
        mappings.put(field.name(), field);
    }

    protected void register(final FormField<?>... fields) {
        for (final FormField<?> field : fields)
            register(field);
    }

    public void match(final HttpServletRequest req) {
        mappings.forEach((name, field) -> {
            field.match(req);
            validations.put(name, field.validationResult());
        });
    }

    public void match() {
        mappings.forEach((name, field) -> {
            validations.put(name, validationPositiveResult());
        });
    }

    public String value(String name) {
        if (!mappings.containsKey(name))
            return null;

        return mappings.get(name).value();
    }

    public boolean isValid(String name) {
        final ValidationResult<?> v = validations.get(name);

        if (null == v)
            return false;

        return v.isValid();
    }

    public boolean isValid() {
        if (validations.isEmpty())
            return false;

        return validations.entrySet().stream()
                .map(e -> e.getValue())
                .map(ValidationResult::isValid)
                .allMatch(b -> true == b);
    }

    public List<String> errors(String name) {
        final ValidationResult<?> v = validations.get(name);

        if (null == v || v.isValid())
            return emptyList();

        return singletonList(v.message());
    }
}
