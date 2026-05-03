package finance.web.forms;

import finance.forms.Required;
import finance.forms.SimpleForm;
import finance.forms.Text;
import finance.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;

public class JoinForm extends SimpleForm {
    private AuthService authService;

    private JoinForm() {
        this.field("username", new Required<>(new Text()));
        this.field("password", new Required<>(new Text()));
        this.field("password2", new Required<>(new Text()));
    }

    public JoinForm(final HttpServletRequest req, final AuthService authService) {
        this();
        this.bindFromRequest(req);
    }
}
