package finance.guice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Format extends AbstractModule {
    public static final String DECIMAL = "DECIMAL";
    public static final String CURRENCY = "CURRENCY";

    @Override
    protected void configure() {
        Locale locale = Locale.US;

        bind(Locale.class).toInstance(locale);
        bind(DateFormatSymbols.class).toInstance(new DateFormatSymbols(locale));
        bind(DateTimeFormatter.class).toInstance(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Provides
    @Named(CURRENCY)
    public NumberFormat currencyFormatter(Locale locale) {
        return NumberFormat.getCurrencyInstance(locale);
    }

    @Provides
    @Named(DECIMAL)
    public NumberFormat numberFormatter(Locale locale) {
        final NumberFormat fnumber = NumberFormat.getNumberInstance(locale);
        fnumber.setMinimumFractionDigits(2);
        fnumber.setMaximumFractionDigits(2);

        return fnumber;
    }
}
