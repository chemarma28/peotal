package finance.service;

import org.javalite.activejdbc.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import finance.objects.Account;
import finance.objects.ExpenseCard;
import finance.objects.IncomeCard;
import finance.web.forms.ExpenseCardForm;
import finance.web.forms.IncomeCardForm;

@Singleton
public class CardService {
    private static final Logger logger = LoggerFactory.getLogger(CardService.class);

    public IncomeCard newIncomeCard(final Account owner, final IncomeCardForm form) {
        if (null == form || form.hasErrors())
            return null;

        final IncomeCard card = new IncomeCard(owner, form.when(), form.amount(), form.description());

        if (!saveWithVerbose(card))
            return null;

        logValidationErrorsIfAny(card);

        return card;
    }

    public ExpenseCard newExpenseCard(final Account owner, final ExpenseCardForm form) {
        if (form.hasErrors())
            return null;

        final ExpenseCard card = new ExpenseCard(owner, form.when(), form.amount(), form.description());

        if (!saveWithVerbose(card))
            return null;

        logValidationErrorsIfAny(card);

        return card;
    }

    private boolean saveWithVerbose(final Model model) {
        logger.debug("Preparing saving {}", model);

        boolean done = false;

        try {
            done = model.save();
            logger.debug("Saving done {}", model);
        } catch (Exception e) {
            logger.error("Saving {} failed due DB errors followed by {}", model, e.getMessage());
            return false;
        }

        return done;
    }

    private void logValidationErrorsIfAny(final Model model) {
        if (!model.hasErrors())
            return;

        logger.error("Active JDBC errors occured for {}:", model);
        logger.error("{}", model.errors());
    }
}
