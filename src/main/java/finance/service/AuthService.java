package finance.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.javalite.activejdbc.Base;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import finance.objects.Account;

@Singleton
public class AuthService {
    public static final String SCOPE_ID = "SCOPE_ID";

    private static final Map<String, Account> sessions = new ConcurrentHashMap<>();

    @Inject
    private DataSource ds;

    public Account match(final String username, final String password) {
        if (null == username || null == password)
            return null;

        Base.open(ds);

        final Account account = Account.findFirst("name = ?", username);

        Base.close();

        return account;
    }

    public String associate(final Account account) {
        final LocalDateTime expiration = LocalDateTime.now().plusDays(1);

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
