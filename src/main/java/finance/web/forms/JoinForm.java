package finance.web.forms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import finance.errors.Errors;
import finance.forms.Required;
import finance.forms.SimpleForm;
import finance.forms.Text;
import finance.objects.Password;
import finance.objects.Password.PasswordConstraintViolation;
import finance.objects.Password.PasswordsNotMathException;
import finance.service.AuthService;
import finance.service.AuthService.SuchAccountAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;

public class JoinForm extends SimpleForm {
    private static final Logger logger = LoggerFactory.getLogger(JoinForm.class);

    private JoinForm() {
        this.field("username", new Required<>(new Text()));
        this.field("password", new Required<>(new Text()));
        this.field("confirmation", new Required<>(new Text()));
    }

    public JoinForm(final HttpServletRequest req, final AuthService authService) {
        this();
        this.bindFromRequest(req);
        this.process(authService);
    }

    private void process(final AuthService authService) {
        try {
            authService.join(username(), new Password(password(), confirmation()));
        } catch (SuchAccountAlreadyExistsException e) {
            this.validation.addError("username", "Try another username.");
        } catch (PasswordsNotMathException e) {
            this.validation.addError("confirmation", "Passwords don't match.");
        } catch (PasswordConstraintViolation e) {
            for (String requirement : e.requirements())
                this.validation.addError("password", requirement);
        } catch (Exception e) {
            logger.error("Form processing is unable due: ", e);
            this.validation.addError(Errors.INTERNAL);
        }
    }

    private String username() {
        return this.requestValue("username");
    }

    private String password() {
        return this.requestValue("password");
    }

    private String confirmation() {
        return this.requestValue("confirmation");
    }
}
