package transactions;

import static java.lang.String.format;
import java.time.LocalDate;
import java.util.List;

import org.jdbi.v3.core.Handle;

import jakarta.inject.Singleton;

@Singleton
public class TransactionRepository {

    public Transaction save(Handle handle, Transaction it) {
        if (it.issued.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(format("Transaction date can not be in the future"));
        }

        final String q = """
            INSERT INTO transactions (description, issued, operation, category_id, amount)
            VALUES (:description, :issued, :operation, :categoryId, :amount)
        """;

        it.id = handle.createUpdate(q)
                .bindBean(it)
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Long.class)
                .one();

        return it;
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
