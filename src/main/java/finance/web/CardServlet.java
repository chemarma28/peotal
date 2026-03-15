package finance.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import finance.guice.modules.HttpServletPath;
import finance.objects.Account;
import finance.objects.FinanceCard;
import finance.objects.TransactionType;
import finance.web.html.AccountServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
@HttpServletPath("/secure/card")
public class CardServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CardServlet.class);

    @Override
    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp) {
        final TransactionType type = TransactionType.parse(req.getParameter("type"));
        final String id = req.getParameter("id");

        final Account owner = (Account) req.getAttribute("account");

        try {
            final FinanceCard card = owner.rollbackTransaction(type, Long.parseLong(id));
            card.thaw();

            resp.setHeader("Location", AccountServlet.resourceUrl(card.date().getYear(), card.date().getMonthValue()));
        } catch (Exception e) {
            logger.error(
                    "Couldn't perform delete operation for requested params [type='{}', id='{}'] due to unknown errors:",
                    type, id, e);

            resp.setStatus(500);
        }
    }
}
