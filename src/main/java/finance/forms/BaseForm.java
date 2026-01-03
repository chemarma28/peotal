package finance.forms;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import finance.forms.rules.ValidationResult;
import jakarta.servlet.http.HttpServletRequest;

public class BaseForm {
    private Map<String, FormField<?>> mappings = new HashMap<>();

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
        });
    }

    public FormField<?> get(String name) {
        return mappings.get(name);
    }

    public boolean isValid() {
        return mappings.entrySet().stream()
                .map(e -> e.getValue())
                .map(FormField::validationResult)
                .map(ValidationResult::isValid)
                .allMatch(b -> true == b);
    }

    public boolean hasErrors(final String name) {
        if (!mappings.containsKey(name))
            return false;

        return !mappings.get(name).validationResult().isValid();
    }

    public List<String> errors(String name) {
        final FormField<?> f = mappings.get(name);

        if (null == f)
            return emptyList();

        final ValidationResult<?> v = f.validationResult();

        if (null == v || v.isValid())
            return emptyList();

        return singletonList(v.message());
    }
}
