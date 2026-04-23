package finance.web.html;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Locale;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.guice.modules.HttpServletPath;
import finance.objects.DailyTransactions;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
@HttpServletPath("/secure/account.html")
public class AccountServlet extends HttpServlet {
    @Inject
    private TemplateEngine te;

    private static final int YEAR;
    private static final int MONTH;

    static {
        final LocalDate NOW = LocalDate.now();
        YEAR = NOW.getYear();
        MONTH = NOW.getMonthValue();
    }

    public static String resourceUrl() {
        return resourceUrl(null,null);
    }

    public static String resourceUrl(Integer year, Integer month) {
        final String url = "/secure/account.html";

        if (null == year || null == month)
            return url;

        return url + format("?y=%d&m=%d", year, month);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final int y = year(req);
        final int m = month(req);

        if (noParams(req, resp, y, m))
            return;

        final Context ctx = new Context(Locale.ENGLISH);

        ctx.setVariable("yearAndMonth", LocalDate.of(y, m, 1));
        ctx.setVariable("daily", new DailyTransactions(YearMonth.of(y, m)));

        te.process("account", ctx, resp.getWriter());
    }

    private boolean noParams(final HttpServletRequest req, final HttpServletResponse resp, int year, int month)
            throws IOException {
        if (null != req.getParameter("y") && null != req.getParameter("m"))
            return false;

        resp.sendRedirect(resourceUrl(year, month));

        return true;
    }

    private int year(final HttpServletRequest req) {
        final String param = req.getParameter("y");

        return null == param ? YEAR : parseInt(param);
    }

    private int month(final HttpServletRequest req) {
        final String param = req.getParameter("m");

        return null == param ? MONTH : parseInt(param);
    }
}
