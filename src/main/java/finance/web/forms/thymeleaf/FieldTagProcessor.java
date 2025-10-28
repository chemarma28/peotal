package finance.web.forms.thymeleaf;

import java.util.NoSuchElementException;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import finance.web.forms.Form;

public class FieldTagProcessor extends AbstractAttributeTagProcessor {
    public static final int PRECEDENCE = 1000;
    public static final String ATTR_NAME = "field";

    public FieldTagProcessor(String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {
        String field = attributeValue.trim();

        if ("".equals(field))
            return;

        structureHandler.setAttribute("name", field);

        Object raw = context.getVariable("form");

        if (null == raw || !(raw instanceof Form))
            return;

        Form form = (Form) raw;

        try {
            final String value = form.rawValue(field);

            String suffixed = field + "_v";
            structureHandler.setLocalVariable(suffixed, value);
        } catch (NoSuchElementException e) {
        }
    }

}
