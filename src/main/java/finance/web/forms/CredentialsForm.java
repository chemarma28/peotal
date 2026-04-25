package finance.web.forms;

import finance.forms.Mapping;
import finance.forms.Required;
import finance.forms.SimpleForm;
import finance.objects.Account;
import finance.service.AuthService;
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

    private Account account;

    private CredentialsForm() {
        this.field("username", new Required<>(Username));
        this.field("password", new Required<>(Password));
    }

    public CredentialsForm(final AuthService authService, final HttpServletRequest req) {
        this();
        super.bindFromRequest(req);

        this.account = authService.match(username(), password());

        if (null == this.account)
            errorOnWrongCredentials();
    }

    private void errorOnWrongCredentials() {
        this.validation.addError("Wrong username or password.");
    }

    public String username() {
        return this.value("username", String.class);
    }

    public String password() {
        return this.value("password", String.class);
    }

    public Account account() {
        return this.account;
    }
}
