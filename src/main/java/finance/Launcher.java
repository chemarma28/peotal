package finance;

import static jakarta.servlet.DispatcherType.ASYNC;
import static jakarta.servlet.DispatcherType.ERROR;
import static jakarta.servlet.DispatcherType.FORWARD;
import static jakarta.servlet.DispatcherType.INCLUDE;
import static jakarta.servlet.DispatcherType.REQUEST;
import static org.eclipse.jetty.ee10.servlet.ServletContextHandler.getServletContextHandler;

import java.util.EnumSet;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Server;

import com.google.inject.servlet.GuiceFilter;

import finance.guice.GuiceWebApplicationModule;

public class Launcher {
    public static void main(String[] args) throws Exception {
        final Server server = new Server(8082);
        final WebAppContext webapp = new WebAppContext();

        webapp.setBaseResourceAsString(Launcher.class.getClassLoader().getResource("webapp").toURI().toString());
        webapp.addFilter(GuiceFilter.class, "/*", EnumSet.of(FORWARD, INCLUDE, REQUEST, ASYNC, ERROR));
        webapp.addEventListener(new GuiceWebApplicationModule());

        // final Path resources = Paths.get("src/main/resources/webapp");

        final ServletContextHandler handler = getServletContextHandler(webapp.getServletContext());
        server.setHandler(handler);
        server.setHandler(webapp);

        server.start();

        server.join();
    }
}
