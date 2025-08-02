package finance.web;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.inject.Singleton;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
public class ClientContextProvider extends HttpFilter {
    private final Map<String, Field> attributes = new ConcurrentHashMap<>();
    public static final String clientHeaderPrefix = "Client-Context-";
    private final int offset = clientHeaderPrefix.length() + 1;

    @Override
    public void init(FilterConfig config) throws ServletException {
        Field[] fields = ClientContext.class.getDeclaredFields();

        for (Field f : fields)
            attributes.put(f.getName(), f);
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        ClientContext client = new ClientContext();

        Enumeration<String> headers = req.getHeaderNames();

        while (headers.hasMoreElements()) {
            final String h = headers.nextElement();

            if (h.startsWith(clientHeaderPrefix)) {
                final String name = h.substring(offset);

                Field f = attributes.get(name.substring(0, 1).toLowerCase() + name.substring(1));

                try {
                    if (f != null)
                        f.set(client, req.getHeader(h));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        req.setAttribute("clientContext", client);

        chain.doFilter(req, res);
    }
}
