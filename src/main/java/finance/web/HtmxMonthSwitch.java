package finance.web;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.guice.routing.Route;
import finance.guice.routing.UriTemplate;
import finance.service.MonthExpenses;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
@Route("/htmx/year/{year}/month/{month}")
public class HtmxMonthSwitch extends HttpServlet {
    private UriTemplate uri;

    @Inject
    private TemplateEngine te;
    @Inject
    private MonthExpenses monthExpenses;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context ctx = new Context();

        Map<String, String> pathParams = uri.match(req.getServletPath());

        int year = Integer.parseInt(pathParams.get("year"));
        int month = Integer.parseInt(pathParams.get("month"));

        resp.setHeader("HX-Trigger-After-Swap", "clientInitAccordion");

        ctx.setVariable("cardItemPacks", monthExpenses.pack(monthExpenses.monthly(year, month)));

        String output = te.process("dashboard", Set.of("cardItemPacks"), ctx);

        resp.getWriter().write(output);
    }

}
