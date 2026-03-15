package finance.web.forms;

import static finance.service.Formats.fdate;
import static finance.service.Formats.fdecimal;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.abbreviate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import finance.forms.Date;
import finance.forms.Mapping;
import finance.forms.Number;
import finance.forms.Required;
import finance.forms.SimpleForm;
import finance.forms.Text;
import jakarta.servlet.http.HttpServletRequest;

public class CardForm extends SimpleForm {
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

    protected CardForm() {
        this.field("amount", new Required<>(Amount));
        this.field("when", new Required<>(new Date()));
        this.field("description", new Text(4));
    }

    public CardForm(final HttpServletRequest req) {
        this();
        bindFromRequest(req);
    }

    public BigDecimal amount() {
        return this.value("amount", BigDecimal.class);
    }

    public LocalDateTime when() {
        return LocalDateTime.of(this.value("when", LocalDate.class), LocalTime.now());
    }

    public String description() {
        return this.value("description", String.class);
    }

    @Override
    public String toString() {
        return format("Form[date='%s', amount='%s', description='%s']",
                fdate(this.when()),
                fdecimal(this.amount()),
                abbreviate(this.description(), 10));
    }
}
