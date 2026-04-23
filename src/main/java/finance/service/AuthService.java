package finance.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.google.inject.Singleton;

import finance.objects.Account;

@Singleton
public class AuthService {
    private static final Map<Long, Account> sessions = new ConcurrentHashMap<>(); 

    public Account current(Long key) {
        return null;
    }

    public Account match(final String username, final String password) {
        final Account account = Account.findFirst("name = ?", username);

        return null;
    }

    public Long associate(final Account account, final LocalDateTime expiration) {
        final long key = UUID.randomUUID().timestamp();

        sessions.put(key, account);

        return key;
    }
}
