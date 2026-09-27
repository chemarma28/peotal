package transactions;

import static java.lang.String.format;
import java.time.LocalDate;

import org.jdbi.v3.core.Handle;

import categories.Category;
import categories.CategoryRepository;

public final class TransactionValidation {

    private TransactionValidation() {
    }

    public static void validate(Handle handle, CategoryRepository categories, Transaction transaction) {
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

        if (transaction.description == null || transaction.description.isBlank()) {
            throw new IllegalArgumentException("description is required");
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
