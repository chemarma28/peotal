package finance.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

public class Formats {
    public static final DecimalFormat decimals = new DecimalFormat("###,###,###.##");
    public static final DateTimeFormatter dates = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter browserDates = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static {
        decimals.setParseBigDecimal(true);
    }

    public static String fdate(final Temporal d) {
        if (null == d)
            return "";

        return dates.format(d);
    }

    public static String fdecimal(final BigDecimal v) {
        if (null == v)
            return "";

        return decimals.format(v);
    }

    public static String fbrowser(final LocalDate d) {
        if (null == d)
            return "";

        return browserDates.format(d);
    }
}
