package finance.web.forms;

public class FormField<R> {
    private final String name;
    private final Rule<String, R> rule;

    public FormField(final String name, final Rule<String, R> rule) {
        this.name = name;
        this.rule = rule;
    }

    public String name() {
        return name;
    }

    Rule<String, R> rule() {
        return rule;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (null == obj)
            return false;

        if (!(obj instanceof FormField))
            return false;

        final FormField o = (FormField) obj;

        return name.equals(o.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
