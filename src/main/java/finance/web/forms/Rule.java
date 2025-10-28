package finance.web.forms;

import java.util.List;

public interface Rule<A, R> {
    boolean question(A source);

    List<String> why();

    R produce(A source);
}
