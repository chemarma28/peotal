package finance.web.forms;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class NumberBtQ implements Rule<BigDecimal, BigDecimal> {
    private final BigDecimal leftInclusive;
    private final BigDecimal rightInvlusive;

    public NumberBtQ(BigDecimal leftInclusive, BigDecimal rightInvlusive) {
        this.leftInclusive = leftInclusive;
        this.rightInvlusive = rightInvlusive;
    }

    @Override
    public boolean question(BigDecimal source) {
        return leftInclusive.compareTo(source) <= 0 && source.compareTo(rightInvlusive) <= 0;
    }

    @Override
    public List<String> why() {
        return Collections.singletonList("Number in range [] required");
    }

    @Override
    public BigDecimal produce(BigDecimal source) {
        return source;
    }

}
