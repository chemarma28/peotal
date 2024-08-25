package com.novorock.jexpenses.view.to;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.novorock.jexpenses.domain.ExpenseCard;

public class ExpenseCardsGroupedByDate {
    private Map<LocalDate, List<ExpenseCard>> groups;

    public ExpenseCardsGroupedByDate(List<ExpenseCard> cards) {
        groups = cards.stream()
                .collect(groupingBy(ExpenseCard::getCardDate, TreeMap::new, toList()));
    }

    public Map<LocalDate, List<ExpenseCard>> getGroups() {
        return groups;
    }
}
