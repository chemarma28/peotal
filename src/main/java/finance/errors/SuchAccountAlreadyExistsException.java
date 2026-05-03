package finance.errors;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import finance.objects.Account;

public class SuchAccountAlreadyExistsException extends Exception {
    private final Account account;

    public SuchAccountAlreadyExistsException(final Account account) {
        this.account = requireNonNull(account);
    }

    @Override
    public String getMessage() {
        return format(
                "Account with name %s is already exists. Sign in with valid credentials or sign up with another name.",
                account.name());
    }
}
