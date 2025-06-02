package finance.web;

import java.io.IOException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
public class HelloServlet extends HttpServlet {
    @Inject
    private TemplateEngine te;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context ctx = new Context();
        ctx.setVariable("company", "SoftClub");

        String output = te.process("hello", ctx);

        resp.getWriter().write(output);
    }

}
