package finance.objects;

import static java.lang.String.format;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.javalite.activejdbc.Model;

public class Account extends Model {
    public Long id() {
        return getLong("id");
    }

    public String name() {
        return getString("name");
    }

    public Account() {
    }

    public Account(final String name) {
        this.set("name", name);
    }

    public IncomeCard topUp(final String description, final LocalDateTime date, final BigDecimal amount) {
        final IncomeCard income = new IncomeCard(this, date, amount, description);

        return income.insert() ? income : null;
    }

    public ExpenseCard reduce(final String description, final LocalDateTime date, final BigDecimal amount) {
        final ExpenseCard expense = new ExpenseCard(this, date, amount, description);

        return expense.insert() ? expense : null;
    }

    private ExpenseCard rollbackExpense(final Long id) {
        final ExpenseCard card = ExpenseCard.findFirst("account_id = ? AND id = ?", this.id(), id);

        return card.delete() ? card : null;
    }

    private IncomeCard rollbackIncome(final Long id) {
        final IncomeCard card = IncomeCard.findFirst("account_id = ? AND id = ?", this.id(), id);

        return card.delete() ? card : null;
    }

    public FinanceCard rollbackTransaction(final TransactionType type, final Long id) {
        switch (type) {
            case EXPENSE:
                return rollbackExpense(id);
            case INCOME:
                return rollbackIncome(id);
            default:
                throw new UnsupportedOperationException("Transaction type should be one of INCOME or EXPENSE");
        }
    }

    @Override
    public String toString() {
        return format("Account[id='%s', name='%s']", this.id(), this.name());
    }
}
