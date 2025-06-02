package finance.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import finance.domain.Contact;
import finance.domain.ExpenseCard;
import finance.domain.MonthlyExpense;
import finance.domain.Office;

import static java.util.stream.Collectors.groupingBy;

public class SimpleFaker {
    private static final Random r = new Random();

    public static final List<Contact> contacts = List.of(
            Contact.builder()
                    .id(1L)
                    .firstName("Emily")
                    .lastName("Clark")
                    .email("emilyclark@gmail.com")
                    .password("default")
                    .isAdmin(true)
                    .expenseCards(new ArrayList<>())
                    .build(),
            Contact.builder()
                    .id(2L)
                    .firstName("Hue")
                    .lastName("Jackman")
                    .email("hue@gmail.com")
                    .password("passw")
                    .isAdmin(true)
                    .expenseCards(new ArrayList<>())
                    .build(),
            Contact.builder()
                    .id(3L)
                    .firstName("Niels")
                    .lastName("Bohr")
                    .email("bohr@twitter.dk")
                    .password("passw")
                    .isAdmin(true)
                    .expenseCards(new ArrayList<>())
                    .build());

    public static final List<Office> offices = IntStream.range(1, contacts.size() + 1)
            .mapToObj(number -> {
                Contact contact = contacts.get(number - 1);

                Office office = Office.builder()
                        .id(3L * number)
                        .name("Office " + number)
                        .contacts(List.of(contact))
                        .build();

                contact.office = office;

                return office;
            })
            .toList();

    private static final String[] items = new String[]{"Gear (p)", "Wires (m)", "Lights (p)"};

    public static List<ExpenseCard> cards = IntStream.range(0, 100)
            .mapToObj(number -> {
                Contact keeper = contacts.get(number % contacts.size());

                String description = items[r.nextInt(items.length)];

                ExpenseCard card = ExpenseCard.builder()
                        .id(5L * number)
                        .amount(BigDecimal.valueOf(10 + r.nextDouble(20)))
                        .cardDate(LocalDate.of(2025, 1 + r.nextInt(12), 10 + r.nextInt(15)))
                        .cardKeeper(keeper)
                        .description(description)
                        .build();

                keeper.expenseCards.add(card);

                return card;
            })
            .toList();

    public static List<MonthlyExpense> monthlyExpenses = IntStream.range(1, 13)
            .mapToObj(number -> MonthlyExpense.builder()
                    .monthDate(LocalDate.of(2025, number, 1))
                    .balance(BigDecimal.valueOf(300 + r.nextDouble(500)))
                    .expenseCards(new ArrayList<>())
                    .build()
            ).toList();

    static {
        class Pair {
            final int e1;
            final long e2;

            Pair(int e1, long e2) {
                this.e1 = e1;
                this.e2 = e2;
            }

            @Override
            public int hashCode() {
                return 10 * e1 + Long.valueOf(e2).hashCode();
            }

            @Override
            public boolean equals(Object other) {
                if (!(other instanceof Pair cast))
                    return false;

                return e1 == cast.e1 && e2 == cast.e2;
            }
        }

        Set<Entry<Pair, List<ExpenseCard>>> grouping = cards.stream()
                .collect(groupingBy(card -> new Pair(card.cardDate.getMonthValue(), card.cardKeeper.id)))
                .entrySet();

        for (Entry<Pair, List<ExpenseCard>> group : grouping) {
            for (ExpenseCard card : group.getValue()) {
                Contact contact = card.cardKeeper;

                int monthIndex = card.getCardDate().getMonthValue() - 1;
                MonthlyExpense monthlyExpense = monthlyExpenses.get(monthIndex);

                monthlyExpense.expenseCards.add(card);
                monthlyExpense.keeper = contact;
            }
        }
    }
}
