package finance.service;

import static finance.db.jooq.tables.ExpenseCard.EXPENSE_CARD;
import static finance.db.jooq.tables.MonthlyExpense.MONTHLY_EXPENSE;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.impl.DSL;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.db.jooq.tables.records.MonthlyExpenseRecord;
import finance.records.Card;
import finance.records.CardFormData;

@Singleton
public class ExpenseCardService {
    private final DataSource datasource;
    private final MonthlyExpenseService monthlyExpenseService;

    @Inject
    public ExpenseCardService(DataSource datasource, MonthlyExpenseService monthlyExpenseService) {
        this.datasource = datasource;
        this.monthlyExpenseService = monthlyExpenseService;
    }

    public Card persistExpenseCard(CardFormData formData, Long contactId) throws SQLException {
        try (Connection connection = datasource.getConnection()) {
            DSLContext sql = DSL.using(connection);

            Result<Record1<Long>> result = sql.select(MONTHLY_EXPENSE.ID).from(MONTHLY_EXPENSE)
                    .where(MONTHLY_EXPENSE.MONTH_DATE.eq(LocalDateTime.from(formData.cardDate())))
                    .fetch();

            new MonthlyExpenseRecord(null, null, BigDecimal.ZERO, contactId, null);

            Long monthlyExpenseId = result.isEmpty()
                    ? monthlyExpenseService.persistMonthlyExpense(formData.cardDate().getYear(),
                            formData.cardDate().getMonthValue(), BigDecimal.ZERO, contactId)
                    : result.getFirst().component1();

            return sql.insertInto(EXPENSE_CARD)
                    .columns(EXPENSE_CARD.CARD_DATE, EXPENSE_CARD.AMOUNT, EXPENSE_CARD.DESCRIPTION,
                            EXPENSE_CARD.CONTACT_ID, EXPENSE_CARD.MONTHLY_EXPENSE_ID)
                    .values(LocalDateTime.from(formData.cardDate()), formData.amount(), formData.description(),
                            contactId, monthlyExpenseId)
                    .returning()
                    .fetch(r -> new Card(r.getId(), r.getDescription(), r.getAmount()))
                    .getFirst();
        }
    }
}
