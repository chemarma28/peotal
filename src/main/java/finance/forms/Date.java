package finance.forms;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Date implements Mapping<LocalDate> {
    @Override
    public LocalDate map(HttpServletRequest req, String key, ValidationResult validation) {
        final String original = req.getParameter(key);

        if (null == original)
            return null;

        try {
            return LocalDate.parse(original);
        } catch (DateTimeParseException e) {
            validation.addError(key, "The value should be a date in 'yyyy-MM-dd' format.");
        }

        return null;
    }
}
