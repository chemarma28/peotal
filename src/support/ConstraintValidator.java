package support;

import io.avaje.validation.ConstraintViolation;
import io.avaje.validation.ConstraintViolationException;
import io.avaje.validation.Validator;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ConstraintValidator {

    private final Validator validator;

    @Inject
    public ConstraintValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(Object bean, Class<?>... groups) {
        try {
            validator.validate(bean, groups);
        } catch (ConstraintViolationException e) {
            final String message = e.violations().stream().findFirst().map(ConstraintViolation::message)
                    .orElse("validation failed");

            throw new IllegalArgumentException(message, e);
        }
    }
}
