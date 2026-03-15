package finance.objects;

import static finance.objects.TransactionType.EXPENSE;
import static finance.objects.TransactionType.INCOME;
import static java.util.Collections.emptyList;
import static java.util.Collections.sort;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DailyTransactions {
    private final LocalDateTime begin;
    private final LocalDateTime end;
    private final List<DailyTransaction> transactions = new ArrayList<>();
    private final Map<LocalDate, List<DailyTransaction>> daily;

    public DailyTransactions(final LocalDate begin, final LocalDate end) {
        this.begin = begin.atStartOfDay();
        this.end = end.atTime(23, 59, 59);

        this.load();
        this.daily = collect();
    }

    public DailyTransactions(final YearMonth yearMonth) {
        this(yearMonth.atDay(1), yearMonth.atEndOfMonth());
    }

    public DailyTransactions(final LocalDate day) {
        this(day, day);
    }

    private void load() {
        final String q = "date BETWEEN ?1 AND ?2";

        List<ExpenseCard> expenses = ExpenseCard.where(q, begin, end);

        transactions.addAll(expenses.stream().map(DailyTransaction::new).toList());

        List<IncomeCard> incomes = IncomeCard.where(q, begin, end);

        transactions.addAll(incomes.stream().map(DailyTransaction::new).toList());
    }

    private Map<LocalDate, List<DailyTransaction>> collect() {
        return transactions.stream().filter(Objects::nonNull).collect(groupingBy(t -> t.date().toLocalDate()));
    }

    public List<LocalDate> days() {
        final List<LocalDate> l = new ArrayList<>(daily.keySet());
        sort(l, Comparator.reverseOrder());

        return l;
    }

    public List<DailyTransaction> day(final LocalDate d) {
        final List<DailyTransaction> r = daily.getOrDefault(d, emptyList());

        r.sort(comparing(DailyTransaction::date));

        return r;
    }

    public BigDecimal amount(final LocalDate d) {
        if (!daily.containsKey(d))
            return BigDecimal.ZERO;

        return daily.get(d).stream()
                .map(DailyTransaction::amount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal expenseAmount() {
        return amount(EXPENSE);
    }

    public BigDecimal incomeAmount() {
        return amount(INCOME);
    }

    private BigDecimal amount(TransactionType type) {
        return transactions.stream()
                .filter(Objects::nonNull)
                .filter(t -> type.equals(t.type()))
                .map(DailyTransaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int count() {
        if (null == transactions || transactions.isEmpty())
            return 0;

        return transactions.size();
    }
}
