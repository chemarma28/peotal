package finance.web;

import static finance.service.Formats.fbrowser;

import java.io.IOException;
import java.time.LocalDate;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.Context;

import com.google.inject.Singleton;

import finance.forms.Form;
import finance.forms.TemplateForm;
import finance.guice.modules.HttpServletPath;
import finance.objects.Account;
import finance.objects.IncomeCard;
import finance.web.forms.FormServlet;
import finance.web.forms.IncomeCardForm;
import finance.web.html.AccountServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
@HttpServletPath("/secure/income.html")
public class IncomeFormServlet extends FormServlet {
    private static final Logger logger = LoggerFactory.getLogger(IncomeFormServlet.class);

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final Form form = new TemplateForm().ordinary("when", fbrowser(LocalDate.now()));
        final Context ctx = new Context();

        html(form, ctx, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        final IncomeCardForm form = new IncomeCardForm(req);
        final Context ctx = new Context();

        if (super.rejectedOnConflicts(form, ctx, resp))
            return;

        final Account owner = (Account) req.getAttribute("account");

        final Supplier<Boolean> callback = () -> {
            final IncomeCard card;

            try {
                card = owner.topUp(form.description(), form.when(), form.amount());
            } catch (Exception e) {
                logger.error("Couldn't handle {} due to unknown errors:", form, e);

                return true;
            }

            return null == card || card.hasErrors();
        };

        if (super.rejectedOnInternalErrors(callback, form, ctx, resp))
            return;

        resp.setStatus(201);
        resp.setHeader("Location", AccountServlet.resourceUrl(form.when().getYear(), form.when().getMonthValue()));
    }

    @Override
    protected String template() {
        return "card-form";
    }
}
