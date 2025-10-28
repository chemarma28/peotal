package finance.web.forms;

import java.util.List;

public class ChainQ<L, T, R> implements Rule<L, R> {
    private final Rule<L, T> left;
    private final Rule<T, R> right;
    private List<String> why;

    public ChainQ(Rule<L, T> left, Rule<T, R> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean question(L source) {
        if (left.question(source)) {
            final T value = left.produce(source);

            why = right.why();

            return right.question(value);
        }

        why = left.why();

        return false;
    }

    @Override
    public List<String> why() {
        return why;
    }

    @Override
    public R produce(L source) {
        return right.produce(left.produce(source));
    }

}
