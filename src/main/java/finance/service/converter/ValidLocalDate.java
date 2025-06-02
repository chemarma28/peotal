package finance.service.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ValidationException;
import org.modelmapper.spi.ErrorMessage;

public class ValidLocalDate extends AbstractConverter<String, LocalDate> {
    private final Pattern pattern = Pattern.compile("\\d{2}[-/\\.]\\d{2}[-/\\.]d{4}");
    private final List<String> formats = List.of("dd-MM-yyyy", "dd.MM.yyyy", "dd/MM/yyyy");

    private String errorMsg(String source) {
        return String.format(
                "String must be consistent with one of [%s] date formats instead of '%s'.",
                String.join(", ", formats),
                source);
    }

    @Override
    protected LocalDate convert(String source) {
        if (source == null || pattern.matcher(source).matches())
            throw new ValidationException(List.of(new ErrorMessage(errorMsg(source))));

        for (String format : formats) {
            try {
                return LocalDate.parse(source, DateTimeFormatter.ofPattern(format));
            } catch (DateTimeParseException e) {
                continue;
            }
        }

        throw new ValidationException(List.of(new ErrorMessage(errorMsg(source))));
    }

}