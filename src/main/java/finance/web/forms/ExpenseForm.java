package finance.web.forms;

import java.math.BigDecimal;
import java.time.LocalDate;

import finance.forms.BaseForm;
import finance.forms.FormField;
import finance.forms.rules.impl.DecimalValue;
import finance.forms.rules.impl.LocalDateValue;
import finance.forms.rules.impl.Text;
import jakarta.servlet.http.HttpServletRequest;

public final class ExpenseForm extends BaseForm {
    private final FormField<BigDecimal> cardAmount = new FormField<>("cardAmount", new DecimalValue().min(BigDecimal.valueOf(0.01)));

    private final FormField<LocalDate> cardDate = new FormField<>(
            "cardDate",
            LocalDate.now().toString(),
            new LocalDateValue());

    private final FormField<String> cardDescription = new FormField<>("cardDescription", new Text()).optional("");

    public ExpenseForm() {
        register(cardAmount, cardDate, cardDescription);
    }

    public ExpenseForm(final HttpServletRequest req) {
        this();
        match(req);
    }

    public BigDecimal cardAmount() {
        return cardAmount.validationResult().value();
    }

    public LocalDate cardDate() {
        return cardDate.validationResult().value();
    }

    public String cardDescription() {
        return cardDescription.validationResult().value();
    }
}
