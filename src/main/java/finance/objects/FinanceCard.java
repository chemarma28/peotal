package finance.objects;

import org.javalite.activejdbc.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class FinanceCard extends Model {
    public FinanceCard() {}

    public FinanceCard(final Account owner, final String description, final LocalDateTime date, final BigDecimal amount) {
        this.setParent(owner);
        this.set("description", description);
        this.set("date", date);
        this.set("amount", amount);
    }

    public Long id() {
        return getLong("id");
    }

    public Account owner() {
        return parent(Account.class);
    }

    public String description() {
        return getString("description");
    }

    public LocalDateTime date() {
        return getLocalDateTime("date");
    }

    public BigDecimal amount() {
        return getBigDecimal("amount");
    }
}
