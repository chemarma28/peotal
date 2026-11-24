package transactions;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static support.Fixtures.category;
import static support.Fixtures.transaction;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import categories.CategoryRepository;
import support.RepositoryTest;

class TransactionRepositoryTest extends RepositoryTest {

    final CategoryRepository categories = new CategoryRepository(validator);
    final TransactionRepository repo = new TransactionRepository(validator, categories);

    @Test
    void saveInsertsTransactionWithGeneratedId() {
        var id = categories.save(handle, category().build()).id;

        var it = repo.save(handle, transaction().categoryId(id).build());

        assertNotNull(it.id);
    }

    @Test
    void updateRejectsMissingTransaction() {
        var id = categories.save(handle, category().build()).id;

        assertThatThrownBy(() -> repo.save(handle, transaction().id(999L).categoryId(id).build()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void saveUpdatesExistingTransaction() {
        var id = categories.save(handle, category().build()).id;
        var it = repo.save(handle, transaction().categoryId(id).description("Lobsters").amount(TEN).build());

        it.description = "Fish";
        it.amount = ONE;

        repo.save(handle, it);

        assertThat(repo.find(handle, it.id)).isPresent().hasValueSatisfying((that) -> {
            assertThat(that.description).isEqualTo("Fish");
            assertThat(that.amount).isEqualByComparingTo("1");
        });
    }

    @Test
    void listIncludesRangeBoundariesAndOrdersByIssuedDescending() {
        var id = categories.save(handle, category().build()).id;
        var early = now().minusDays(1);
        var late = now();

        repo.save(handle, transaction().categoryId(id).description("early").issued(early).build());
        repo.save(handle, transaction().categoryId(id).description("late").issued(late).build());

        assertThat(repo.list(handle, early, late).stream().map(Transaction::getDescription).toList())
                .containsExactly("late", "early");
    }

    @Test
    void listExcludesTransactionsOutsideRange() {
        var id = categories.save(handle, category().build()).id;

        repo.save(handle, transaction().categoryId(id).description("outside").issued(now().minusDays(5)).build());
        repo.save(handle, transaction().categoryId(id).description("inside").build());

        assertThat(repo.list(handle, now(), now().plusDays(1)).stream().map(Transaction::getDescription).toList())
                .containsExactly("inside");
    }

    @Test
    void deleteRemovesTransaction() {
        var id = categories.save(handle, category().build()).id;
        var it = repo.save(handle, transaction().categoryId(id).build());

        repo.delete(handle, it.id);

        assertThat(repo.find(handle, it.id)).isEmpty();
    }
}
