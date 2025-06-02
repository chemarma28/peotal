package finance.guice.routing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UriTemplate {
    private final Pattern declarationRegex = Pattern.compile("\\{\\w+\\}", Pattern.DOTALL);
    private final String paramRegex = "[_\\\\w]+";
    private final Pattern pattern;

    private final String template;
    private final List<String> params;

    public UriTemplate(String template) {
        this.template = template;
        pattern = Pattern.compile(regex());

        params = parameters(template);
    }

    public boolean matches(String uri) {
        return pattern.matcher(uri).matches();
    }

    public String regex() {
        return declarationRegex.matcher(template).replaceAll(paramRegex);
    }

    public List<String> parameters(String uriTemplate) {
        List<String> names = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\{\\w+\\}", Pattern.DOTALL);

        Matcher matcher = pattern.matcher(uriTemplate);

        while (matcher.find()) {
            String token = uriTemplate.substring(matcher.start(), matcher.end());

            names.add(token.substring(1, token.length() - 1));
        }

        return names;
    }

    private UriParamValueInfo value(String uri, int p) {
        StringBuilder buffer = new StringBuilder();

        while (p < uri.length() && uri.charAt(p) != '/')
            buffer.append(uri.charAt(p++));

        return new UriParamValueInfo(p, buffer.toString());
    }

    private int skipDeclaration(String uriTemplate, int p) {
        while (p < uriTemplate.length() && uriTemplate.charAt(p) != '}')
            p++;

        return ++p;
    }

    public Map<String, String> match(String uri) {
        if (!matches(uri))
            throw new UnsupportedOperationException(
                    String.format("Given URI '%s' doesn't appropriate to template '%s'.", uri, template));

        List<String> values = new ArrayList<>();

        int pt = 0;
        int pu = 0;

        while (pu < uri.length() && pt < template.length()) {
            if (template.charAt(pt) == '{') {
                UriParamValueInfo param = value(uri, pu);

                values.add(param.value);

                pu = param.end;
                pt = skipDeclaration(template, pt);
            } else {
                pt++;
                pu++;
            }
        }

        Map<String, String> r = new HashMap<String, String>();

        for (int i = 0; i < params.size(); i++)
            r.put(params.get(i), values.get(i));

        return r;
    }

    private class UriParamValueInfo {
        int end;
        String value;

        UriParamValueInfo(int p, String value) {
            end = Objects.requireNonNull(p);
            this.value = Objects.requireNonNull(value);
        }
    }
}
