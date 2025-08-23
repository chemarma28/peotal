package finance.web.routing;

import java.util.Map;

import com.google.inject.Singleton;

import finance.guice.routing.Route;
import finance.guice.routing.UriTemplate;
import jakarta.servlet.http.HttpServlet;

@Singleton
@Route("/test/entrypoint/{value}")
public class TestableServlet extends HttpServlet {
    private UriTemplate uriTemplate;
    
    public String serve(String value) {
        final String request = String.format("/test/entrypoint/%s", value);

        Map<String, String> params = uriTemplate.match(request);

        return params.get("value");
    }
}
