package finance.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.domain.ExpenseCard;
import finance.domain.MonthlyExpense;
import finance.repository.Repository;
import finance.service.dto.MonthItem;
import finance.service.mapper.MonthlyExpenseMapper;

@Singleton
public class AnnualMonthlyExpenses {
    @Inject
    private Repository<Long, MonthlyExpense> repo;
    @Inject
    private MonthlyExpenseMapper mapper;

    public List<MonthlyExpense> filterAndSort(int year) {
        return StreamSupport.stream(repo.all().spliterator(), false)
                .filter(monthlyExpense -> year == monthlyExpense.monthDate.getYear())
                .sorted(Comparator.comparing(MonthlyExpense::getMonthDate))
                .toList();

    }

    public List<MonthItem> annual(List<MonthlyExpense> annual) {
        if (annual == null || annual.isEmpty())
            return Collections.emptyList();

        int year = annual.getFirst().monthDate.getYear();

        List<MonthItem> items = new ArrayList<>();

        for (int m = 1; m <= 12; m++)
            if (m != annual.get(m - 1).monthDate.getMonthValue())
                items.add(MonthItem.builder()
                        .month(LocalDate.of(year, m, 1))
                        .amount(BigDecimal.ZERO)
                        .income(BigDecimal.ZERO)
                        .build());
            else
                items.add(mapper.map(annual.get(m - 1), MonthItem.class));

        return items;
    }

    public MonthItem sum(List<MonthlyExpense> annual) {
        return MonthItem.builder()
                .amount(new BigDecimalSum<>(annual.stream().flatMap(m -> m.expenseCards.stream()).toList())
                        .resultByProperty(ExpenseCard::getAmount))
                .income(new BigDecimalSum<>(annual).resultByProperty(MonthlyExpense::getBalance))
                .build();
    }
}
