package finance.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.domain.ExpenseCard;
import finance.domain.MonthlyExpense;
import finance.repository.Repository;
import finance.service.dto.ExpenseCardsList;
import finance.service.dto.MonthlyExpenseLineItem;
import finance.service.mapper.ExpenseCardMapper;
import finance.service.mapper.MonthlyExpenseMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Singleton
public class ExpensesService {
    private final Repository<Long, MonthlyExpense> monthlyExpensesRepo;
    private final MonthlyExpenseMapper monthlyExpenseMapper;
    private final ExpenseCardMapper expenseCardMapper;

    @Inject
    public ExpensesService(
            Repository<Long, MonthlyExpense> monthlyExpensesRepo,
            MonthlyExpenseMapper monthlyExpenseMapper,
            ExpenseCardMapper expenseCardMapper
    ) {
        this.monthlyExpensesRepo = monthlyExpensesRepo;
        this.monthlyExpenseMapper = monthlyExpenseMapper;
        this.expenseCardMapper = expenseCardMapper;
    }

    private List<MonthlyExpense> getAnnualMonthlyExpenses(int year) {
        List<MonthlyExpense> r = new ArrayList<>();

        for (MonthlyExpense monthlyExpense : monthlyExpensesRepo.all()) {
            if (year == monthlyExpense.monthDate.getYear())
                r.add(monthlyExpense);
        }

        return r;
    }

    private Set<Entry<LocalDate, List<ExpenseCard>>> groupCardsByDateInOrder(List<ExpenseCard> cards) {
        return cards.stream()
                .collect(groupingBy(ExpenseCard::getCardDate, TreeMap::new, toList()))
                .entrySet();
    }

    public List<ExpenseCardsList> getGroupedAndOrderedCardsByDate(int year, int month) {
        List<ExpenseCard> cards = getAnnualMonthlyExpenses(year).stream()
                .filter(m -> Month.of(month).equals(m.monthDate.getMonth()))
                .flatMap(m -> m.expenseCards.stream())
                .toList();

        return groupCardsByDateInOrder(cards).stream()
                .map(entry -> expenseCardMapper.map(entry.getValue()))
                .toList();
    }

    public List<MonthlyExpenseLineItem> getAnnualMonthlyExpenseLineItems(int year, int month) {
        List<MonthlyExpenseLineItem> lineItems = getAnnualMonthlyExpenses(year).stream()
                .map(monthlyExpense -> monthlyExpenseMapper.map(monthlyExpense, MonthlyExpenseLineItem.class))
                .toList();

        if (month <= lineItems.size())
            lineItems.get(month - 1).requested = true;

        return lineItems;
    }

    private BigDecimal getAnnualIncome(int year) {
        List<MonthlyExpense> annual = getAnnualMonthlyExpenses(year);

        return new BigDecimalSum<>(annual).resultByProperty(MonthlyExpense::getBalance);
    }

    private BigDecimal getAnnualSpentAmount(int year) {
        List<MonthlyExpense> annual = getAnnualMonthlyExpenses(year);

        return annual.stream()
                .map(monthly -> new BigDecimalSum<>(monthly.expenseCards)
                        .resultByProperty(ExpenseCard::getAmount)
                )
                .reduce(BigDecimal.ZERO, BigDecimalSum::operation);
    }

    public MonthlyExpenseLineItem getAnnualMonthlyExpenseTotal(int year) {
        return monthlyExpenseMapper.map(getAnnualSpentAmount(year), getAnnualIncome(year));
    }

    public BigDecimal getAnnualBalance(int year) {
        return getAnnualIncome(year).subtract(getAnnualSpentAmount(year));
    }
}
