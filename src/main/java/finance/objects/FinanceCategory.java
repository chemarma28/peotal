package finance.objects;

import org.javalite.activejdbc.Model;

public class FinanceCategory extends Model {
    public FinanceCategory() {}

    public FinanceCategory(final String name) {
        this.set("name", name);
    }

    public Long id() {
        return getLong("id");
    }

    public String name() {
        return getString("name");
    }
}
