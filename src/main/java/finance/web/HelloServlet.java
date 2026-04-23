package finance.web;

import java.io.IOException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.forms.Form;
import finance.forms.TemplateForm;
import finance.guice.modules.HttpServletPath;
import finance.web.forms.CredentialsForm;
import finance.web.html.AccountServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
@HttpServletPath("/hello.html")
public class HelloServlet extends HttpServlet {
    @Inject
    private TemplateEngine engine;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final TemplateForm form = new TemplateForm();
        final Context ctx = new Context();
        ctx.setVariable("form", form);

        engine.process("hello", ctx, resp.getWriter());
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final Form form = new CredentialsForm(req);
        final Context ctx = new Context();
        ctx.setVariable("form", form);

        if (form.hasErrors()) {
            resp.setStatus(401);
            engine.process("hello", ctx, resp.getWriter());
        } else {
            resp.sendRedirect(AccountServlet.resourceUrl());
            resp.addCookie(null);
        }
    }
}
