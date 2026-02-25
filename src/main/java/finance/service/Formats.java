package finance.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class Formats {
    public static final DecimalFormat decimals = new DecimalFormat("###,###,###.##");
    public static final SimpleDateFormat dates = new SimpleDateFormat("dd.MM.yyyy");

    static {
        decimals.setParseBigDecimal(true);
    }

    public static String fdate(final LocalDate d) {
        if (null == d)
            return "";

        return dates.format(d);
    }

    public static String fdecimal(final BigDecimal v) {
        if (null == v)
            return "";

        return decimals.format(v);
    }
}
