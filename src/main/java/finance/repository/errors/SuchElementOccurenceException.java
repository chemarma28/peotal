package finance.repository.errors;

import finance.repository.Entity;

public class SuchElementOccurenceException extends Exception {
    private Entity<?> cause;

    public SuchElementOccurenceException(Entity<?> e) {
        cause = e;
    }

    @Override
    public String getMessage() {
        return String.format(
                "The entity with id='%s' of type '%s' is already stored. Use methods to update the storage.",
                cause.id(),
                cause.getClass().getName());
    }
}
