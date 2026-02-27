package finance.forms;

import jakarta.servlet.http.HttpServletRequest;

public class Text implements Mapping<String> {

    @Override
    public String map(HttpServletRequest req, String key, ValidationResult validation) {
        return req.getParameter(key);
    }
}
