package finance.web.html;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.guice.modules.HttpServletPath;
import finance.records.CardFolder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
@HttpServletPath("/dashboard.html")
public class DashboardResource extends HttpServlet {
    @Inject
    private TemplateEngine te;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String yearParam = req.getParameter("year");
        final String monthParam = req.getParameter("month");
        final LocalDate NOW = LocalDate.now();

        final int year = yearParam == null ? NOW.getYear() : Integer.parseInt(yearParam);
        final int month = monthParam == null ? NOW.getMonthValue() : Integer.parseInt(monthParam);

        if (req.getHeader("HX-Request") != null)
            resp.setHeader("HX-Redirect", String.format("/dashboard.html?year=%s&month=%s", year, month));

        if (yearParam == null || monthParam == null)
            resp.sendRedirect(String.format("/dashboard.html?year=%s&month=%s", year, month));

        te.process("dashboard", new Context(), resp.getWriter());
    }
}
