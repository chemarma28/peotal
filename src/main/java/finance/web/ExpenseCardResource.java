package finance.web;

import java.io.IOException;

import org.thymeleaf.TemplateEngine;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.guice.modules.HttpServletPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
@HttpServletPath("/hx/card")
public class ExpenseCardResource extends HttpServlet {
    @Inject
    private TemplateEngine engine;

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
