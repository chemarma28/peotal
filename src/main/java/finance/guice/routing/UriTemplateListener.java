package finance.guice.routing;

import java.lang.reflect.Field;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class UriTemplateListener implements TypeListener {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
        Class<?> c1ass = type.getRawType();

        if (c1ass != null && c1ass.isAnnotationPresent(Route.class)) {
            boolean fieldOfUriTemplateTypePresent = false;

            for (Field field : c1ass.getDeclaredFields()) {
                if (field.getType() == UriTemplate.class) {
                    if (fieldOfUriTemplateTypePresent)
                        encounter.addError(
                                String.format("Mutiple uri template bindings in servlet is not allowed: check '%s'",
                                        c1ass.getTypeName()));

                    encounter.register(new UriTemplateInjector(field, c1ass.getAnnotation(Route.class)));

                    fieldOfUriTemplateTypePresent = true;
                }
            }
        }
    }

}
