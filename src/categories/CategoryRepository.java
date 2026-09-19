package categories;

import java.util.List;

import org.jdbi.v3.core.Handle;

import jakarta.inject.Singleton;

@Singleton
public class CategoryRepository {

    public List<Category> list(Handle h) {
        final String q = """
            SELECT id, name FROM categories
        """;

        return h.createQuery(q).mapTo(Category.class).collectIntoList();
    }
}
