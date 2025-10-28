package finance.web.forms;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

public class LocalDateQ implements Rule<String, LocalDate> {

    @Override
    public boolean question(String source) {
        try {
            LocalDate.parse(source);

            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Override
    public List<String> why() {
        return Collections.singletonList("'yyyy-MM-dd' format required");
    }

    @Override
    public LocalDate produce(String source) {
        return LocalDate.parse(source);
    }

}
