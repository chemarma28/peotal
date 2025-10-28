package finance.web.forms.thymeleaf;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import finance.web.forms.Form;

public class VldClassTagProcessor extends AbstractAttributeTagProcessor {
    public static final int PRECEDENCE = 1000;
    public static final String ATTR_NAME = "vldclass";

    public VldClassTagProcessor(final String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {
        Object raw = context.getVariable("form");

        if (null == raw || !(raw instanceof Form))
            return;

        Form form = (Form) raw;

        String styleclass = "";

        if ("all".equals(attributeValue)) {
            if (form.failedQ())
                styleclass = "failed";
            else
                styleclass = "success";
        } else {
            Form.Result result = form.validation(attributeValue);

            switch (result) {
                case FAIL:
                    styleclass = "failed";
                    break;
                case SUCCESS:
                    styleclass = "success";
                    break;
                default:
                    break;
            }
        }

        final String current = tag.getAttributeValue("class");

        structureHandler.setAttribute("class", String.join(" ", current, styleclass));
    }
}
