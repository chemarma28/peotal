package finance.web.filter;

import static java.lang.Long.parseLong;

import java.io.IOException;

import com.google.inject.Singleton;

import finance.objects.Account;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

@Singleton
public class WhichAccount implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        final String param = req.getParameter("account-id");

        // This is a stub. Later I provide real authentication
        // and provide current account of an user
        long accountId = null == param ? 1L : parseLong(param);

        final Account account = Account.findById(accountId);
        req.setAttribute("account", account);

        chain.doFilter(req, resp);
    }
}
