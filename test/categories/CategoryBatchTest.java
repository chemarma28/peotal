package categories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static support.Fixtures.category;

import java.util.List;

import org.junit.jupiter.api.Test;

import support.RepositoryTest;

class CategoryBatchTest extends RepositoryTest {

    final CategoryRepository repo = new CategoryRepository(validator);

    @Test
    void saveAllCreatesEveryCategoryWithSequentialOrdinals() {
        final Category groceries = category().name("groceries").build();
        final Category transport = category().name("transport").build();
        final Category salary = category().name("salary").build();

        repo.saveAll(handle, List.of(groceries, transport, salary));

        final List<Category> saved = repo.listAll(handle);

        assertThat(saved).hasSize(3);
        assertThat(saved.stream().map(Category::getOrdinal).toList()).containsExactly(0, 1, 2);
        assertThat(saved).allSatisfy((it) -> assertThat(it.id).isNotNull());
    }

    @Test
    void rejectsDuplicateNamesWithoutCreatingAnything() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> repo.saveAll(handle, List.of(category().build(), category().build())));

        handle.rollback();

        var categories = repo.listAll(handle);

        assertThat(categories).hasSize(0);
    }
}
