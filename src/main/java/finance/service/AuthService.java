package finance.service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.javalite.activejdbc.Base;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.errors.PasswordConstraintViolation;
import finance.errors.PasswordsNotMathException;
import finance.errors.SuchAccountAlreadyExistsException;
import finance.objects.Account;
import finance.objects.Password;

@Singleton
public class AuthService {
    public static final String SCOPE_ID = "SCOPE_ID";
    public static final int MAX_AGE_SECONDS = 3600 * 24;

    private static final Map<String, Account> sessions = new ConcurrentHashMap<>();

    @Inject
    private DataSource ds;

    public Account join(final String username, final Password password) throws SuchAccountAlreadyExistsException,
            NoSuchAlgorithmException, PasswordConstraintViolation, PasswordsNotMathException {
        final Account account = Account.findFirst("name = ?", username);

        if (null != account)
            throw new SuchAccountAlreadyExistsException(account);

        final Account joining = new Account(username, password.secret());

        joining.saveIt();

        return joining;
    }

    public Account request(final String username, final String password) {
        if (null == username || null == password)
            return null;

        Base.open(ds);

        final Account account = Account.findFirst("name = ?", username);

        Base.close();

        return account;
    }

    public String associate(final Account account) {
        final LocalDateTime expiration = LocalDateTime.now().plusSeconds(MAX_AGE_SECONDS);

        return associate(account, expiration);
    }

    public String associate(final Account account, final LocalDateTime expiration) {
        if (null == account)
            return null;

        final String key = UUID.randomUUID().toString();

        sessions.put(key, account);

        return key;
    }

    public Account associated(final String key) {
        return sessions.get(key);
    }
}
