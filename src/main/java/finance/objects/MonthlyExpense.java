package finance.objects;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.RowListenerAdapter;
import org.javalite.activejdbc.annotations.Table;
import org.javalite.common.Convert;

@Table("monthly_expense")
public class MonthlyExpense extends Model {
    static {
        validatePresenceOf("contact_id");
        validatePresenceOf("name");
        validatePresenceOf("balance");
        validatePresenceOf("month_date");
    }

    @Deprecated
    public MonthlyExpense() {
    }

    public MonthlyExpense(Long contactId, LocalDate monthDate) {
        this.set("contact_id", contactId);
        this.set("month_date", monthDate);
        this.set("balance", BigDecimal.ZERO);
    }

    public MonthlyExpense(Long contactId, int year, int month) {
        this(contactId, LocalDate.of(year, month, 1));
    }

    public BigDecimal amount() {
        long id = this.getLong("id");

        return Objects.requireNonNullElse(Convert.toBigDecimal(Base.firstCell(
                String.join(" ",
                        "SELECT SUM(expense_card.amount) FROM expense_card",
                        "WHERE expense_card.monthly_expense_id = ?1",
                        "GROUP BY expense_card.monthly_expense_id"),
                id)),
                BigDecimal.ZERO);
    }

    @Override
    protected void beforeSave() {
        final MonthlyExpense origin = this;

        Base.find("SELECT MAX(name) AS max_name FROM monthly_expense").with(new RowListenerAdapter() {
            @Override
            public void onNext(Map<String, Object> row) {
                String maxName = (String) row.get("max_name");

                int number = maxName == null ? 0 : Integer.parseInt(maxName.substring(maxName.indexOf('-') + 1)) + 1;

                origin.set("name", String.format("ME-%07d", number));
            }
        });
    }

    public static MonthlyExpense find(final int year, final int month, final long contactId) {
        return MonthlyExpense.findFirst(String.join(" ",
                "YEAR(month_date) = ?1",
                "AND MONTH(month_date) = ?2",
                "AND contact_id = ?3"),
                year, month, contactId);
    }

    public static MonthlyExpense topUp(final BigDecimal amount, final int year, final int month, final long contactId) {
        final MonthlyExpense m = find(year, month, contactId);

        if (null == m)
            return null;

        m.setBigDecimal("balance", m.getBigDecimal("balance").add(amount));

        return m;
    }
}
