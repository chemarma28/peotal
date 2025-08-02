package finance.web;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.domain.MonthlyExpense;
import finance.guice.routing.Route;
import finance.service.AnnualMonthlyExpenses;
import finance.service.MonthExpenses;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
@Route("/html/dashboard")
public class Dashboard extends HttpServlet {
    @Inject
    private TemplateEngine te;
    @Inject
    private AnnualMonthlyExpenses annualMonthlyExpenses;
    @Inject
    private MonthExpenses monthExpenses;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String yearParam = req.getParameter("year");
        final String monthParam = req.getParameter("month");

        final int year = yearParam == null ? LocalDate.now().getYear() : Integer.parseInt(yearParam);
        int month = monthParam == null ? LocalDate.now().getMonthValue() : Integer.parseInt(monthParam);

        month = month < 1 || 12 < month ? LocalDate.now().getMonthValue() : month;

        if (yearParam == null || monthParam == null)
            resp.sendRedirect(String.format("/html/dashboard?year=%s&month=%s&set=true", year, month));

        Context ctx = new Context();

        List<MonthlyExpense> annual = annualMonthlyExpenses.filterAndSort(2025);

        ctx.setVariables(Map.of(
                "monthItems", annualMonthlyExpenses.annual(annual),
                "monthTotalItem", annualMonthlyExpenses.sum(annual),
                "cardItemPacks", monthExpenses.pack(monthExpenses.monthly(2025, 5)),
                "year", year,
                "month", month));

        String output = te.process("dashboard", ctx);

        resp.getWriter().write(output);
    }
}
