package finance.objects;

import org.javalite.activejdbc.Model;

import static finance.service.Formats.fdate;
import static finance.service.Formats.fdecimal;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.abbreviate;

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

    @Override
    public String toString() {
        final long ownerId = this.getLong("owner_id");

        return format(
            "Card[id='%s', owner='%s', date='%s', amount='%s', description='%s']",
            this.id(),
            ownerId,
            fdate(this.date()),
            fdecimal(this.amount()),
            abbreviate(this.description(), 10)
        );
    }
}
