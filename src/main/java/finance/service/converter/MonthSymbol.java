package finance.service.converter;

import org.modelmapper.AbstractConverter;

import java.text.DateFormatSymbols;
import java.time.LocalDate;

public class MonthSymbol extends AbstractConverter<LocalDate, String> {
    private final String[] symbols;

    public MonthSymbol(DateFormatSymbols symbols) {
        this.symbols = symbols.getMonths();
    }

    @Override
    protected String convert(LocalDate date) {
        return symbols[date.getMonthValue() - 1];
    }
}
