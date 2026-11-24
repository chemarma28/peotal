package categories;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.valueOf;
import static java.time.LocalDate.now;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static support.Fixtures.category;
import static support.Fixtures.transaction;
import static transactions.Currency.BYN;
import static transactions.Currency.USD;
import static transactions.TransactionType.EXPENSE;
import static transactions.TransactionType.INCOME;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import support.RepositoryTest;
import transactions.TransactionRepository;

class CategoryRepositoryTest extends RepositoryTest {

    final CategoryRepository repo = new CategoryRepository(validator);
    final TransactionRepository transactionRepo = new TransactionRepository(validator, repo);

    @Test
    void saveInsertsNewCategory() {
        var it = repo.save(handle, category().name("Groceries").operation(EXPENSE).build());

        assertNotNull(it.id);
        var that = assertDoesNotThrow(() -> repo.find(handle, it.id).orElseThrow(NoSuchElementException::new));
        assertEquals("Groceries", that.name);
        assertEquals(EXPENSE, that.operation);
    }

    @Test
    void saveAssignsOrdinal() {
        var id = repo.save(handle, category().ordinal(null).build()).id;

        var it = assertDoesNotThrow(() -> repo.find(handle, id).orElseThrow(NoSuchElementException::new));
        assertEquals(0, it.ordinal);
    }

    @Test
    void saveAssignsNextOrdinal() {
        repo.save(handle, category().ordinal(2).build());
        var id = repo.save(handle, category().name("Next").ordinal(null).build()).id;

        var it = assertDoesNotThrow(() -> repo.find(handle, id).orElseThrow(NoSuchElementException::new));
        assertEquals(3, it.ordinal);
    }

    @Test
    void saveUpdatesExisting() {
        var it = repo.save(handle, category().name("Groceries").operation(EXPENSE).build());

        it.name = "Services";

        repo.save(handle, it);

        var that = assertDoesNotThrow(() -> repo.find(handle, it.id).orElseThrow(NoSuchElementException::new));
        assertEquals("Services", that.name);
    }

    @Test
    void listReturnsAllOrderedByOrdinalAndName() {
        repo.save(handle, category().name("Groceries").ordinal(3).build());
        repo.save(handle, category().name("Transport").ordinal(1).build());
        repo.save(handle, category().name("Farmacy").ordinal(2).build());

        List<Category> it = repo.listAll(handle);

        assertThat(it.stream().map(Category::getName).toList()).containsExactly("Transport", "Farmacy", "Groceries");
    }

    @Test
    void listFilterByOperation() {
        repo.save(handle, category().name("Groceries").operation(EXPENSE).build());
        repo.save(handle, category().name("Gifts").operation(INCOME).build());

        List<Category> it = repo.listByOperation(handle, INCOME);

        assertThat(it.stream().map(Category::getName).toList()).containsExactly("Gifts");
    }

    @Test
    void deleteRemovesCategory() {
        var id = repo.save(handle, category().build()).id;

        assertThat(repo.find(handle, id)).isNotEmpty();
        repo.delete(handle, id);

        assertThat(repo.find(handle, id)).isEmpty();
    }

    @Test
    void countTransactionsIsZero() {
        var id = repo.save(handle, category().build()).id;

        assertEquals(0, repo.countTransactions(handle, id));
    }

    @Test
    void countTransactionsCountsUsedCategory() {
        var id = repo.save(handle, category().build()).id;

        transactionRepo.save(handle, transaction().categoryId(id).build());

        assertEquals(1, repo.countTransactions(handle, id));
    }

    @Test
    void existsByNameIsScopedToOperation() {
        repo.save(handle, category().name("Groceries").operation(EXPENSE).build());

        assertThat(repo.existsByName(handle, EXPENSE, "Groceries", -1L)).isTrue();
        assertThat(repo.existsByName(handle, INCOME, "Groceries", -1L)).isFalse();
    }

    @Test
    void existsByNameExcludesGivenId() {
        var id = repo.save(handle, category().name("Groceries").build()).id;

        assertThat(repo.existsByName(handle, EXPENSE, "Groceries", id)).isFalse();
    }

    @Test
    void reorderUpdatesOrdinals() {
        var first = repo.save(handle, category().name("Alpha").ordinal(0).build());
        var second = repo.save(handle, category().name("Beta").ordinal(1).build());

        repo.reorder(handle, List.of(
                new CategoryOrder.Item(first.id, 1),
                new CategoryOrder.Item(second.id, 0)));

        assertThat(repo.listByOperation(handle, EXPENSE).stream().map(Category::getName).toList())
                .containsExactly("Beta", "Alpha");
    }

    @Test
    void saveRejectsDuplicateName() {
        repo.save(handle, category().name("Groceries").build());

        assertThatThrownBy(() -> repo.save(handle, category().name("Groceries").build()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void getCategorySummariesForTwoTransactions() {
        var groceries = repo.save(handle, category().name("Groceries").build());

        transactionRepo.save(handle, transaction().categoryId(groceries.id).amount(TEN).build());
        transactionRepo.save(handle, transaction().categoryId(groceries.id).amount(TEN).build());

        var stats = repo.getCategorySummaries(handle, now(), now());

        assertThat(stats).hasSize(1);

        assertThat(stats.getFirst().sums.getFirst()).satisfies((it) -> {
            assertThat(it.currency).isEqualTo(BYN);
            assertThat(it.amount).isEqualByComparingTo("20");
        });
    }

    @Test
    void getCategorySummariesForTwoDistinctCurrencies() {
        var id = repo.save(handle, category().name("Groceries").build()).id;

        transactionRepo.save(handle, transaction().categoryId(id).amount(TEN).currency(BYN).build());
        transactionRepo.save(handle, transaction().categoryId(id).amount(ONE).currency(USD).build());

        var sums = repo.getCategorySummaries(handle, now(), now()).getFirst().sums;

        assertThat(sums).hasSize(2);

        assertThat(sums).anySatisfy(it -> {
            assertThat(it.currency).isEqualTo(BYN);
            assertThat(it.amount).isEqualByComparingTo("10");
        });

        assertThat(sums).anySatisfy(it -> {
            assertThat(it.currency).isEqualTo(USD);
            assertThat(it.amount).isEqualByComparingTo("1");
        });
    }

    @Test
    void getCorrectWeightsForCategory() {
        var groceries = repo.save(handle, category().name("Groceries").build());
        var transport = repo.save(handle, category().name("Transport").build());

        transactionRepo.save(handle, transaction().categoryId(groceries.id).amount(TEN).build());
        transactionRepo.save(handle, transaction().categoryId(transport.id).amount(TEN).build());

        var stats = repo.getCategorySummaries(handle, now(), now());

        assertThat(stats).hasSize(2);
        assertThat(stats).allSatisfy(it -> assertThat(it.weight).isEqualByComparingTo("0.50"));
    }

    @Test
    void singleCategoryHasFullWeight() {
        var groceries = repo.save(handle, category().name("Groceries").build());

        transactionRepo.save(handle, transaction().categoryId(groceries.id).amount(TEN).build());

        assertThat(repo.getCategorySummaries(handle, now(), now())).singleElement()
                .satisfies((stats) -> assertThat(stats.weight).isEqualByComparingTo("1.00"));
    }

    @Test
    void sharesCurrenciesByAmount() {
        var groceries = repo.save(handle, category().name("Groceries").build());
        var transport = repo.save(handle, category().name("Transport").build());

        transactionRepo.save(handle, transaction().categoryId(groceries.id).amount(TEN).currency(BYN).build());
        transactionRepo.save(handle, transaction().categoryId(transport.id).amount(valueOf(5)).currency(USD).build());

        var stats = repo.getCategorySummaries(handle, now(), now());

        assertThat(stats).hasSize(2);
        assertThat(stats.get(0).weight).isEqualByComparingTo("0.67");
        assertThat(stats.get(1).weight).isEqualByComparingTo("0.33");
    }

    @Test
    void incomeWeightsUseIncomeTotal() {
        var groceries = repo.save(handle, category().name("Groceries").build());
        var salary = repo.save(handle, category().name("Salary").operation(INCOME).build());
        var bonus = repo.save(handle, category().name("Bonus").operation(INCOME).build());

        transactionRepo.save(handle, transaction().categoryId(groceries.id).amount(valueOf(200)).build());
        transactionRepo.save(handle, transaction().categoryId(salary.id).amount(valueOf(30)).operation(INCOME).build());
        transactionRepo.save(handle, transaction().categoryId(bonus.id).amount(valueOf(70)).operation(INCOME).build());

        var weights = repo.getCategorySummaries(handle, now(), now()).stream()
                .collect(toMap(CategorySummaries::getCategoryId, CategorySummaries::getWeight));

        assertThat(weights.get(groceries.id)).isEqualByComparingTo("1.00");
        assertThat(weights.get(salary.id)).isEqualByComparingTo("0.30");
        assertThat(weights.get(bonus.id)).isEqualByComparingTo("0.70");
    }
}
