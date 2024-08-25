package com.novorock.jexpenses.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MonthlyExpense {
    public Long id;
    public String name;
    public BigDecimal balance;
    public Contact keeper;
    public LocalDate monthDate;
    public List<ExpenseCard> expenseCards;

    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setKeeper(Contact contact) {
        keeper = contact;
    }

    public void setMonthDate(LocalDate month) {
        monthDate = month;
    }

    public void setExpenseCards(List<ExpenseCard> expenseCards) {
        this.expenseCards = expenseCards;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Contact getKeeper() {
        return keeper;
    }

    public LocalDate getMonthDate() {
        return monthDate;
    }

    public List<ExpenseCard> getExpenseCards() {
        return expenseCards;
    }

    public BigDecimal getSpentAmount() {
        BigDecimal sum = BigDecimal.ZERO;

        for (ExpenseCard card : expenseCards)
            sum = sum.add(card.getAmount());

        return sum;
    }

    public BigDecimal getRemainder() {
        BigDecimal spentAmount = getSpentAmount();
        if (balance != null && spentAmount != null)
            return balance.subtract(spentAmount);

        return BigDecimal.ZERO;
    }

    public static List<MonthlyExpense> getMonthlyExpenseSamples() {
        return IntStream.range(1, 13).mapToObj(m -> {
            MonthlyExpense monthlyExpense = new MonthlyExpense();
            monthlyExpense.monthDate = LocalDate.of(1, m, 1);

            Contact keeper = new Contact();
            Office office = new Office();
            office.name = "Office 1";
            keeper.office = office;

            monthlyExpense.keeper = keeper;

            Random random = new Random();
            List<ExpenseCard> cards = new ArrayList<>();

            for (int i = 0; i < random.nextInt(5) + 5; i++) {
                int dayOfMonth = random.nextInt(20) + 1;
                LocalDate cardDate = LocalDate.of(Year.now().getValue(), m, dayOfMonth);
                ExpenseCard card = ExpenseCard.getExpenseCardSample(i, monthlyExpense, null, cardDate);
                cards.add(card);
            }

            monthlyExpense.balance = BigDecimal.valueOf(random.nextInt(500));
            monthlyExpense.expenseCards = cards;

            return monthlyExpense;
        }).collect(Collectors.toList());
    }
}
