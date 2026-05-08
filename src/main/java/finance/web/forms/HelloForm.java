package finance.web.forms;

import finance.forms.Required;
import finance.forms.SimpleForm;
import finance.forms.Text;
import finance.objects.Account;
import finance.objects.Password;
import finance.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;

public class HelloForm extends SimpleForm {
    private AuthService authService;
    private Account account;

    private HelloForm() {
        this.field("username", new Required<>(new Text()));
        this.field("password", new Required<>(new Text()));
    }

    public HelloForm(final AuthService authService, final HttpServletRequest req) {
        this();
        super.bindFromRequest(req);
        this.authService = authService;

        this.process();
    }

    private void process() {
        this.account = authService.request(username(), new Password(password()));

        if (null == this.account)
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
