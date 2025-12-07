package finance.web.hx.forms;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.javalite.json.JSONHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.guice.routing.Route;
import finance.objects.ExpenseCard;
import finance.objects.MonthlyExpense;
import finance.web.forms.ChainQ;
import finance.web.forms.Form;
import finance.web.forms.FormField;
import finance.web.forms.IntegerBtQ;
import finance.web.forms.IntegerQ;
import finance.web.forms.NonNegativeNumberQ;
import finance.web.forms.NumberQ;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
@Route("/hx/forms/income")
public class IncomeFormResource extends HttpServlet {
    private static final String TEMPLATE = "modal-income-form";
    private static final Set<String> FORM_FRAGMENT = Set.of("income-form");

    private final FormField<Integer> incomeYear = new FormField<>(
            "incomeYear",
            new ChainQ<>(
                    new IntegerQ(),
                    new IntegerBtQ(1990, Integer.MAX_VALUE)));

    private final FormField<Integer> incomeMonth = new FormField<>(
            "incomeMonth",
            new ChainQ<>(
                    new IntegerQ(),
                    new IntegerBtQ(1 + Calendar.JANUARY, 1 + Calendar.DECEMBER)));

    private final FormField<BigDecimal> incomeAmount = new FormField<>(
            "incomeAmount",
            new ChainQ<>(new NumberQ(), new NonNegativeNumberQ()));

    private final TemplateEngine engine;
    private final FormPart part;

    @Inject
    public IncomeFormResource(final TemplateEngine engine) {
        this.engine = engine;

        part = new FormPart(engine, "modal-income-form")
                .match()
                .when(incomeYear).fragment("income-year-detail")
                .when(incomeMonth).fragment("income-month-detail")
                .when(incomeAmount).fragment("income-amount-detail")
                .end();
    }

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        if (req.getParameter("modal") != null)
            engine.process(TEMPLATE, new Context(), resp.getWriter());
        else
            part.send(req, resp);
    }

    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final Form f = new Form(req, incomeYear, incomeMonth, incomeYear);
        final Context c = new Context(null, Map.of("form", f));
        final Writer w = resp.getWriter();

        if (!f.failedQ()) {
            try {
                final long contactId = 1L;
                final int year = f.value(incomeYear.name(), Integer.class);
                final int month = f.value(incomeMonth.name(), Integer.class);

                final List<MonthlyExpense> r = MonthlyExpense.where(
                        "YEAR(month_date) = ?1 AND MONTH(month_date) = ?2 AND contact_id = ?3",
                        year,
                        month,
                        contactId);

                final MonthlyExpense m = r.getFirst();

                // if (card.saveIt())
                //     onsuccess(req, resp);
                // else
                //     onerror(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
                onerror(req, resp);
            }
        } else
            engine.process(TEMPLATE, FORM_FRAGMENT, c, w);
    }

    private final void onsuccess(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getHeader("HX-Current-URL"));
    }

    private final void onerror(final HttpServletRequest req, final HttpServletResponse resp) {
        final String events = JSONHelper.toJSON("modal:close", "",
                "toastify:warn", Map.of("message", "Server Error. The income isn't received."));

        resp.setHeader("HX-Reswap", "none");
        resp.setHeader("HX-Trigger", events);
    }
}
