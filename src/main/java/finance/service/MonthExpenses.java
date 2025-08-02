package finance.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.StreamSupport;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.domain.ExpenseCard;
import finance.domain.MonthlyExpense;
import finance.repository.Repository;
import finance.service.dto.CardItem;
import finance.service.dto.CardItemPack;
import finance.service.mapper.ExpenseCardMapper;

@Singleton
public class MonthExpenses {
    @Inject
    private Repository<Long, MonthlyExpense> repo;
    @Inject
    private ExpenseCardMapper mapper;

    public List<ExpenseCard> monthly(int year, int month) {
        MonthlyExpense monthlyExpense = StreamSupport.stream(repo.all().spliterator(), false)
                .filter(m -> year == m.monthDate.getYear()
                        && month == m.monthDate.getMonthValue())
                .findFirst()
                .orElse(MonthlyExpense.builder()
                        .expenseCards(Collections.emptyList())
                        .build());

        return monthlyExpense.getExpenseCards();
    }

    public List<CardItemPack> pack(List<ExpenseCard> cards) {
        Map<LocalDate, List<ExpenseCard>> packs = cards.stream()
                .collect(groupingBy(ExpenseCard::getCardDate, TreeMap::new, toList()));

        return packs.entrySet().stream().map(pack -> CardItemPack.builder()
                .cardDate(pack.getKey())
                .cards(pack.getValue().stream().map(card -> mapper.map(card, CardItem.class)).toList())
                .total(CardItem.builder()
                        .description("Total")
                        .amount(new BigDecimalSum<>(pack.getValue()).resultByProperty(ExpenseCard::getAmount))
                        .build())
                .build())
                .toList();
    }
}
