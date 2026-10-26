package transactions;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import categories.Category;
import categories.CategoryRepository;
import support.Fixtures;
import support.RepositoryTest;

class TransactionValidationTest extends RepositoryTest {

    final CategoryRepository CATEGORIES = new CategoryRepository();
    Category category;

    @BeforeEach
    void createCategory() {
        category = CATEGORIES.save(handle, Fixtures.expense("Groceries"));
    }

    @Test
    void createAllowsNullDescription() {
        final Transaction transaction = Fixtures.transaction(
                null, LocalDate.now(), TransactionType.EXPENSE, category.id, BigDecimal.ONE, Currency.USD);

        assertDoesNotThrow(() -> TransactionValidation.validateForCreate(handle, CATEGORIES, transaction));
    }

    @Test
    void createRejectsExistingId() {
        final Transaction transaction = Fixtures.transaction(
                "lunch", LocalDate.now(), TransactionType.EXPENSE, category.id, BigDecimal.ONE, Currency.USD);
        transaction.id = 1L;

        assertThrows(IllegalArgumentException.class,
                () -> TransactionValidation.validateForCreate(handle, CATEGORIES, transaction));
    }

    @Test
    void updateRequiresId() {
        final Transaction transaction = Fixtures.transaction(
                "lunch", LocalDate.now(), TransactionType.EXPENSE, category.id, BigDecimal.ONE, Currency.USD);

        assertThrows(IllegalArgumentException.class,
                () -> TransactionValidation.validateForUpdate(handle, CATEGORIES, transaction));
    }

    @Test
    void updateRejectsCategoryFromAnotherOperation() {
        final Category income = CATEGORIES.save(handle, Fixtures.income("Salary"));
        final Transaction transaction = Fixtures.transaction(
                "salary", LocalDate.now(), TransactionType.EXPENSE, income.id, BigDecimal.ONE, Currency.USD);
        transaction.id = 1L;

        assertThrows(IllegalArgumentException.class,
                () -> TransactionValidation.validateForUpdate(handle, CATEGORIES, transaction));
    }
}
