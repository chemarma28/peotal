package finance.web.filter;

import com.google.inject.Singleton;
import finance.objects.Account;
import jakarta.servlet.*;

import java.io.IOException;

import static java.lang.Long.parseLong;

@Singleton
public class WhichAccount implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        final String param = req.getParameter("account-id");

        long accountId = null == param ? 1L : parseLong(param);

        final Account account = Account.findById(accountId);
        req.setAttribute("account", account);

        chain.doFilter(req, resp);
    }
}
