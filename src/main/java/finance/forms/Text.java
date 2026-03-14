package finance.forms;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.defaultString;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import jakarta.servlet.http.HttpServletRequest;

public class Text implements Mapping<String> {
    private int min = -1;

    public Text() {
    }

    public Text(int min) {
        this.min = min;
    }

    @Override
    public String map(HttpServletRequest req, String key, ValidationResult validation) {
        final String v = req.getParameter(key);

        if (min < 1)
            return v;

        try {
            final String decoded = URLDecoder.decode(defaultString(v), "UTF-8");

            if (decoded.length() < min)
                validation.addError(key, format("The value should contain %d characters at least.", min));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return v;
    }
}
