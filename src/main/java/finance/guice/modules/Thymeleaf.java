package finance.guice.modules;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
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
        engine.addDialect(new FinanceDialectExtension());

        return engine;
    }

    class FinanceDialectExtension implements IExpressionObjectDialect {

        @Override
        public String getName() {
            return "currency";
        }

        @Override
        public IExpressionObjectFactory getExpressionObjectFactory() {
            return new IExpressionObjectFactory() {

                @Override
                public Set<String> getAllExpressionObjectNames() {
                    return Set.of("currency", "localdates");
                }

                @Override
                public Object buildObject(IExpressionContext context, String expressionObjectName) {
                    if ("currency".equals(expressionObjectName))
                        return new CurrencyFormat(Locale.US);
                    if ("localdates".equals(expressionObjectName))
                        return new LocalDateFormat();

                    return null;
                }

                @Override
                public boolean isCacheable(String expressionObjectName) {
                    return true;
                }
            };
        }

    }
}
