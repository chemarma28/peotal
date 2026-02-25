package finance.forms;

import finance.service.Formats;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.text.ParseException;

public class Number implements Mapping<BigDecimal> {
    @Override
    public BigDecimal map(final HttpServletRequest req, final String key, final ValidationResult result) {
        final String original = req.getParameter(key.toString());

        if (null == original)
            return null;

        try {
            return (BigDecimal) Formats.decimals.parse(
                    original.trim()
                    .replaceAll("\\s", "")
            );
        } catch (ParseException e) {
            result.addError(key, "Please enter a valid decimal number");
        }

        return null;
    }
}
