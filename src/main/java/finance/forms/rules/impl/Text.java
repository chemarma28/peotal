package finance.forms.rules.impl;

import static finance.forms.rules.ValidationResult.validationPositiveResult;

import finance.forms.rules.FieldRule;
import finance.forms.rules.ValidationResult;

public class Text implements FieldRule<String, String> {

    @Override
    public ValidationResult<String> apply(String value) {
        return validationPositiveResult(value.trim());
    }

}
