package support;

import io.avaje.inject.Bean;
import io.avaje.inject.Factory;
import io.avaje.validation.Validator;

@Factory
public final class ValidationFactory {

    @Bean
    public Validator validator() {
        return Validator.builder()
                .failFast(true)
                .build();
    }
}
