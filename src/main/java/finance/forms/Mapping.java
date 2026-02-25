package finance.forms;

import jakarta.servlet.http.HttpServletRequest;

public interface Mapping<R> {
    R map(HttpServletRequest req, String key, ValidationResult validation);
}
