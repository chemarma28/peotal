package finance.forms.rules.impl;

import static finance.forms.rules.ValidationResult.validationNegativeResult;
import static finance.forms.rules.ValidationResult.validationPositiveResult;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

import java.math.BigDecimal;
import java.text.NumberFormat;

import finance.forms.rules.FieldRule;
import finance.forms.rules.ValidationResult;

public class DecimalValue implements FieldRule<String, BigDecimal> {
    private static NumberFormat numberFormat;

    static {
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(4);
    }

    private BigDecimal min;
    private BigDecimal max;

    public DecimalValue() {
        this.min = null;
        this.max = null;
    }

    public DecimalValue(final BigDecimal min, final BigDecimal max) {
        this.min = min;
        this.max = max;
    }

    public DecimalValue min(final BigDecimal min) {
        this.min = min;

        return this;
    }

    public DecimalValue max(final BigDecimal max) {
        this.max = max;

        return this;
    }

    private String message() {
        final String min = this.min == null ? null : numberFormat.format(this.min);
        final String max = this.max == null ? null : numberFormat.format(this.max);

        if (null != min && null != max)
            return format("Please provide a decimal in range from %s to %s.", min, max);
        else if (null != min)
            return format("Please provide a decimal greater than or equal to %s.", min);
        else if (null != max)
            return format("Please provide a decimal less than or equal to %s.", max);
        else
            return "Please provide a decimal.";
    }

    @Override
    public ValidationResult<BigDecimal> apply(String value) {
        try {
            final BigDecimal val = new BigDecimal(value);

            if (null != min && val.compareTo(min) < 0 || null != max && max.compareTo(val) < 0)
                return validationNegativeResult(message());

            return validationPositiveResult(val);
        } catch (Exception e) {
            return validationNegativeResult(message(), singletonList(e.getMessage()));
        }
    }
}
