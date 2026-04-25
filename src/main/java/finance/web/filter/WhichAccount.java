package finance.web.filter;

import static finance.service.AuthService.SCOPE_ID;

import java.io.IOException;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.objects.Account;
import finance.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
public class WhichAccount extends HttpFilter {
    @Inject
    private AuthService authService;

    @Override
    public void doFilter(final HttpServletRequest req, final HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        final String scopeId = scopeId(req, resp);

        if (null == scopeId) {
            resp.sendRedirect("/hello.html");
            return;
        }

        final Account account = authService.associated(scopeId);

        if (null == account) {
            resp.sendRedirect("/hello.html");
            return;
        }

        req.setAttribute("account", account);
        chain.doFilter(req, resp);
    }

    private String scopeId(final HttpServletRequest req, final HttpServletResponse resp) {
        final Cookie[] cookies = req.getCookies();

        for (final Cookie cookie : cookies)
            if (SCOPE_ID.equals(cookie.getName()))
                return cookie.getValue();

        return null;
    }
}
