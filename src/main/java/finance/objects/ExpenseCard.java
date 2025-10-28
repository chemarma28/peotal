package finance.objects;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.RowListenerAdapter;
import org.javalite.activejdbc.annotations.Table;

@Table("expense_card")
public class ExpenseCard extends Model {
    static {
        validateNumericalityOf("amount").greaterThan(0);
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

        Long monthlyExpenseId = null;

        List<MonthlyExpense> r = MonthlyExpense.where(
                "YEAR(month_date) = ?1 AND MONTH(month_date) = ?2 AND contact_id = ?3", year, month, contactId);

        if (r.isEmpty()) {
            MonthlyExpense m = new MonthlyExpense(contactId, LocalDate.of(year, month, 1));

            if (m.save())
                monthlyExpenseId = m.getLong("id");
        } else
            monthlyExpenseId = r.getFirst().getLong("id");

        this.set("monthly_expense_id", monthlyExpenseId);

        final ExpenseCard origin = this;

        Base.find("SELECT MAX(name) AS max_name FROM expense_card").with(new RowListenerAdapter() {
            @Override
            public void onNext(Map<String, Object> row) {
                String maxName = (String) row.get("max_name");

                int number = maxName == null ? 0 : Integer.parseInt(maxName.substring(maxName.indexOf('-') + 1)) + 1;

                origin.set("name", String.format("EXP-%06d", number));
            }
        });
    }
}
