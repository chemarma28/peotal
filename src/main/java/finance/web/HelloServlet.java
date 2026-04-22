package finance.web;

import java.io.IOException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.guice.modules.HttpServletPath;
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
        engine.process("hello", new Context(), resp.getWriter());
    }
}
