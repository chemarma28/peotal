package finance.guice.modules;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateFormat {
    public String format(LocalDate value, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(value);
    }
}
