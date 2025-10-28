package finance.web.forms;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class IntegerQ implements Rule<String, Integer> {
    public static Pattern pattern = Pattern.compile("-?\\d{1,9}");

    @Override
    public boolean question(final String source) {
        return pattern.matcher(source.trim()).matches();
    }

    @Override
    public List<String> why() {
        return Collections.singletonList("Integer required");
    }

    @Override
    public Integer produce(final String source) {
        return Integer.parseInt(source.trim());
    }

}
