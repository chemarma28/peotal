package finance.web.forms;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.guice.routing.Route;
import finance.guice.routing.UriTemplate;
import finance.service.validation.ValidDecimal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
@Route("/hx/forms/card/{field}")
public class FormCardField extends HttpServlet {
    private UriTemplate uri;
    @Inject
    private TemplateEngine te;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> pathparams = uri.match(req.getServletPath());

        Form form = new Form();

        String field = pathparams.get("field");
        form.setLastValue(field, req.getParameter(field));

        if ("cardAmount".equals(field)) {
            if (!ValidDecimal.pattern.matcher(form.lastValue(field)).matches())
                form.addError(field, "Decimal value required");

            te.process("modal-card-form",
                    Set.of("card-amount-field"),
                    new Context(null, Map.of("form", form)),
                    resp.getWriter());
        }
    }
}
