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

    @Override
    public ValidationResult<BigDecimal> apply(String value) {
        try {
            final BigDecimal val = new BigDecimal(value);

            if (null != min && 0 < val.compareTo(min) || null != max && 0 < max.compareTo(val))
                return validationNegativeResult(format("Please provide a decimal value in range from %s to %s",
                        numberFormat.format(min), numberFormat.format(max)));

            return validationPositiveResult(val);
        } catch (Exception e) {
            return validationNegativeResult("Please provide a decimal value.", singletonList(e.getMessage()));
        }
    }
}
