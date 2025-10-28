package finance.web.hx.forms;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.guice.routing.Route;
import finance.web.forms.ChainQ;
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
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        if (req.getParameter("modal") != null)
            engine.process("modal-income-form", new Context(), resp.getWriter());
        else
            part.send(req, resp);
    }
}
