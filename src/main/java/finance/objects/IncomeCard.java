package finance.objects;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class IncomeCard extends FinanceCard {
    public IncomeCard() {
        super();
    }

    public IncomeCard(final Account owner, final LocalDateTime date, final BigDecimal amount, final String description) {
        super(owner, description, date, amount);
    }
}
