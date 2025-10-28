package finance.web.forms;

import java.util.Collections;
import java.util.List;

public class EmptyRule implements Rule<String, String> {

    @Override
    public boolean question(String source) {
        return true;
    }

    @Override
    public List<String> why() {
        return Collections.emptyList();
    }

    @Override
    public String produce(String source) {
        return source;
    }

}
