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
import finance.records.MonthlyInfo;
import finance.service.AvailableYears;
import finance.service.CardFolderFetch;
import finance.service.MonthlyInfoFetch;
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

        final List<MonthlyInfo> infos = new MonthlyInfoFetch().retrieve(year);

        if (infos.size() < 0)
            resp.sendRedirect(String.format("/404.html", year, month));

        final List<CardFolder> folders = new CardFolderFetch().retrieve(year, month);
        final List<Integer> availableYears = new AvailableYears().retrieve();

        Context thymeleaf = new Context(null,
                Map.of("yearParam", year, "monthParam", month, "infos", infos, "folders", folders,
                        "availableYears", availableYears));

        te.process("dashboard", thymeleaf, resp.getWriter());
    }
}
