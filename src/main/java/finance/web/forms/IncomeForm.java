package finance.web.forms;

import static java.lang.String.valueOf;

import java.math.BigDecimal;
import java.time.LocalDate;

import finance.forms.BaseForm;
import finance.forms.FormField;
import finance.forms.rules.impl.DecimalValue;
import finance.forms.rules.impl.IntegerValue;
import jakarta.servlet.http.HttpServletRequest;

public final class IncomeForm extends BaseForm {
    private final LocalDate now = LocalDate.now();

    private FormField<BigDecimal> incomeAmount = new FormField<>("incomeAmount",
            new DecimalValue().min(BigDecimal.valueOf(0.01)));

    private FormField<Integer> incomeYear = new FormField<>(
            "incomeYear",
            valueOf(now.getYear()),
            new IntegerValue().min(2000));

    private FormField<Integer> incomeMonth = new FormField<>(
            "incomeMonth",
            valueOf(now.getMonthValue()),
            new IntegerValue().min(1).max(12));

    public IncomeForm() {
        register(incomeAmount, incomeYear, incomeMonth);
    }

    public IncomeForm(final HttpServletRequest req) {
        this();
        match(req);
    }

    public LocalDate incomeDate() {
        final Integer year = incomeYear.validationResult().value();
        final Integer month = incomeMonth.validationResult().value();

        if (null == year || null == month)
            return null;

        return LocalDate.of(year, month, 1);
    }

    public BigDecimal incomeAmount() {
        return incomeAmount.validationResult().value();
    }
}
