package finance.service.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

import javax.naming.OperationNotSupportedException;

public class ValidLocalDate {
    public static final Pattern pattern = Pattern
            .compile("\\d{2}[-/\\.]\\d{2}[-/\\.]\\d{4}|\\d{4}[-/\\.]\\d{2}[-/\\.]\\d{2}");
    private final List<String> formats = List.of("dd-MM-yyyy", "dd.MM.yyyy", "dd/MM/yyyy", "yyyy-MM-dd", "yyyy.MM.dd",
            "yyyy/MM/dd");

    private String errorMsg(String source) {
        return String.format(
                "Can not convert '%s' to type %s, causes source string is not consistent to the following formats [%s].",
                source,
                LocalDate.class.getName(),
                String.join(", ", formats));
    }

    public LocalDate fromString(String source) throws OperationNotSupportedException {
        if (source == null || !pattern.matcher(source).matches())
            throw new IllegalArgumentException(errorMsg(source));

        for (String format : formats) {
            try {
                return LocalDate.parse(source, DateTimeFormatter.ofPattern(format));
            } catch (DateTimeParseException e) {
                continue;
            }
        }

        throw new OperationNotSupportedException("Operation is not supported for string for string: " + source);
    }
}