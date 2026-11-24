package transactions;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.jdbi.v3.core.Handle;

import io.jooby.Context;
import io.jooby.annotation.DELETE;
import io.jooby.annotation.GET;
import io.jooby.annotation.POST;
import io.jooby.annotation.PUT;
import io.jooby.annotation.Path;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
@Path("/api")
public class TransactionController {

    private final TransactionRepository repository;

    @Inject
    public TransactionController(TransactionRepository repository) {
        this.repository = repository;
    }

    @GET("/transactions")
    public List<Transaction> list(Context ctx) {
        final Handle handle = ctx.require(Handle.class);
        final LocalDate begin = ctx.query("begin").to(LocalDate.class);
        final LocalDate end = ctx.query("end").to(LocalDate.class);

        return repository.list(handle, begin, end);
    }

    @POST("/transaction")
    public Transaction create(Context ctx) {
        final Handle handle = ctx.require(Handle.class);
        final Transaction transaction = ctx.body(Transaction.class);

        if (transaction.id != null) {
            throw new IllegalArgumentException("id must not be set when creating a transaction");
        }

        return repository.save(handle, transaction);
    }

    @PUT("/transaction")
    public Transaction update(Context ctx) {
        final Handle handle = ctx.require(Handle.class);
        final Transaction transaction = ctx.body(Transaction.class);

        if (transaction.id == null) {
            throw new IllegalArgumentException("id is required to update a transaction");
        }

        return repository.save(handle, transaction);
    }

    @DELETE("/transaction/{id}")
    public void delete(Context ctx) {
        final Handle handle = ctx.require(Handle.class);
        final Long id = ctx.path("id").to(long.class);

        repository.find(handle, id).orElseThrow(NoSuchElementException::new);

        repository.delete(handle, id);
    }
}
