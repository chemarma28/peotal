package finance.guice.modules;

import java.util.Set;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import finance.forms.thymeleaf.FieldTagProcessor;
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
        resolver.setPrefix("/WEB-INF/");
        resolver.setSuffix(".html");
        resolver.setCacheable(false);
        resolver.setCharacterEncoding("utf-8");

        return resolver;
    }

    @Provides
    public TemplateEngine engine() {
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver());
        engine.addDialect(new FormDialect());

        return engine;
    }

    final class FormDialect extends AbstractProcessorDialect {
        FormDialect() {
            super("Form Dialect", "th", StandardDialect.PROCESSOR_PRECEDENCE);
        }

        @Override
        public Set<IProcessor> getProcessors(String dialectPrefix) {
            return Set.of(new FieldTagProcessor(dialectPrefix));
        }
    }
}
