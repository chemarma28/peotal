package categories;

import static java.lang.String.format;

public class CategoryInUseException extends RuntimeException {

    public CategoryInUseException(long id) {
        super(format("Category %d is used by transactions and cannot be deleted", id));
    }
}
