package categories;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.jdbi.v3.core.Handle;

import transactions.TransactionType;

import jakarta.inject.Singleton;

@Singleton
public class CategoryRepository {

    public Category save(Handle handle, Category category) {
        if (category.id == null) {
            if (category.ordinal == null) {
                category.ordinal = nextOrdinal(handle);
            }

            final String q = """
                INSERT INTO categories (name, operation, color, icon, ordinal)
                VALUES (:name, :operation, :color, :icon, :ordinal)
            """;

            category.id = handle.createUpdate(q)
                    .bindBean(category)
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Long.class)
                    .one();
        } else {
            if (category.ordinal == null) {
                category.ordinal = find(handle, category.id).map(it -> it.ordinal).orElse(0);
            }

            final String q = """
                UPDATE categories
                SET name = :name, operation = :operation, color = :color, icon = :icon, ordinal = :ordinal
                WHERE id = :id
            """;

            handle.createUpdate(q).bindBean(category).execute();
        }

        return category;
    }

    public List<Category> saveAll(Handle handle, List<Category> categories) {
        for (final Category category : categories) {
            save(handle, category);
        }

        return categories;
    }

    public List<Category> listAll(Handle handle) {
        return list(handle, null);
    }

    public List<Category> listByOperation(Handle handle, TransactionType operation) {
        Objects.requireNonNull(operation, "operation is required");

        return list(handle, operation);
    }

    private List<Category> list(Handle handle, TransactionType operation) {
        if (operation == null) {
            return handle.createQuery("""
                        SELECT id, name, operation, color, icon, ordinal
                        FROM categories
                        ORDER BY ordinal, name
                    """)
                    .mapTo(Category.class)
                    .list();
        }

        return handle.createQuery("""
                    SELECT id, name, operation, color, icon, ordinal
                    FROM categories
                    WHERE operation = :operation
                    ORDER BY ordinal, name
                """)
                .bind("operation", operation.name())
                .mapTo(Category.class)
                .list();
    }

    public Optional<Category> find(Handle handle, long id) {
        return handle.createQuery("""
                    SELECT id, name, operation, color, icon, ordinal
                    FROM categories
                    WHERE id = :id
                """)
                .bind("id", id)
                .mapTo(Category.class)
                .findOne();
    }

    public void delete(Handle handle, long id) {
        handle.createUpdate("DELETE FROM categories WHERE id = :id")
                .bind("id", id)
                .execute();
    }

    public void reorder(Handle handle, List<CategoryOrder.Item> items) {
        final var batch = handle.prepareBatch("UPDATE categories SET ordinal = :ordinal WHERE id = :id");

        for (final CategoryOrder.Item item : items) {
            batch.bind("id", item.id).bind("ordinal", item.ordinal).add();
        }

        batch.execute();
    }

    public long countTransactions(Handle handle, long id) {
        return handle.createQuery("SELECT COUNT(*) FROM transactions WHERE category_id = :id")
                .bind("id", id)
                .mapTo(Long.class)
                .one();
    }

    public boolean existsByName(Handle handle, TransactionType operation, String name, Long excludeId) {
        final long excluded = excludeId == null ? -1 : excludeId;

        return handle.createQuery("""
                    SELECT COUNT(*) FROM categories
                    WHERE operation = :operation AND name = :name AND id <> :excludeId
                """)
                .bind("operation", operation.name())
                .bind("name", name)
                .bind("excludeId", excluded)
                .mapTo(Long.class)
                .one() > 0;
    }

    private int nextOrdinal(Handle handle) {
        return handle.createQuery("SELECT COALESCE(MAX(ordinal), -1) + 1 FROM categories")
                .mapTo(Integer.class)
                .one();
    }
}
