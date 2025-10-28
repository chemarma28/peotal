package finance.web.forms;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class NonNegativeNumberQ implements Rule<BigDecimal, BigDecimal> {

    @Override
    public boolean question(BigDecimal source) {
        return BigDecimal.ZERO.compareTo(source) <= 0;
    }

    @Override
    public List<String> why() {
        return Collections.singletonList("Non negative number required");
    }

    @Override
    public BigDecimal produce(BigDecimal source) {
        return source;
    }

}
