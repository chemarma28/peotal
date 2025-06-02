package finance.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import finance.guice.modules.Format;
import finance.guice.modules.LocalRepository;
import finance.guice.modules.Mapper;
import finance.guice.modules.Thymeleaf;
import finance.guice.routing.GuiceServletRoutingModule;
import finance.web.HelloServlet;

public class GuiceWebappModuleCompilation extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new GuiceServletRoutingModule() {
            @Override
            public void configureServlets() {
                install(new Format(),
                        new LocalRepository(),
                        new Mapper(),
                        new Thymeleaf(getServletContext()));

                // auto(ImmutableList.of(ExpensesHtmlResource.class));
                serve("/welcome").with(HelloServlet.class);
            }
        });
    }
}
