package categories;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import support.Fixtures;
import support.RepositoryTest;
import transactions.TransactionType;

class CategoryBatchTest extends RepositoryTest {

    final CategoryRepository CATEGORIES = new CategoryRepository();

    @Test
    void saveAllCreatesEveryCategoryWithSequentialOrdinals() {
        final List<Category> items = List.of(
                category(TransactionType.EXPENSE, "Groceries", "#7cb342", "groceries"),
                category(TransactionType.EXPENSE, "Transport", "#5c6bc0", "transport"),
                category(TransactionType.EXPENSE, "Restaurant", "#ef6c00", "restaurant"),
                category(TransactionType.EXPENSE, "Internet/Mobile", "#29b6f6", "internet"),
                category(TransactionType.EXPENSE, "Household", "#8d6e63", "household"),
                category(TransactionType.EXPENSE, "Services", "#607d8b", "services"),
                category(TransactionType.INCOME, "Salary", "#2e7d32", "salary"),
                category(TransactionType.INCOME, "Gifts", "#c2185b", "gift"));

        CategoryValidation.validateForBatch(handle, CATEGORIES, items);
        CATEGORIES.saveAll(handle, items);

        final List<Category> saved = CATEGORIES.listAll(handle);

        assertEquals(8, saved.size());
        assertEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7), saved.stream().map(it -> it.ordinal).toList());
        assertEquals(6, saved.stream().filter(it -> it.operation == TransactionType.EXPENSE).count());
        assertEquals(2, saved.stream().filter(it -> it.operation == TransactionType.INCOME).count());
        saved.forEach(it -> assertNotNull(it.id));
    }

    @Test
    void rejectsEmptyBatchWithoutCreatingAnything() {
        assertThrows(IllegalArgumentException.class,
                () -> CategoryValidation.validateForBatch(handle, CATEGORIES, List.of()));

        assertEquals(0, CATEGORIES.listAll(handle).size());
    }

    @Test
    void rejectsDuplicateNamesWithinBatchWithoutCreatingAnything() {
        final List<Category> items = List.of(
                category(TransactionType.EXPENSE, "Groceries", "#7cb342", "groceries"),
                category(TransactionType.EXPENSE, "Groceries", "#7cb342", "groceries"));

        assertThrows(IllegalArgumentException.class,
                () -> CategoryValidation.validateForBatch(handle, CATEGORIES, items));

        assertEquals(0, CATEGORIES.listAll(handle).size());
    }

    @Test
    void rejectsNameAlreadyPresentInDatabase() {
        CATEGORIES.save(handle, Fixtures.expense("Groceries"));

        final List<Category> items = List.of(
                category(TransactionType.EXPENSE, "Groceries", "#7cb342", "groceries"));

        assertThrows(IllegalArgumentException.class,
                () -> CategoryValidation.validateForBatch(handle, CATEGORIES, items));

        assertEquals(1, CATEGORIES.listAll(handle).size());
    }

    private Category category(TransactionType operation, String name, String color, String icon) {
        final Category category = Fixtures.category(operation, name, color, icon, 0);
        category.ordinal = null;

        return category;
    }
}
