package finance.web;

import java.io.IOException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.forms.TemplateForm;
import finance.guice.modules.HttpServletPath;
import finance.service.AuthService;
import finance.web.forms.CredentialsForm;
import finance.web.html.AccountServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
@HttpServletPath("/hello.html")
public class HelloServlet extends HttpServlet {
    @Inject
    private TemplateEngine engine;
    @Inject
    private AuthService authService;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final TemplateForm form = new TemplateForm();
        final Context ctx = new Context();
        ctx.setVariable("form", form);

        engine.process("hello", ctx, resp.getWriter());
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final CredentialsForm form = new CredentialsForm(authService, req);
        final Context ctx = new Context();
        ctx.setVariable("form", form);

        if (form.hasErrors()) {
            resp.setStatus(401);
            engine.process("hello", ctx, resp.getWriter());

            return;
        }

        final String scopeId = authService.associate(form.account());
        final Cookie cookie = new Cookie(AuthService.SCOPE_ID, scopeId.toString());
        cookie.setMaxAge(3600 * 24);

        resp.addCookie(cookie);
        resp.sendRedirect(AccountServlet.resourceUrl());
    }
}
