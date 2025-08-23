package finance.service;

import static finance.db.jooq.tables.ExpenseCard.EXPENSE_CARD;
import static finance.db.jooq.tables.MonthlyExpense.MONTHLY_EXPENSE;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.jooq.impl.DSL.day;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.month;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.year;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.db.jooq.tables.records.ExpenseCardRecord;
import finance.records.Card;
import finance.records.CardFolder;
import finance.records.MonthlyInfo;

@Singleton
public class DashboardService {
    private final DataSource datasource;

    @Inject
    public DashboardService(DataSource datasource) {
        this.datasource = datasource;
    }

    public CardFolder collectExpenseCardsByDate(int year, int month, int day) throws SQLException {
        try (Connection connection = datasource.getConnection()) {
            DSLContext sql = DSL.using(connection);

            List<Card> cards = sql.select()
                    .from(EXPENSE_CARD)
                    .where(year(EXPENSE_CARD.CARD_DATE).eq(year)
                            .and(month(EXPENSE_CARD.CARD_DATE).eq(month))
                            .and(day(EXPENSE_CARD.CARD_DATE).eq(day)))
                    .fetch()
                    .into(EXPENSE_CARD)
                    .stream()
                    .map(r -> new Card(r.getId(), r.getDescription(), r.getAmount()))
                    .toList();

            return new CardFolder(LocalDate.of(year, month, day), cards,
                    new BigDecimalSum<>(cards).resultByProperty(Card::amount));
        }
    }

    public List<CardFolder> collectExpenseCardsByDate(int year, int month) {
        try (Connection connection = datasource.getConnection()) {
            DSLContext sql = DSL.using(connection);

            return sql.select()
                    .from(EXPENSE_CARD)
                    .where(year(EXPENSE_CARD.CARD_DATE).eq(year))
                    .and(month(EXPENSE_CARD.CARD_DATE).eq(month))
                    .collect(groupingBy(r -> r.get(EXPENSE_CARD.CARD_DATE),
                            mapping(r -> r.into(EXPENSE_CARD), toList())))
                    .entrySet()
                    .stream()
                    .map(e -> new CardFolder(
                            e.getKey().toLocalDate(),
                            e.getValue()
                                    .stream()
                                    .map(r -> new Card(r.getId(), r.getDescription(), r.getAmount()))
                                    .toList(),
                            new BigDecimalSum<>(e.getValue()).resultByProperty(ExpenseCardRecord::getAmount)))
                    .sorted(Comparator.comparing(CardFolder::date))
                    .toList();
        } catch (SQLException e1) {
            return Collections.emptyList();
        }
    }

    public List<MonthlyInfo> collectMonthlyExpenses(int year) {
        try (Connection connection = datasource.getConnection()) {
            DSLContext sql = DSL.using(connection);

            Iterator<MonthlyInfo> iterator = sql.select(MONTHLY_EXPENSE.MONTH_DATE,
                    MONTHLY_EXPENSE.BALANCE,
                    field(select(sum(EXPENSE_CARD.AMOUNT))
                            .from(EXPENSE_CARD)
                            .where(EXPENSE_CARD.MONTHLY_EXPENSE_ID.eq(MONTHLY_EXPENSE.ID))
                            .and(month(EXPENSE_CARD.CARD_DATE).eq(month(MONTHLY_EXPENSE.MONTH_DATE)))
                            .and(year(EXPENSE_CARD.CARD_DATE).eq(year))))
                    .from(MONTHLY_EXPENSE)
                    .where(year(MONTHLY_EXPENSE.MONTH_DATE).eq(year))
                    .stream()
                    .map(r -> new MonthlyInfo(
                            r.component1().toLocalDate(),
                            Objects.requireNonNullElse(r.component2(), BigDecimal.ZERO),
                            Objects.requireNonNullElse(r.component3(), BigDecimal.ZERO)))
                    .sorted(Comparator.comparing(MonthlyInfo::monthDate))
                    .iterator();

            List<MonthlyInfo> list = new ArrayList<>();
            MonthlyInfo info = iterator.hasNext() ? iterator.next() : null;
            BigDecimal income = BigDecimal.ZERO;
            BigDecimal amount = BigDecimal.ZERO;

            for (int m = 1; m <= 12; m++) {
                if (info != null && m == info.monthDate().getMonthValue()) {
                    list.add(info);
                    income = income.add(info.income());
                    amount = amount.add(info.amount());

                    info = iterator.hasNext() ? iterator.next() : null;
                } else
                    list.add(new MonthlyInfo(LocalDate.of(year, m, 1), BigDecimal.ZERO, BigDecimal.ZERO));
            }

            list.add(new MonthlyInfo(null, income, amount));

            return list;
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }
}
