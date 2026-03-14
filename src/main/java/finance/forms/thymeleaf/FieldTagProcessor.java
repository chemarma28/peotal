package finance.forms.thymeleaf;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import finance.forms.Form;

public class FieldTagProcessor extends AbstractAttributeTagProcessor {
    public static final int PRECEDENCE = 1000;
    public static final String ATTR_NAME = "field";

    public FieldTagProcessor(String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {
        final Object raw = context.getSelectionTarget();

        if (!(raw instanceof Form))
            throw new IllegalStateException(
                    "A form object is not specified under 'th:object' attribute or of incorrect type.");

        final Form form = (Form) raw;

        structureHandler.setAttribute("id", attributeValue);
        structureHandler.setAttribute("name", attributeValue);

        reset(form.requestValue(attributeValue), tag, structureHandler);
    }

    private void reset(String reqValue, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
        if (isBlank(reqValue))
            return;

        if (tag.getElementCompleteName().equalsIgnoreCase("textarea")) {
            structureHandler.setBody(reqValue, false);
            return;
        }

        structureHandler.setAttribute("value", reqValue);
    }
}
