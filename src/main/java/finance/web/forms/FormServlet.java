package finance.web.forms;

import java.io.IOException;
import java.util.function.Supplier;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.inject.Inject;

import finance.errors.Errors;
import finance.forms.Form;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

public abstract class FormServlet extends HttpServlet {
    @Inject
    protected TemplateEngine engine;

    protected boolean rejectedOnConflicts(final Form form, final Context ctx, final HttpServletResponse resp)
            throws IOException {
        if (form.isValid())
            return false;

        ctx.setVariable("form", form);

        resp.setStatus(409);
        engine.process(template(), ctx, resp.getWriter());

        return true;
    }

    protected boolean rejectedOnInternalErrors(
            final Supplier<Boolean> errorQ,
            final Form form,
            final Context ctx,
            final HttpServletResponse resp) throws IOException {
        if (null == errorQ || !errorQ.get())
            return false;

        ctx.setVariable("form", form);
        ctx.setVariable("INTERNAL_ERROR", Errors.INTERNAL);

        resp.setStatus(500);

        engine.process(template(), ctx, resp.getWriter());

        return true;
    }

    protected void html(final Form form, final Context ctx, final HttpServletResponse resp) throws IOException {
        ctx.setVariable("form", form);

        engine.process("card-form", ctx, resp.getWriter());
    }

    protected abstract String template();
}
