package categories;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
import transactions.TransactionType;

@Singleton
@Path("/api")
public class CategoryController {

    private final CategoryRepository repository;

    @Inject
    public CategoryController(CategoryRepository repository) {
        this.repository = repository;
    }

    @GET("/categories")
    public List<Category> list(Context ctx) {
        final Handle handle = ctx.require(Handle.class);
        final TransactionType operation = ctx.query("operation").toOptional(TransactionType.class).orElse(null);

        return operation == null ? repository.listAll(handle) : repository.listByOperation(handle, operation);
    }

    @POST("/category")
    public Category create(Context ctx) {
        final Handle handle = ctx.require(Handle.class);
        final Category category = ctx.body(Category.class);

        CategoryValidation.validateForCreate(handle, repository, category);

        return repository.save(handle, category);
    }

    @POST("/category/batch")
    public List<Category> createBatch(Context ctx) {
        final Handle handle = ctx.require(Handle.class);
        final CategoryBatch batch = ctx.body(CategoryBatch.class);

        if (batch == null) {
            throw new IllegalArgumentException("body is required");
        }

        CategoryValidation.validateForBatch(handle, repository, batch.items);

        return repository.saveAll(handle, batch.items);
    }

    @PUT("/category")
    public Category update(Context ctx) {
        final Handle handle = ctx.require(Handle.class);
        final Category category = ctx.body(Category.class);

        if (category.id == null) {
            throw new IllegalArgumentException("id is required to update a category");
        }

        final Category existing = repository.find(handle, category.id).orElseThrow(NoSuchElementException::new);

        CategoryValidation.validateForUpdate(handle, repository, category, existing);

        return repository.save(handle, category);
    }

    @PUT("/category/order")
    public List<Category> reorder(Context ctx) {
        final Handle handle = ctx.require(Handle.class);
        final CategoryOrder order = ctx.body(CategoryOrder.class);

        if (order.items == null) {
            throw new IllegalArgumentException("items are required");
        }

        repository.reorder(handle, order.items);

        return repository.listAll(handle);
    }

    @DELETE("/category/{id}")
    public Map<String, Object> delete(Context ctx) {
        final Handle handle = ctx.require(Handle.class);
        final long id = ctx.path("id").to(long.class);

        repository.find(handle, id).orElseThrow(NoSuchElementException::new);

        if (repository.countTransactions(handle, id) > 0) {
            throw new CategoryInUseException(id);
        }

        repository.delete(handle, id);

        return Map.of("id", id);
    }

    @GET("/categories/summary")
    public List<CategorySummary> geCategorySummaries(Context ctx) {
        final Handle handle = ctx.require(Handle.class);
        final LocalDate begin = ctx.query("begin").to(LocalDate.class);
        final LocalDate end = ctx.query("end").to(LocalDate.class);

        return repository.getCategorySummaries(handle, begin, end);
    }
}
