package finance.forms.rules.impl;

import static finance.forms.rules.ValidationResult.validationNegativeResult;
import static finance.forms.rules.ValidationResult.validationPositiveResult;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

import finance.forms.rules.FieldRule;
import finance.forms.rules.ValidationResult;

public class IntegerValue implements FieldRule<String, Integer> {
    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;

    public IntegerValue min(final int min) {
        this.min = min;

        return this;
    }

    public IntegerValue max(final int max) {
        this.max = max;

        return this;
    }

    @Override
    public ValidationResult<Integer> apply(String value) {
        try {
            int v = Integer.valueOf(value);

            if (min <= v && v <= max)
                return validationPositiveResult(v);

            return validationNegativeResult(format("Integer must be in range from %s to %s", min, max));
        } catch (Exception e) {
            return validationNegativeResult("Please provide an integer.", singletonList(e.getMessage()));
        }
    }
}
