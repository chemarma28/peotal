package finance.guice.modules;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Currency;
import java.util.Locale;

public class CurrencyFormat {
    private final Locale locale;
    private final Currency currency;
    private final DecimalFormat format;

    public CurrencyFormat(Locale locale) {
        this.locale = locale;
        currency = Currency.getInstance(locale);
        format = new DecimalFormat();
    }

    public String formatDecimal(BigDecimal value, int minFracDigits, int maxFracDigits, String separator) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);

        if ("COMMA".equals(separator))
            symbols.setDecimalSeparator(',');
        else if ("POINT".equals(separator))
            symbols.setDecimalSeparator('.');

        format.setMaximumFractionDigits(maxFracDigits);
        format.setMinimumFractionDigits(minFracDigits);
        format.setDecimalFormatSymbols(symbols);

        return currency.getSymbol() + format.format(value == null ? BigDecimal.ZERO : value);
    }
}
