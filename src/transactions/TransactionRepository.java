package transactions;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.jdbi.v3.core.Handle;

import categories.Category;
import categories.CategoryRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import support.ConstraintValidator;

@Singleton
public class TransactionRepository {

    private final ConstraintValidator constraints;
    private final CategoryRepository categories;

    @Inject
    public TransactionRepository(ConstraintValidator constraints, CategoryRepository categories) {
        this.constraints = constraints;
        this.categories = categories;
    }

    public Transaction save(Handle handle, Transaction it) {
        it.description = Optional.ofNullable(it.description)
                .map(String::strip)
                .filter(description -> !description.isEmpty())
                .orElse(null);

        if (it.id == null) {
            return create(handle, it);
        }

        return update(handle, it);
    }

    private Transaction create(Handle handle, Transaction it) {
        constraints.validate(it);
        requireCategory(handle, it);

        final String q = """
            INSERT INTO transactions (description, issued, operation, category_id, amount, currency)
            VALUES (:description, :issued, :operation, :categoryId, :amount, :currency)
        """;

        it.id = handle.createUpdate(q)
                .bindBean(it)
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Long.class)
                .one();

        return it;
    }

    private Transaction update(Handle handle, Transaction it) {
        find(handle, it.id).orElseThrow(NoSuchElementException::new);

        constraints.validate(it);
        requireCategory(handle, it);

        final String q = """
            UPDATE transactions
            SET description = :description,
                issued = :issued,
                operation = :operation,
                category_id = :categoryId,
                amount = :amount,
                currency = :currency
            WHERE id = :id
        """;

        handle.createUpdate(q).bindBean(it).execute();

        return it;
    }

    private void requireCategory(Handle handle, Transaction it) {
        final Category category = categories.find(handle, it.categoryId)
                .orElseThrow(() -> new IllegalArgumentException("category %d not found".formatted(it.categoryId)));

        if (category.operation != it.operation) {
            throw new IllegalArgumentException("category does not match the transaction operation");
        }
    }

    public Optional<Transaction> find(Handle handle, long id) {
        return handle.createQuery("SELECT * FROM transactions WHERE id = :id")
                .bind("id", id)
                .mapTo(Transaction.class)
                .findOne();
    }

    public List<Transaction> list(Handle handle, LocalDate begin, LocalDate end) {
        final String q = """
            SELECT * FROM transactions WHERE issued BETWEEN :begin AND :end ORDER BY issued DESC
        """;

        return handle.createQuery(q)
                .bind("begin", begin)
                .bind("end", end)
                .mapTo(Transaction.class)
                .list();
    }

    public void delete(Handle handle, long id) {
        handle.createUpdate("DELETE FROM transactions WHERE id = :id")
                .bind("id", id)
                .execute();
    }
}
