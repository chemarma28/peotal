package transactions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import categories.Category;
import categories.CategoryRepository;
import support.Fixtures;
import support.RepositoryTest;

class TransactionRepositoryTest extends RepositoryTest {

    final CategoryRepository CATEGORIES = new CategoryRepository();
    final TransactionRepository TRANSACTIONS = new TransactionRepository();

    final Category EXPENSE_CATEGORY = Fixtures.expense("Groceries");
    final Category INCOME_CATEGORY = Fixtures.income("Salary");

    final LocalDate EARLY = LocalDate.of(2026, 1, 10);
    final LocalDate MIDDLE = LocalDate.of(2026, 1, 15);
    final LocalDate LATE = LocalDate.of(2026, 1, 20);

    @Test
    void saveInsertsTransactionWithGeneratedId() {
        final Category category = CATEGORIES.save(handle, EXPENSE_CATEGORY);

        final Transaction saved = TRANSACTIONS.save(handle, Fixtures.expense(category, new BigDecimal("12.34")));

        assertNotNull(saved.id);
    }

    @Test
    void saveRoundTripsAllFields() {
        final Category category = CATEGORIES.save(handle, EXPENSE_CATEGORY);

        final Transaction saved = TRANSACTIONS.save(handle, Fixtures.transaction(
                "lunch", MIDDLE, TransactionType.EXPENSE, category.id, new BigDecimal("12.34"), Currency.USD));

        final Transaction loaded = TRANSACTIONS.list(handle, MIDDLE, MIDDLE).getFirst();

        assertEquals(saved.id, loaded.id);
        assertEquals("lunch", loaded.description);
        assertEquals(MIDDLE, loaded.issued);
        assertEquals(TransactionType.EXPENSE, loaded.operation);
        assertEquals(category.id, loaded.categoryId);
        assertEquals(Currency.USD, loaded.currency);
        assertEquals(0, new BigDecimal("12.34").compareTo(loaded.amount));
    }

    @Test
    void saveAllowsNullDescription() {
        final Category category = CATEGORIES.save(handle, EXPENSE_CATEGORY);

        final Transaction saved = TRANSACTIONS.save(handle, Fixtures.transaction(
                null, MIDDLE, TransactionType.EXPENSE, category.id, new BigDecimal("12.34"), Currency.USD));

        final Transaction loaded = TRANSACTIONS.find(handle, saved.id).orElseThrow();

        assertNull(loaded.description);
    }

    @Test
    void saveUpdatesExistingTransaction() {
        final Category category = CATEGORIES.save(handle, EXPENSE_CATEGORY);
        final Transaction saved = TRANSACTIONS.save(handle, Fixtures.transaction(
                "lunch", MIDDLE, TransactionType.EXPENSE, category.id, new BigDecimal("12.34"), Currency.USD));

        saved.description = "dinner";
        saved.amount = new BigDecimal("25.00");
        TRANSACTIONS.save(handle, saved);

        final Transaction loaded = TRANSACTIONS.find(handle, saved.id).orElseThrow();

        assertEquals("dinner", loaded.description);
        assertEquals(0, new BigDecimal("25.00").compareTo(loaded.amount));
        assertEquals(1, TRANSACTIONS.list(handle, MIDDLE, MIDDLE).size());
    }

    @Test
    void findReturnsEmptyForMissingTransaction() {
        assertTrue(TRANSACTIONS.find(handle, 999L).isEmpty());
    }

    @Test
    void saveRoundTripsIncomeOperation() {
        final Category category = CATEGORIES.save(handle, INCOME_CATEGORY);

        TRANSACTIONS.save(handle, Fixtures.transaction(
                "salary", MIDDLE, TransactionType.INCOME, category.id, new BigDecimal("100.00"), Currency.EUR));

        final Transaction loaded = TRANSACTIONS.list(handle, MIDDLE, MIDDLE).getFirst();

        assertEquals(TransactionType.INCOME, loaded.operation);
        assertEquals(Currency.EUR, loaded.currency);
        assertEquals(category.id, loaded.categoryId);
    }

    @Test
    void listIncludesRangeBoundariesAndOrdersByIssuedDescending() {
        final Category category = CATEGORIES.save(handle, EXPENSE_CATEGORY);
        TRANSACTIONS.save(handle, Fixtures.transaction("early", EARLY, TransactionType.EXPENSE, category.id, new BigDecimal("1.00"), Currency.USD));
        TRANSACTIONS.save(handle, Fixtures.transaction("late", LATE, TransactionType.EXPENSE, category.id, new BigDecimal("2.00"), Currency.USD));

        final List<String> descriptions = TRANSACTIONS.list(handle, EARLY, LATE).stream().map(it -> it.description).toList();

        assertEquals(List.of("late", "early"), descriptions);
    }

    @Test
    void listExcludesTransactionsOutsideRange() {
        final Category category = CATEGORIES.save(handle, EXPENSE_CATEGORY);
        TRANSACTIONS.save(handle, Fixtures.transaction("outside", EARLY, TransactionType.EXPENSE, category.id, new BigDecimal("1.00"), Currency.USD));
        TRANSACTIONS.save(handle, Fixtures.transaction("inside", MIDDLE, TransactionType.EXPENSE, category.id, new BigDecimal("2.00"), Currency.USD));

        final List<String> descriptions = TRANSACTIONS.list(handle, MIDDLE, LATE).stream().map(it -> it.description).toList();

        assertEquals(List.of("inside"), descriptions);
    }
}
