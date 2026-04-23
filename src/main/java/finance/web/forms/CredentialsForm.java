package finance.web.forms;

import finance.forms.Mapping;
import finance.forms.Required;
import finance.forms.SimpleForm;
import jakarta.servlet.http.HttpServletRequest;

public class CredentialsForm extends SimpleForm {
    private final Mapping<String> Username = (req, key, validation) -> {
        final String username = req.getParameter(key);

        return username;
    };

    private final Mapping<String> Password = (req, key, validation) -> {
        final String password = req.getParameter(key);

        return password;
    };

    private CredentialsForm() {
        this.field("username", new Required<>(Username));
        this.field("password", new Required<>(Password));
    }

    public CredentialsForm(final HttpServletRequest req) {
        this();
        bindFromRequest(req);

        // this.validation.addError("Username or password is wrong.");
    }

    public String username() {
        return this.value("username", String.class);
    }

    public String password() {
        return this.value("password", String.class);
    }
}
