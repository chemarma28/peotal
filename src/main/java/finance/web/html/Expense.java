package finance.web.html;

import static finance.service.Formats.fbrowser;

import java.io.IOException;
import java.time.LocalDate;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.forms.Form;
import finance.forms.TemplateForm;
import finance.guice.modules.HttpServletPath;
import finance.web.forms.ExpenseForm;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
@HttpServletPath("/expense.html")
public class Expense extends HttpServlet {
    @Inject
    private TemplateEngine engine;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Form form = new TemplateForm().ordinary("when", fbrowser(LocalDate.now()));

        final Context ctx = new Context();
        ctx.setVariable("form", form);

        engine.process("new-expense-card-form", ctx, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final ExpenseForm form = new ExpenseForm();
        form.bindFromRequest(req);

        final Context ctx = new Context();
        ctx.setVariable("form", form);

        engine.process("new-expense-card-form", ctx, resp.getWriter());
    }
}
