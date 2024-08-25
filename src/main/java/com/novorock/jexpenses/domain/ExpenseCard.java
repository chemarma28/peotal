package com.novorock.jexpenses.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;


public class ExpenseCard {
    public Long id;
    public String name;
    public String description;
    public BigDecimal amount;
    public LocalDate cardDate;
    public Contact cardKeeper;
    public MonthlyExpense monthlyExpense;

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getCardDate() {
        return cardDate;
    }

    public static ExpenseCard getExpenseCardSample(long id, MonthlyExpense monthlyExpense,
            Contact cardKeeper, LocalDate date) {
        ExpenseCard card = new ExpenseCard();
        card.id = id;
        card.monthlyExpense = monthlyExpense;
        card.description = String.format("Item %s", id);
        card.cardDate = date;
        card.cardKeeper = cardKeeper;

        Random random = new Random();
        card.amount = BigDecimal.valueOf(random.nextInt(100));

        return card;
    }

    public static ExpenseCard getExpenseCardSample(long id, MonthlyExpense monthlyExpense,
            Contact contact) {
        return getExpenseCardSample(id, monthlyExpense, contact, LocalDate.now());
    }
}
