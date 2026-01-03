package finance.web.hx;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.javalite.json.JSONHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.guice.modules.HttpServletPath;
import finance.objects.ExpenseCard;
import finance.web.forms.ExpenseForm;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
@HttpServletPath("/hx/forms/card")
public class ExpenseCardFormResource extends HttpServlet {
    private static final String TEMPLATE = "modal-card-form";
    private static final Set<String> FORM_FRAGMENT = Set.of("card-form");
    private final TemplateEngine engine;

    @Inject
    public ExpenseCardFormResource(final TemplateEngine engine) {
        this.engine = engine;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final ExpenseForm form = new ExpenseForm();
        final Context ctx = new Context();
        ctx.setVariable("expenseCardForm", form);

        if (req.getParameter("modal") != null)
            engine.process(TEMPLATE, ctx, resp.getWriter());
        else
            engine.process(TEMPLATE, FORM_FRAGMENT, ctx, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final ExpenseForm form = new ExpenseForm(req);
        final Context ctx = new Context();
        ctx.setVariable("expenseCardForm", form);

        if (!form.isValid())
            engine.process(TEMPLATE, FORM_FRAGMENT, ctx, resp.getWriter());
        else {
            final ExpenseCard card = new ExpenseCard(1L, form.cardAmount(), form.cardDate(), form.cardDescription());

            try {
                if (card.save())
                    onsuccess(req, resp);
                else
                    onerror(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
                onerror(req, resp);
            }
        }
    }

    private final void onsuccess(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getHeader("HX-Current-URL"));
    }

    private final void onerror(final HttpServletRequest req, final HttpServletResponse resp) {
        final String events = JSONHelper.toJSON(
                "modal:close", "",
                "toastify:warn", Map.of("message", "Data not saved. Server error. Please try later."));

        resp.setHeader("HX-Reswap", "none");
        resp.setHeader("HX-Trigger", events);
    }
}
