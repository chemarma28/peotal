package finance.service;

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

@Singleton
public class MonthlyExpenseService {
    private final DataSource datasource;

    @Inject
    public MonthlyExpenseService(DataSource datasource) {
        this.datasource = datasource;
    }

    public Long persistMonthlyExpense(int year, int month, BigDecimal balance, Long contactId) throws SQLException {
        try (Connection connection = datasource.getConnection()) {
            DSLContext sql = DSL.using(connection);

            Result<Record1<Long>> result = sql.insertInto(MONTHLY_EXPENSE)
                    .columns(MONTHLY_EXPENSE.MONTH_DATE, MONTHLY_EXPENSE.BALANCE, MONTHLY_EXPENSE.CONTACT_ID)
                    .values(LocalDateTime.of(year, month, 1, 0, 0), balance, contactId)
                    .returningResult(MONTHLY_EXPENSE.ID)
                    .fetch();

            return result.getFirst().component1();
        }
    }
}
