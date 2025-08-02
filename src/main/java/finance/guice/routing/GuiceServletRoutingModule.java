package finance.guice.routing;

import com.google.common.collect.ImmutableList;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.ServletModule;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

public class GuiceServletRoutingModule extends ServletModule {

    private boolean uriTemplateListenerBinded = false;

    protected void install(Module... modules) {
        for (Module module : modules)
            install(module);
    }

    protected void auto(Class<? extends HttpServlet> c1ass) {
        if (!uriTemplateListenerBinded) {
            bindListener(Matchers.any(), new UriTemplateListener());

            uriTemplateListenerBinded = true;
        }

        if (c1ass != null) {
            if (c1ass.isAnnotationPresent(Route.class))
                serveRegex(UriTemplateInjector.get(c1ass.getAnnotation(Route.class).value()).regex()).with(c1ass);
            else if (c1ass.isAnnotationPresent(WebServlet.class))
                for (String url : c1ass.getAnnotation(WebServlet.class).value())
                    serve(url).with(c1ass);
            else
                addError("Given type should be annotated with @WebServlet or @Route: " + c1ass.getName());
        }
    }

    protected void autoServe(ImmutableList<Class<? extends HttpServlet>> servlets) {
        for (Class<? extends HttpServlet> c1ass : servlets)
            auto(c1ass);
    }
}
