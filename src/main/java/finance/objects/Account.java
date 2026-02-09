package finance.objects;

import org.javalite.activejdbc.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account extends Model {
    public Long id() {
        return getLong("id");
    }

    public String name() {
        return getString("name");
    }

    public Account() {}

    public Account(final String name) {
        this.set("name", name);
    }

    public IncomeCard topUp(final String description, final LocalDateTime date, final BigDecimal amount) {
        final IncomeCard income = new IncomeCard(this, date, amount, description);

        if (income.save())
            return income;

        return null;
    }

    public ExpenseCard reduce(final String description, final LocalDateTime date, final BigDecimal amount) {
        final ExpenseCard expense = new ExpenseCard(this, date, amount,  description);

        if (expense.save())
            return expense;

        return null;
    }
}
