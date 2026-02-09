package finance.objects;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExpenseCard extends FinanceCard {
    public ExpenseCard() {
        super();
    }

    public FinanceCategory category() {
        return parent(FinanceCategory.class);
    }

    public ExpenseCard(final Account owner, final LocalDateTime date, final BigDecimal amount, final String description) {
        super(owner, description, date, amount);
    }
}
