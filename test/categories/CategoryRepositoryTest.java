package categories;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static support.Fixtures.ISSUED;
import static transactions.Currency.BYN;
import static transactions.Currency.USD;

import java.math.BigDecimal;
import java.util.List;

import org.jdbi.v3.core.JdbiException;
import org.junit.jupiter.api.Test;

import support.Fixtures;
import support.RepositoryTest;
import transactions.TransactionRepository;
import transactions.TransactionType;

class CategoryRepositoryTest extends RepositoryTest {

    final CategoryRepository CATEGORIES = new CategoryRepository();
    final TransactionRepository TRANSACTIONS = new TransactionRepository();

    final Category EXPENSE = Fixtures.expense("Groceries");
    final Category INCOME = Fixtures.income("Salary");

    @Test
    void saveInsertsNewCategory() {
        final Category saved = CATEGORIES.save(handle, EXPENSE);

        assertNotNull(saved.id);
        assertEquals("Groceries", saved.name);
        assertEquals(TransactionType.EXPENSE, saved.operation);
    }

    @Test
    void saveAssignsNextOrdinalWhenMissing() {
        EXPENSE.ordinal = null;
        final Category first = CATEGORIES.save(handle, EXPENSE);

        final Category other = Fixtures.expense("Cafe");
        other.ordinal = null;
        final Category second = CATEGORIES.save(handle, other);

        assertEquals(0, first.ordinal.intValue());
        assertEquals(1, second.ordinal.intValue());
    }

    @Test
    void saveUpdatesExistingCategory() {
        final Category saved = CATEGORIES.save(handle, EXPENSE);
        final long countBefore = countCategories();

        saved.name = "Food";
        saved.color = "#ff00ff";
        CATEGORIES.save(handle, saved);

        final Category updated = CATEGORIES.find(handle, saved.id).orElseThrow();
        assertEquals("Food", updated.name);
        assertEquals("#ff00ff", updated.color);
        assertEquals(countBefore, countCategories());
    }

    @Test
    void saveKeepsOrdinalOnUpdateWhenMissing() {
        final Category saved = CATEGORIES.save(handle,
                Fixtures.category(TransactionType.EXPENSE, "Groceries", "#a8b7d1", "circle", 5));

        saved.name = "Food";
        saved.ordinal = null;
        CATEGORIES.save(handle, saved);

        assertEquals(5, CATEGORIES.find(handle, saved.id).orElseThrow().ordinal.intValue());
    }

    @Test
    void findReturnsEmptyForMissingId() {
        assertTrue(CATEGORIES.find(handle, 999L).isEmpty());
    }

    @Test
    void listReturnsAllCategoriesSortedByOrdinalThenName() {
        CATEGORIES.save(handle, Fixtures.category(TransactionType.EXPENSE, "Second", "#000000", null, 1));
        CATEGORIES.save(handle, Fixtures.category(TransactionType.EXPENSE, "Alpha", "#000000", null, 0));
        CATEGORIES.save(handle, Fixtures.category(TransactionType.INCOME, "Income", "#000000", null, 0));

        final List<String> names = CATEGORIES.listAll(handle).stream().map(it -> it.name).toList();

        assertEquals(List.of("Alpha", "Income", "Second"), names);
    }

    @Test
    void listFiltersByOperation() {
        CATEGORIES.save(handle, EXPENSE);
        CATEGORIES.save(handle, INCOME);

        final List<Category> expenses = CATEGORIES.listByOperation(handle, TransactionType.EXPENSE);

        assertEquals(1, expenses.size());
        assertEquals(TransactionType.EXPENSE, expenses.getFirst().operation);
    }

    @Test
    void deleteRemovesCategory() {
        final Category saved = CATEGORIES.save(handle, EXPENSE);

        CATEGORIES.delete(handle, saved.id);

        assertTrue(CATEGORIES.find(handle, saved.id).isEmpty());
    }

    @Test
    void countTransactionsIsZeroForUnusedCategory() {
        final Category saved = CATEGORIES.save(handle, EXPENSE);

        assertEquals(0, CATEGORIES.countTransactions(handle, saved.id));
    }

    @Test
    void countTransactionsCountsUsedCategory() {
        final Category saved = CATEGORIES.save(handle, EXPENSE);
        TRANSACTIONS.save(handle, Fixtures.expense(saved, new BigDecimal("10.00")));

        assertEquals(1, CATEGORIES.countTransactions(handle, saved.id));
    }

    @Test
    void existsByNameIsScopedToOperation() {
        CATEGORIES.save(handle, EXPENSE);

        assertTrue(CATEGORIES.existsByName(handle, TransactionType.EXPENSE, "Groceries", null));
        assertFalse(CATEGORIES.existsByName(handle, TransactionType.INCOME, "Groceries", null));
    }

    @Test
    void existsByNameExcludesGivenId() {
        final Category saved = CATEGORIES.save(handle, EXPENSE);

        assertFalse(CATEGORIES.existsByName(handle, TransactionType.EXPENSE, "Groceries", saved.id));
    }

    @Test
    void reorderUpdatesOrdinals() {
        final Category first = CATEGORIES.save(handle,
                Fixtures.category(TransactionType.EXPENSE, "Alpha", "#000000", null, 0));
        final Category second = CATEGORIES.save(handle,
                Fixtures.category(TransactionType.EXPENSE, "Beta", "#000000", null, 1));

        CATEGORIES.reorder(handle, List.of(
                new CategoryOrder.Item(first.id, 1),
                new CategoryOrder.Item(second.id, 0)));

        final List<String> names = CATEGORIES.listByOperation(handle, TransactionType.EXPENSE).stream()
                .map(it -> it.name).toList();

        assertEquals(List.of("Beta", "Alpha"), names);
    }

    @Test
    void saveRejectsDuplicateNameInSameOperation() {
        CATEGORIES.save(handle, EXPENSE);
        final Category duplicate = Fixtures.expense("Groceries");

        try {
            CATEGORIES.save(handle, duplicate);
            fail("Duplicate name must be rejected");
        } catch (JdbiException expected) {
            // expected: unique index (operation, name)
        }
    }

    @Test
    void getCategorySummariesForTwoTransactions() {
        final Category GROCERIES = CATEGORIES.save(handle, Fixtures.GROCERIES.get());
        TRANSACTIONS.save(handle, Fixtures.expense(GROCERIES, TEN, BYN));
        TRANSACTIONS.save(handle, Fixtures.expense(GROCERIES, TEN, BYN));

        List<CategorySummaries> stat = CATEGORIES.getCategorySummaries(handle, ISSUED, ISSUED);

        assertEquals(1, stat.size());

        final CategorySummaries sum = stat.getFirst();

        assertEquals(1, sum.sums.size());
        assertEquals(BYN, sum.sums.getFirst().currency);
        assertEquals(0, BigDecimal.valueOf(20).compareTo(sum.sums.getFirst().amount));
    }

    @Test
    void getCategorySummariesForTwoDistinctCurrencies() {
        final Category GROCERIES = CATEGORIES.save(handle, Fixtures.GROCERIES.get());
        TRANSACTIONS.save(handle, Fixtures.expense(GROCERIES, TEN, BYN));
        TRANSACTIONS.save(handle, Fixtures.expense(GROCERIES, valueOf(5), USD));

        List<CategorySummaries> stat = CATEGORIES.getCategorySummaries(handle, ISSUED, ISSUED);

        assertEquals(1, stat.size());

        CategorySummary byn = assertDoesNotThrow(
                () -> stat.getFirst().sums.stream().filter((it) -> it.currency.equals(BYN)).findAny().orElseThrow());
        CategorySummary usd = assertDoesNotThrow(
                () -> stat.getFirst().sums.stream().filter((it) -> it.currency.equals(USD)).findAny().orElseThrow());

        assertEquals(0, TEN.compareTo(byn.amount));
        assertEquals(0, valueOf(5).compareTo(usd.amount));
    }

    @Test
    void getCorrectWeightsForCategory() {
        final Category GROCERIES = CATEGORIES.save(handle, Fixtures.GROCERIES.get());
        final Category TRANSPORT = CATEGORIES.save(handle, Fixtures.TRANSPORT.get());

        TRANSACTIONS.save(handle, Fixtures.expense(GROCERIES, TEN, BYN));
        TRANSACTIONS.save(handle, Fixtures.expense(TRANSPORT, TEN, BYN));

        List<CategorySummaries> stats = CATEGORIES.getCategorySummaries(handle, ISSUED, ISSUED);

        CategorySummaries groceriesStat = assertDoesNotThrow(
                () -> stats.stream().filter((it) -> it.categoryId.equals(GROCERIES.id)).findAny().orElseThrow());
        CategorySummaries transportStat = assertDoesNotThrow(
                () -> stats.stream().filter((it) -> it.categoryId.equals(TRANSPORT.id)).findAny().orElseThrow());

        assertThat(groceriesStat.weight).isEqualByComparingTo("0.5");
        assertThat(transportStat.weight).isEqualByComparingTo("0.5");
    }

    @Test
    void singleCategoryHasFullWeight() {
        final Category groceries = CATEGORIES.save(handle, Fixtures.GROCERIES.get());
        TRANSACTIONS.save(handle, Fixtures.expense(groceries, TEN, BYN));

        final List<CategorySummaries> stats = CATEGORIES.getCategorySummaries(handle, ISSUED, ISSUED);

        assertEquals(1, stats.size());
        assertThat(stats.getFirst().weight).isEqualByComparingTo("1.00");
    }

    @Test
    void sharesCurrenciesByAmount() {
        final Category groceries = CATEGORIES.save(handle, Fixtures.GROCERIES.get());
        final Category transport = CATEGORIES.save(handle, Fixtures.TRANSPORT.get());

        TRANSACTIONS.save(handle, Fixtures.expense(groceries, TEN, BYN));
        TRANSACTIONS.save(handle, Fixtures.expense(transport, valueOf(5), USD));

        final List<CategorySummaries> stats = CATEGORIES.getCategorySummaries(handle, ISSUED, ISSUED);

        CategorySummaries groceriesStat = assertDoesNotThrow(
                () -> stats.stream().filter((it) -> it.categoryId.equals(groceries.id)).findAny().orElseThrow());
        CategorySummaries transportStat = assertDoesNotThrow(
                () -> stats.stream().filter((it) -> it.categoryId.equals(transport.id)).findAny().orElseThrow());

        assertThat(groceriesStat.weight).isEqualByComparingTo("0.67");
        assertThat(transportStat.weight).isEqualByComparingTo("0.33");
    }

    @Test
    void incomeWeightsUseIncomeTotal() {
        final Category groceries = CATEGORIES.save(handle, Fixtures.GROCERIES.get());
        final Category salary = CATEGORIES.save(handle, Fixtures.SALARY.get());
        final Category bonus = CATEGORIES.save(handle, Fixtures.income("Bonus"));

        TRANSACTIONS.save(handle, Fixtures.expense(groceries, valueOf(200), BYN));
        TRANSACTIONS.save(handle, Fixtures.income(salary, valueOf(30)));
        TRANSACTIONS.save(handle, Fixtures.income(bonus, valueOf(70)));

        final List<CategorySummaries> stats = CATEGORIES.getCategorySummaries(handle, ISSUED, ISSUED);

        CategorySummaries groceriesStat = assertDoesNotThrow(
                () -> stats.stream().filter((it) -> it.categoryId.equals(groceries.id)).findAny().orElseThrow());
        CategorySummaries salaryStat = assertDoesNotThrow(
                () -> stats.stream().filter((it) -> it.categoryId.equals(salary.id)).findAny().orElseThrow());
        CategorySummaries bonusStat = assertDoesNotThrow(
                () -> stats.stream().filter((it) -> it.categoryId.equals(bonus.id)).findAny().orElseThrow());

        assertThat(groceriesStat.weight).isEqualByComparingTo("1.00");
        assertThat(salaryStat.weight).isEqualByComparingTo("0.30");
        assertThat(bonusStat.weight).isEqualByComparingTo("0.70");
    }

    private long countCategories() {
        return handle.createQuery("SELECT COUNT(*) FROM categories").mapTo(Long.class).one();
    }
}
