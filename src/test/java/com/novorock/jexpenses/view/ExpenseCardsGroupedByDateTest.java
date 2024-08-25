package com.novorock.jexpenses.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.novorock.jexpenses.domain.ExpenseCard;
import com.novorock.jexpenses.view.to.ExpenseCardsGroupedByDate;

public class ExpenseCardsGroupedByDateTest {
    private List<ExpenseCard> cards;

    @BeforeEach
    void setup() {
        cards = Arrays.asList(
                ExpenseCard.getExpenseCardSample(0, null, null, LocalDate.of(2024, 1, 1)),
                ExpenseCard.getExpenseCardSample(1, null, null, LocalDate.of(2024, 1, 1)),
                ExpenseCard.getExpenseCardSample(2, null, null, LocalDate.of(2024, 1, 2)),
                ExpenseCard.getExpenseCardSample(3, null, null, LocalDate.of(2024, 1, 2)));
    }

    @Test
    void testGroupingByDate() {
        ExpenseCardsGroupedByDate expenseCardsGroupedByDate = new ExpenseCardsGroupedByDate(cards);

        for (Entry<LocalDate, List<ExpenseCard>> entry : expenseCardsGroupedByDate.getGroups()
                .entrySet()) {
            assertEquals(2, entry.getValue().size());
        }

        assertEquals(2, expenseCardsGroupedByDate.getGroups().size());
    }

    @Test
    void testFormat() {
        DecimalFormat format = new DecimalFormat("#.00");
        System.out.println(format.format(175.3));
        System.out.println(format.format(105.3532));
        System.out.println(format.format(BigDecimal.valueOf(10)));
    }
}
