package finance.guice.modules;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import jakarta.servlet.ServletContext;

public class Thymeleaf extends AbstractModule {
    private final ServletContext servletContext;

    public Thymeleaf(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    protected void configure() {
    }

    private WebApplicationTemplateResolver resolver() {
        WebApplicationTemplateResolver resolver = new WebApplicationTemplateResolver(
                JakartaServletWebApplication.buildApplication(servletContext));

        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setCacheable(false);
        resolver.setCharacterEncoding("utf-8");

        return resolver;
    }

    @Provides
    public TemplateEngine engine() {
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver());

        return engine;
    }
}
