package finance.service.validation;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class ValidDecimal {
    public static final Pattern pattern = Pattern.compile("\\d{1,12}[\\.,]{0,1}\\d{0,3}");

    private String errorMsg(String source) {
        return String.format(
                "Please enter positive decimal number like one of '10', '10.235', '10,235' instead of '%s'",
                source);
    }

    public BigDecimal fromString(String source) {
        if (source == null || !pattern.matcher(source).matches())
            throw new IllegalArgumentException(errorMsg(source));

        return new BigDecimal(source.replace(',', '.'));
    }

}
