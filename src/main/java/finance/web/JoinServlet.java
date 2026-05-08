package finance.web;

import java.io.IOException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.forms.Form;
import finance.forms.TemplateForm;
import finance.guice.modules.HttpServletPath;
import finance.service.AuthService;
import finance.web.forms.JoinForm;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
@HttpServletPath("/join.html")
public class JoinServlet extends HttpServlet {
    @Inject
    private TemplateEngine engine;
    @Inject
    private AuthService authService;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final Form form = new TemplateForm();
        final Context ctx = new Context();
        ctx.setVariable("form", form);

        engine.process("join", ctx, resp.getWriter());
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final JoinForm form = new JoinForm(req, authService);
        final Context ctx = new Context();
        ctx.setVariable("form", form);

        if (form.hasErrors()) {
            resp.setStatus(409);
            engine.process("/join.html", ctx, resp.getWriter());

            return;
        }
        
        resp.sendRedirect("/hello.html");
    }
}
