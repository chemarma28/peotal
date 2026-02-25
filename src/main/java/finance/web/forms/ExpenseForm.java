package finance.web.forms;

import java.math.BigDecimal;
import java.time.LocalDate;

import finance.forms.Date;
import finance.forms.SimpleForm;
import finance.forms.Number;
import finance.forms.Required;

public final class ExpenseForm extends SimpleForm {
    public ExpenseForm() {
        this.field("amount", new Required<>(new Number()));
        this.field("date", new Required<>(new Date()));
    }

    public BigDecimal cardAmount() {
        return this.value("amount", BigDecimal.class);
    }

    public LocalDate cardDate() {
        return this.value("date", LocalDate.class);
    }

    public String cardDescription() {
        return "";
    }
}
