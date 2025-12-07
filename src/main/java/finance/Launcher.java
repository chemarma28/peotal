package finance;

import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Server;

public class Launcher {
    public static void main(String[] args) throws Exception {
        final Server server = new Server(8082);
        final WebAppContext webapp = new WebAppContext();

        webapp.setBaseResourceAsString(Launcher.class.getClassLoader().getResource("webapp").toURI().toString());

        server.setHandler(webapp);
        server.start();

        server.join();
    }
}
