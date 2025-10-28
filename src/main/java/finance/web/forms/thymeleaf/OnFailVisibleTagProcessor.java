package finance.web.forms.thymeleaf;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.standard.processor.AbstractStandardConditionalVisibilityTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import finance.web.forms.Form;

public class OnFailVisibleTagProcessor extends AbstractStandardConditionalVisibilityTagProcessor {
    public static final int PRECEDENCE = 1000;
    public static final String ATTR_NAME = "onfailvisible";

    public OnFailVisibleTagProcessor(final String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, ATTR_NAME, PRECEDENCE);
    }

    @Override
    protected boolean isVisible(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue) {
        Object raw = context.getVariable("form");

        if (null == raw || !(raw instanceof Form))
            return false;

        Form form = (Form) raw;

        if (attributeValue == null || attributeValue.trim().equals(""))
            return form.failedQ();

        Form.Result result = form.validation(attributeValue);

        if (Form.Result.FAIL == result)
            return true;

        return false;
    }
}
