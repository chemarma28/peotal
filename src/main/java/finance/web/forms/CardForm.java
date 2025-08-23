package finance.web.forms;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.guice.routing.Route;
import finance.service.validation.ValidDecimal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
@Route("/hx/forms/card")
public class CardForm extends HttpServlet {
    @Inject
    private TemplateEngine te;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Form form = new Form();
        Context thymeleaf = new Context(null, Map.of("form", form));

        if (req.getParameter("modal") != null)
            te.process("modal-card-form", thymeleaf, resp.getWriter());
        else {
            form.setLastValue("cardAmount", req.getParameter("cardAmount"));
            form.setLastValue("cardDate", req.getParameter("cardDate"));

            if (!ValidDecimal.pattern.matcher(form.lastValue("cardAmount")).matches()) {
                form.addError("cardAmount", "Decimal value required");
                te.process("modal-card-form", Set.of("card-form"), thymeleaf, resp.getWriter());
            } else {
                resp.setHeader("HX-Reswap", "none");
                resp.setHeader("HX-Trigger", "modal:closed");
                req.getRequestDispatcher("/card");
            }
        }
    }
}
