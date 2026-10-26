package transactions;

import static java.lang.String.format;
import java.time.LocalDate;

import org.jdbi.v3.core.Handle;

import categories.Category;
import categories.CategoryRepository;

public final class TransactionValidation {

    private TransactionValidation() {
    }

    public static void validateForCreate(Handle handle, CategoryRepository categories, Transaction transaction) {
        if (transaction.id != null) {
            throw new IllegalArgumentException("id must not be set when creating a transaction");
        }

        validate(handle, categories, transaction);
    }

    public static void validateForUpdate(Handle handle, CategoryRepository categories, Transaction transaction) {
        if (transaction.id == null) {
            throw new IllegalArgumentException("id is required to update a transaction");
        }

        validate(handle, categories, transaction);
    }

    private static void validate(Handle handle, CategoryRepository categories, Transaction transaction) {
        if (transaction.operation == null) {
            throw new IllegalArgumentException("operation is required");
        }

        if (transaction.issued == null) {
            throw new IllegalArgumentException("issued date is required");
        }

        if (transaction.issued.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Transaction date can not be in the future");
        }

        if (transaction.amount == null || transaction.amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be greater than 0");
        }

        if (transaction.currency == null) {
            throw new IllegalArgumentException("currency is required");
        }

        if (transaction.description != null) {
            transaction.description = transaction.description.trim();

            if (transaction.description.isEmpty()) {
                transaction.description = null;
            }
        }

        if (transaction.categoryId == null) {
            throw new IllegalArgumentException("categoryId is required");
        }

        final Category category = categories.find(handle, transaction.categoryId)
                .orElseThrow(() -> new IllegalArgumentException(format("category %d not found", transaction.categoryId)));

        if (category.operation != transaction.operation) {
            throw new IllegalArgumentException("category does not match the transaction operation");
        }
    }
}
