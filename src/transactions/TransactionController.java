package transactions;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.jdbi.v3.core.Handle;

import categories.CategoryRepository;
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
    private final CategoryRepository categories;

    @Inject
    public TransactionController(TransactionRepository repository, CategoryRepository categories) {
        this.repository = repository;
        this.categories = categories;
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

        TransactionValidation.validateForCreate(handle, categories, transaction);

        return repository.save(handle, transaction);
    }

    @PUT("/transaction")
    public Transaction update(Context ctx) {
        final Handle handle = ctx.require(Handle.class);
        final Transaction transaction = ctx.body(Transaction.class);

        TransactionValidation.validateForUpdate(handle, categories, transaction);

        repository.find(handle, transaction.id).orElseThrow(NoSuchElementException::new);

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
