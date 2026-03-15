package finance.web.forms;

import jakarta.servlet.http.HttpServletRequest;

public final class ExpenseCardForm extends CardForm {
    public ExpenseCardForm(final HttpServletRequest req) {
        super(req);
    }
}
