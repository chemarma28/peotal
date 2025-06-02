package finance.service.converter;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ValidationException;
import org.modelmapper.spi.ErrorMessage;

public class ValidDecimal extends AbstractConverter<String, BigDecimal> {
    private final Pattern pattern = Pattern.compile("\\d{1,12}[\\.,]{0,1}\\d{0,3}");

    private String errorMsg(String source) {
        return String.format(
                "Please enter positive decimal number like one of '10', '10.235', '10,235' instead of '%s'",
                source);
    }

    @Override
    protected BigDecimal convert(String source) {
        if (source == null || !pattern.matcher(source).matches())
            throw new ValidationException(List.of(new ErrorMessage(errorMsg(source))));

        return new BigDecimal(source);
    }

}
