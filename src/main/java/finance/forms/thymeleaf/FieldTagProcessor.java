package finance.forms.thymeleaf;

import static org.apache.commons.lang3.StringUtils.defaultString;

import java.util.Map;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import finance.forms.SimpleForm;

public class FieldTagProcessor extends AbstractAttributeTagProcessor {
    public static final int PRECEDENCE = 1000;
    public static final String ATTR_NAME = "field";

    private static final String INPUT = "input";
    private static final String TEXTAREA = "textarea";
    private static final String VALUE = "value";
    private static final String TEXT = "text";

    private static Map<String, String> valueAttributeName = Map.of(
        INPUT, VALUE,
        TEXTAREA, TEXT
    );

    public FieldTagProcessor(String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {
        final Object raw = context.getSelectionTarget();

        if (!(raw instanceof SimpleForm))
            throw new IllegalStateException(
                "A form object is not specified under 'th:object' attribute or of incorrect type."
            );

        final SimpleForm form = (SimpleForm) raw;

        structureHandler.setAttribute("id", attributeValue);
        structureHandler.setAttribute("name", attributeValue);

        final String targetValueAttribute = valueAttributeName.get(tag.getElementCompleteName());
        final String value = defaultString(form.requestValue(attributeValue));

        if (null != targetValueAttribute)
            structureHandler.setAttribute(targetValueAttribute, value);
    }
}
