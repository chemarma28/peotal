package finance.objects;

import static org.javalite.common.Convert.toInteger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("expense_card")
public class ExpenseCard extends Model {
    static {
        validatePresenceOf("contact_id");
        validateNumericalityOf("amount");
        validatePresenceOf("card_date");
    }

    @Deprecated
    public ExpenseCard() {
    }

    public ExpenseCard(Long contactId, BigDecimal amount, LocalDate cardDate, String description) {
        this.set("contact_id", contactId);
        this.set("amount", amount);
        this.set("card_date", LocalDateTime.of(cardDate, LocalTime.MIN));
        this.set("description", description);
    }

    @Override
    protected void beforeSave() {
        Long contactId = this.getLong("contact_id");
        LocalDateTime date = this.getLocalDateTime("card_date");
        int year = date.getYear();
        int month = date.getMonthValue();

        MonthlyExpense m = MonthlyExpense.findFirst(String.join(" ",
                "YEAR(month_date) = ?1",
                "AND MONTH(month_date) = ?2",
                "AND contact_id = ?3"),
                year, month, contactId);

        if (null == m) {
            m = new MonthlyExpense(contactId, LocalDate.of(year, month, 1));
            m.saveIt();
        }

        this.setParent(m);
        this.set("name", nextName());
    }

    private String nextName() {
        final String maxName = (String) Base.firstCell("SELECT MAX(name) AS max_name FROM expense_card");
        int number = maxName == null ? 0 : toInteger(maxName.substring(maxName.indexOf('-') + 1)) + 1;

        return String.format("EXP-%06d", number);
    }
}
