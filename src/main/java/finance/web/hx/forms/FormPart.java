package finance.web.hx.forms;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import finance.web.forms.Form;
import finance.web.forms.FormField;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FormPart {
    private final TemplateEngine engine;
    private final String template;
    private final Map<String, FormField<?>> fields = new HashMap<>();
    private final Map<String, String> fragments = new HashMap<>();

    public FormPart(final TemplateEngine engine, final String template) {
        this.engine = engine;
        this.template = template;
    }

    public Chain match() {
        return new Chain();
    }

    public void send(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final Enumeration<String> params = req.getParameterNames();
        final String fieldName = params.hasMoreElements() ? params.nextElement() : null;

        int status = 200;

        if (!"GET".equals(req.getMethod()))
            status = 400;
        else if (!fragments.containsKey(fieldName))
            status = 404;

        if (!(200 <= status && status <= 299)) {
            resp.setStatus(status);
            resp.setContentLength(0);

            if (null != req.getHeader("HX-Request"))
                resp.setHeader("HX-Reswap", "none");

            return;
        }

        final Context context = new Context(null, Map.of(
                "form", new Form(req, fields.get(fieldName))));

        engine.process(template, Set.of(fragments.get(fieldName)), context, resp.getWriter());
    }

    public class Chain {
        public Fragment when(final FormField<?> field) {
            return new Fragment(field);
        }

        public FormPart end() {
            return FormPart.this;
        }
    }

    public class Fragment {
        private final FormField<?> field;

        public Fragment(final FormField<?> field) {
            this.field = field;

            FormPart.this.fields.put(this.field.name(), field);
        }

        public Chain fragment(final String fragment) {
            FormPart.this.fragments.put(this.field.name(), fragment);

            return new Chain();
        }
    }
}
