package categories;

import static java.lang.String.format;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.jdbi.v3.core.Handle;

public final class CategoryValidation {

    private static final Pattern COLOR = Pattern.compile("^#[0-9a-fA-F]{6}$");
    private static final Pattern ICON = Pattern.compile("^[a-z0-9-]{1,64}$");

    private CategoryValidation() {
    }

    public static void validateForCreate(Handle handle, CategoryRepository repository, Category category) {
        if (category.id != null) {
            throw new IllegalArgumentException("id must not be set when creating a category");
        }

        validate(handle, repository, category, null);
    }

    public static void validateForBatch(Handle handle, CategoryRepository repository, List<Category> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("items are required");
        }

        final Set<String> names = new HashSet<>();

        for (final Category category : items) {
            if (category == null) {
                throw new IllegalArgumentException("category must not be null");
            }

            validateForCreate(handle, repository, category);

            final String key = category.operation.name() + ":" + category.name;

            if (!names.add(key)) {
                throw new IllegalArgumentException(format(
                        "category '%s' is duplicated in the batch for %s", category.name, category.operation));
            }
        }
    }

    public static void validateForUpdate(Handle handle, CategoryRepository repository, Category category, Category existing) {
        validate(handle, repository, category, category.id);

        if (existing.operation != category.operation && repository.countTransactions(handle, category.id) > 0) {
            throw new IllegalArgumentException("operation cannot be changed while the category is used by transactions");
        }
    }

    private static void validate(Handle handle, CategoryRepository repository, Category category, Long excludeId) {
        if (category.name == null || category.name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }

        category.name = category.name.trim();

        if (category.name.length() > 64) {
            throw new IllegalArgumentException("name must be at most 64 characters");
        }

        if (category.operation == null) {
            throw new IllegalArgumentException("operation is required");
        }

        if (category.color == null || !COLOR.matcher(category.color).matches()) {
            throw new IllegalArgumentException("color must be in #RRGGBB format");
        }

        if (category.icon != null && !ICON.matcher(category.icon).matches()) {
            throw new IllegalArgumentException("icon must match [a-z0-9-]{1,64}");
        }

        if (category.ordinal != null && category.ordinal < 0) {
            throw new IllegalArgumentException("ordinal must not be negative");
        }

        if (repository.existsByName(handle, category.operation, category.name, excludeId)) {
            throw new IllegalArgumentException(format("category '%s' already exists for %s", category.name, category.operation));
        }
    }
}
