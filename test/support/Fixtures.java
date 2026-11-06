package support;

import static transactions.Currency.USD;
import static transactions.TransactionType.EXPENSE;
import static transactions.TransactionType.INCOME;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.Supplier;

import categories.Category;
import transactions.Currency;
import transactions.Transaction;
import transactions.TransactionType;

public final class Fixtures {

    public static final LocalDate ISSUED = LocalDate.of(2026, 1, 15);
    public static final Supplier<Category> GROCERIES = () -> category(EXPENSE, "Groceries", "#000000", "any", 0);
    public static final Supplier<Category> TRANSPORT = () -> category(EXPENSE, "Transport", "#000000", "any", 0);

    private Fixtures() {
    }

    public static Category expense(String name) {
        return category(EXPENSE, name, "#a8b7d1", "circle", 0);
    }

    public static Category income(String name) {
        return category(INCOME, name, "#a8b7d1", "circle", 0);
    }

    public static Category category(TransactionType operation, String name, String color, String icon, int ordinal) {
        return new Category(null, name, operation, color, icon, ordinal);
    }

    public static Transaction expense(Category category, BigDecimal amount) {
        return transaction("fixture", ISSUED, EXPENSE, category.id, amount, USD);
    }

        public static Transaction expense(Category category, BigDecimal amount, Currency currency) {
        return transaction("fixture", ISSUED, EXPENSE, category.id, amount, currency);
    }

    public static Transaction income(Category category, BigDecimal amount) {
        return transaction("fixture", ISSUED, INCOME, category.id, amount, USD);
    }

    public static Transaction transaction(String description, LocalDate issued, TransactionType operation,
            Long categoryId, BigDecimal amount, Currency currency) {
        return new Transaction(null, description, issued, operation, categoryId, amount, currency);
    }
}
