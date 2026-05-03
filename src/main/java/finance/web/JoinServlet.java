package finance.web;

import com.google.inject.Singleton;

import finance.guice.modules.HttpServletPath;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
@HttpServletPath("/join.html")
public class JoinServlet extends HttpServlet {
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) {

    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) {

    }
}
