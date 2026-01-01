package finance;

import java.util.EnumSet;

import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Server;

import com.google.inject.servlet.GuiceFilter;

import finance.guice.GuiceWebApplicationModule;

import static jakarta.servlet.DispatcherType.*;

public class Launcher {
    public static void main(String[] args) throws Exception {
        final Server server = new Server(8082);
        final WebAppContext webapp = new WebAppContext();

        webapp.setBaseResourceAsString(Launcher.class.getClassLoader().getResource("webapp").toURI().toString());
        webapp.addFilter(GuiceFilter.class, "/*", EnumSet.of(FORWARD, INCLUDE, REQUEST, ASYNC, ERROR));
        webapp.addEventListener(new GuiceWebApplicationModule());

        server.setHandler(webapp);
        server.start();

        server.join();
    }
}
