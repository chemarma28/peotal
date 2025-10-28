package finance.web.forms;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class NumberQ implements Rule<String, BigDecimal> {
    @Override
    public boolean question(String source) {
        try {
            new BigDecimal(source);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    @Override
    public List<String> why() {
        return Collections.singletonList("Number value required");
    }

    @Override
    public BigDecimal produce(String source) {
        return new BigDecimal(source);
    }
}
