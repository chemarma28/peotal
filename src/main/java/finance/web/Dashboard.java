package finance.web;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.guice.routing.Route;
import finance.service.DashboardService;
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
    private DashboardService service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String yearParam = req.getParameter("year");
        final String monthParam = req.getParameter("month");
        final LocalDate NOW = LocalDate.now();

        final int year = yearParam == null ? NOW.getYear() : Integer.parseInt(yearParam);
        final int month = monthParam == null ? NOW.getMonthValue() : Integer.parseInt(monthParam);

        if (req.getHeader("HX-Request") != null)
            resp.setHeader("HX-Redirect", String.format("/html/dashboard?year=%s&month=%s", year, month));

        if (yearParam == null || monthParam == null)
            resp.sendRedirect(String.format("/html/dashboard?year=%s&month=%s", year, month));

        Context thymeleaf = new Context(null, Map.of(
                "monthParam", month,
                "infos", service.collectMonthlyExpenses(year),
                "folders", service.collectExpenseCardsByDate(year, month)));

        te.process("dashboard", thymeleaf, resp.getWriter());
    }
}
