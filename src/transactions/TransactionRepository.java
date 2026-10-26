package transactions;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.jdbi.v3.core.Handle;

import jakarta.inject.Singleton;

@Singleton
public class TransactionRepository {

    public Transaction save(Handle handle, Transaction it) {
        if (it.id == null) {
            final String q = """
                INSERT INTO transactions (description, issued, operation, category_id, amount, currency)
                VALUES (:description, :issued, :operation, :categoryId, :amount, :currency)
            """;

            it.id = handle.createUpdate(q)
                    .bindBean(it)
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Long.class)
                    .one();
        } else {
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
        }

        return it;
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
}
