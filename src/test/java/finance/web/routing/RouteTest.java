package finance.web.routing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Injector;

import finance.guice.routing.GuiceServletRoutingModule;

public class RouteTest {
    private Injector injector;

    @BeforeEach
    public void init() {
        injector = Guice.createInjector(new GuiceServletRoutingModule() {
            @Override
            public void configureServlets() {
                autoServe(ImmutableList.of(TestableServlet.class));
            }
        });
    }

    @Test
    public void uriTemplateInjectTest() {
        TestableServlet service = injector.getInstance(TestableServlet.class);

        Assertions.assertNotNull(service);

        Assertions.assertEquals("route", service.serve("route"));
    }
}
