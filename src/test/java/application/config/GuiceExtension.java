package application.config;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import finance.guice.modules.Mapper;

public class GuiceExtension implements ParameterResolver {
    private final Injector injector;

    public GuiceExtension() {
        injector = Guice.createInjector(new Mapper());
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return true;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if (supportsNamedInjection(parameterContext))
            return resolveNamedInjection(parameterContext);

        return resolveDefaultInjection(parameterContext);
    }

    private boolean supportsNamedInjection(ParameterContext parameterContext) {
        return parameterContext.getParameter().isAnnotationPresent(Named.class);
    }

    private Object resolveDefaultInjection(ParameterContext parameterContext) {
        return injector.getInstance(parameterContext.getParameter().getType());
    }

    private Object resolveNamedInjection(ParameterContext parameterContext) {
        Class<?> type = parameterContext.getParameter().getType();
        String name = parameterContext.getParameter().getAnnotation(Named.class).value();

        return injector.getInstance(Key.get(type, Names.named(name)));
    }
}
