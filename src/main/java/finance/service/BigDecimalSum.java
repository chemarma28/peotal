package finance.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

public class BigDecimalSum<T> {
    private final List<? extends T> elements;

    public BigDecimalSum(List<? extends T> elements) {
        this.elements = elements;
    }

    public BigDecimal resultByProperty(Function<T, BigDecimal> property) {
        return elements.stream()
                .reduce(
                        BigDecimal.ZERO,
                        (acc, element) -> acc.add(property.apply(element)),
                        BigDecimalSum::operation
                );
    }

    public static BigDecimal operation(BigDecimal l, BigDecimal r) {
        return l.add(r);
    }
}
