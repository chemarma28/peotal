package support;

import static java.math.BigDecimal.ONE;
import static transactions.Currency.BYN;
import static transactions.TransactionType.EXPENSE;

import java.time.LocalDate;

import categories.Category;
import categories.Category.CategoryBuilder;
import transactions.Transaction;
import transactions.Transaction.TransactionBuilder;

public final class Fixtures {

    public static final CategoryBuilder category() {
        return Category.builder()
                .operation(EXPENSE)
                .name("Groceries")
                .color("#000000")
                .icon("any")
                .ordinal(null);
    }

    public static final TransactionBuilder transaction() {
        return Transaction.builder()
                .operation(EXPENSE)
                .amount(ONE)
                .issued(LocalDate.now())
                .currency(BYN);
    }

    private Fixtures() {
    }
}
