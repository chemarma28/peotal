package finance.web.forms.thymeleaf;

import java.util.Arrays;
import java.util.List;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import finance.web.forms.Form;

public class EachFailTagProcessor extends AbstractAttributeTagProcessor {
    public static final int PRECEDENCE = 1010;
    public static final String ATTR_NAME = "eachfail";

    public EachFailTagProcessor(String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {
        Object raw = context.getVariable("form");

        if (null == raw || !(raw instanceof Form))
            return;

        Form form = (Form) raw;

        Each each = new Each(attributeValue);

        if (Form.Result.FAIL != form.validation(each.field))
            return;

        structureHandler.iterateElement(each.var, each.statusvar, form.whyFailed(each.field));
    }

    private final static class Each {
        String var;
        String statusvar;
        String field;

        Each(String expr) {
            List<String> tokens = Arrays.stream(expr.split(" "))
                    .map(part -> part.trim())
                    .toList();

            if (tokens.size() < 3)
                throw new TemplateProcessingException(String.format("Invalid syntax of each expression: '%s'", expr));

            String[] t1 = tokens.get(0).split(",");

            if (2 < t1.length)
                throw new TemplateProcessingException(String.format("Invalid syntax of each expression: '%s'", expr));

            this.var = t1[0];

            if (1 < t1.length)
                this.statusvar = t1[0];

            if (!"of".equals(tokens.get(1)))
                throw new TemplateProcessingException(String.format("Invalid syntax of each expression: '%s'", expr));

            this.field = tokens.get(2);
        }
    }
}
