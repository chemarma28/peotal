package support;

import java.math.BigDecimal;
import java.time.LocalDate;

import categories.Category;
import transactions.Currency;
import transactions.Transaction;
import transactions.TransactionType;

public final class Fixtures {

    public static final LocalDate ISSUED = LocalDate.of(2026, 1, 15);

    private Fixtures() {
    }

    public static Category expense(String name) {
        return category(TransactionType.EXPENSE, name, "#a8b7d1", "circle", 0);
    }

    public static Category income(String name) {
        return category(TransactionType.INCOME, name, "#a8b7d1", "circle", 0);
    }

    public static Category category(TransactionType operation, String name, String color, String icon, int ordinal) {
        return new Category(null, name, operation, color, icon, ordinal);
    }

    public static Transaction expense(Category category, BigDecimal amount) {
        return transaction("fixture", ISSUED, TransactionType.EXPENSE, category.id, amount, Currency.USD);
    }

    public static Transaction income(Category category, BigDecimal amount) {
        return transaction("fixture", ISSUED, TransactionType.INCOME, category.id, amount, Currency.USD);
    }

    public static Transaction transaction(String description, LocalDate issued, TransactionType operation, Long categoryId, BigDecimal amount, Currency currency) {
        return new Transaction(null, description, issued, operation, categoryId, amount, currency);
    }
}
