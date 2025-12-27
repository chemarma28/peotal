package finance.forms.rules.impl;

import static finance.forms.rules.ValidationResult.validationNegativeResult;
import static finance.forms.rules.ValidationResult.validationPositiveResult;
import static java.util.Collections.singletonList;

import java.time.LocalDate;

import finance.forms.rules.FieldRule;
import finance.forms.rules.ValidationResult;

public class LocalDateValue implements FieldRule<String, LocalDate> {
    @Override
    public ValidationResult<LocalDate> apply(String value) {
        try {
            final LocalDate val = LocalDate.parse(value);

            return validationPositiveResult(val);
        } catch (Exception e) {
            return validationNegativeResult("Please provide a date.", singletonList(e.getMessage()));
        }
    }
}
