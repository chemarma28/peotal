package finance.web.html;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import finance.guice.modules.HttpServletPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;

@Singleton
@HttpServletPath("/account.html")
public class Account extends HttpServlet {
    @Inject
    private TemplateEngine te;

    private static final int YEAR;
    private static final int MONTH;

    static {
        final LocalDate NOW = LocalDate.now();
        YEAR = NOW.getYear();
        MONTH = NOW.getMonthValue();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final int y = year(req);
        final int m = month(req);

        noParams(req, resp, y, m);

        final Context thymeleaf = new Context(Locale.getDefault(), Map.of());

        te.process("account", thymeleaf ,resp.getWriter());
    }

    private void noParams(final HttpServletRequest req, final HttpServletResponse resp, int year, int month) throws IOException {
        if (null != req.getParameter("y") && null != req.getParameter("m"))
            return;

        resp.sendRedirect(format("/account.html?y=%d&m=%d", year, month));
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
