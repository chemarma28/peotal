package finance.guice.routing;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.inject.MembersInjector;

public class UriTemplateInjector<T> implements MembersInjector<T> {
    private final Field field;
    private final Route route;
    private static final Map<String, UriTemplate> templates = new ConcurrentHashMap<>();

    UriTemplateInjector(Field field, Route route) {
        this.field = field;
        this.route = route;
    }

    @Override
    public void injectMembers(T instance) {
        try {
            field.setAccessible(true);
            field.set(instance, UriTemplateInjector.get(route.value()));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static UriTemplate get(String uriTemplate) {
        if (!templates.containsKey(uriTemplate))
            templates.put(uriTemplate, new UriTemplate(uriTemplate));

        return templates.get(uriTemplate);
    }
}
