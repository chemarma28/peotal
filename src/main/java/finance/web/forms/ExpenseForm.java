package finance.web.forms;

import java.math.BigDecimal;
import java.time.LocalDate;

import finance.forms.*;
import finance.forms.Number;

public final class ExpenseForm extends SimpleForm {
    public ExpenseForm() {
        this.field("amount", new Required<>(new Number()));
        this.field("date", new Required<>(new Date()));
        this.field("description", new Text());
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
