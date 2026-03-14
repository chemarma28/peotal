package finance.web.forms;

import static finance.service.Formats.fdecimal;
import static java.lang.String.format;

import java.math.BigDecimal;
import java.time.LocalDate;

import finance.forms.*;
import finance.forms.Number;

public final class ExpenseForm extends SimpleForm {
    private static final Mapping<BigDecimal> Amount = (req, key, validation) -> {
        final Number number = new Number();

        final BigDecimal n = number.map(req, key, validation);

        if (null == n)
            return null;

        final BigDecimal threshold = BigDecimal.valueOf(0.01);

        if (threshold.compareTo(n) <= 0)
            return n;

        validation.addError(key, format("The value should be %s at least.", fdecimal(threshold)));

        return null;
    };

    public ExpenseForm() {
        this.field("amount", new Required<>(Amount));
        this.field("when", new Required<>(new Date()));
        this.field("description", new Text(4));
    }

    public BigDecimal cardAmount() {
        return this.value("amount", BigDecimal.class);
    }

    public LocalDate cardDate() {
        return this.value("date", LocalDate.class);
    }

    public String cardDescription() {
        return this.value("description", String.class);
    }
}
