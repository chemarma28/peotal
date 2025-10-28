package finance.web.hx.forms;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.guice.routing.Route;
import finance.objects.ExpenseCard;
import finance.web.forms.ChainQ;
import finance.web.forms.EmptyRule;
import finance.web.forms.Form;
import finance.web.forms.FormField;
import finance.web.forms.LocalDateQ;
import finance.web.forms.NonNegativeNumberQ;
import finance.web.forms.NumberQ;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
@Route("/hx/forms/card")
public class ExpenseCardFormResource extends HttpServlet {
    private static final String TEMPLATE = "modal-card-form";
    private static final Set<String> FORM_FRAGMENT = Set.of("card-form");

    private final FormField<BigDecimal> cardAmount = new FormField<>(
            "cardAmount",
            new ChainQ<>(new NumberQ(), new NonNegativeNumberQ()));

    private final FormField<LocalDate> cardDate = new FormField<>("cardDate", new LocalDateQ());

    private final FormField<String> cardDescription = new FormField<>("cardDescription", new EmptyRule());

    private final TemplateEngine engine;
    private final FormPart part;

    @Inject
    public ExpenseCardFormResource(final TemplateEngine engine) {
        this.engine = engine;

        part = new FormPart(engine, "modal-card-form").match()
                .when(cardAmount).fragment("card-amount-detail")
                .when(cardDate).fragment("card-date-detail")
                .when(cardDescription).fragment("card-description-detail")
                .end();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("modal") != null)
            engine.process(TEMPLATE, new Context(), resp.getWriter());
        else
            part.send(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Form f = new Form(req, cardAmount, cardDate, cardDescription);
        final Context c = new Context(null, Map.of("form", f));
        final Writer w = resp.getWriter();

        if (f.failedQ()) {
            engine.process(TEMPLATE, FORM_FRAGMENT, c, w);

            return;
        }

        ExpenseCard card = new ExpenseCard(1L,
                f.value("cardAmount", BigDecimal.class),
                f.value("cardDate", LocalDate.class),
                f.rawValue("cardDescription"));

        try {
            if (card.saveIt())
                onsuccess(req, resp);
            else
                onerror(req, resp);
        } catch (Exception e) {
            onerror(req, resp);
        }
    }

    private final void onsuccess(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getHeader("HX-Current-URL"));
    }

    private final void onerror(final HttpServletRequest req, final HttpServletResponse resp) {
        final JSONObject message = new JSONObject(Map.of("message", "Server Error. The expense card wasn't saved"));
        final JSONObject events = new JSONObject(Map.of("modal:close", "", "toastify:warn", message));

        resp.setHeader("HX-Reswap", "none");
        resp.setHeader("HX-Trigger", events.toJSONString());
    }
}
