package finance.web.forms;

import java.util.Collections;
import java.util.List;

public class IntegerBtQ implements Rule<Integer, Integer> {
    private final int left;
    private final int right;

    public IntegerBtQ(final int left, final int right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean question(final Integer source) {
        return left <= source && source <= right;
    }

    @Override
    public List<String> why() {
        return Collections.singletonList(String.format("Integer between %d and %d required", left, right));
    }

    @Override
    public Integer produce(final Integer source) {
        return source;
    }

}
