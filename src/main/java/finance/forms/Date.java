package finance.forms;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Date implements Mapping<LocalDate> {
    @Override
    public LocalDate map(HttpServletRequest req, Object key, ValidationResult validation) {
        final String original = req.getParameter(key.toString());

        if (null == original)
            return null;

        try {
            return LocalDate.parse(original);
        } catch (DateTimeParseException e) {
            validation.addError(key, "Please enter a valid date");
        }

        return null;
    }
}
