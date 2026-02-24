package finance.service;

import java.text.DecimalFormat;

public class Formats {
    public static final DecimalFormat decimals = new DecimalFormat();

    static {
        decimals.setParseBigDecimal(true);
    }
}
