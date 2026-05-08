package finance.service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.javalite.activejdbc.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.objects.Account;
import finance.objects.Password;
import finance.objects.Password.PasswordConstraintViolation;
import finance.objects.Password.PasswordsNotMathException;

@Singleton
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public static final String SCOPE_ID = "SCOPE_ID";
    public static final int MAX_AGE_SECONDS = 3600 * 24;

    private static final Map<String, Account> sessions = new ConcurrentHashMap<>();

    @Inject
    private DataSource ds;

    public Account join(final String username, final Password password) throws NoSuchAlgorithmException,
            SuchAccountAlreadyExistsException, PasswordConstraintViolation, PasswordsNotMathException {
        Base.open(ds);

        final Account account = Account.findFirst("name = ?", username);

        if (null != account)
            throw new SuchAccountAlreadyExistsException();

        final Account joining = new Account(username, password.ensuredSecrect());

        joining.saveIt();

        Base.close();

        return joining;
    }

    public Account request(final String username, final Password password) {
        if (null == username || null == password)
            return null;

        Account account = null;

        Base.open(ds);

        try {
            account = Account.findFirst("name = ? AND secret = ?", username, password.secret());
        } catch (NoSuchAlgorithmException e) {
            logger.error("Unable verify credentials due errors: ", e);
        }

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

    public static class SuchAccountAlreadyExistsException extends Exception {
    }
}
