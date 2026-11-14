package auth;

import static java.text.Normalizer.normalize;
import static java.text.Normalizer.Form.NFKC;
import static java.util.Locale.ROOT;

import java.util.Optional;

import org.jdbi.v3.core.Handle;

import jakarta.inject.Singleton;

@Singleton
public class AccountRepository {

    public Optional<AccountCredentials> findByUsername(Handle handle, String username) {
        return handle.createQuery("""
                    SELECT id, username, password_hash FROM accounts
                    WHERE username = :username
                """)
                .bind("username", requireNormalized(username))
                .mapTo(AccountCredentials.class)
                .findOne();
    }

    public Optional<Account> findById(Handle handle, long id) {
        return handle.createQuery("SELECT id, username FROM accounts WHERE id = :id")
                .bind("id", id)
                .mapTo(Account.class)
                .findOne();
    }

    public Account create(Handle handle, String username, String passwordHash) {
        final String normalized = requireNormalized(username);

        final Long id = handle.createUpdate("""
                    INSERT INTO accounts (username, password_hash)
                    VALUES (:username, :passwordHash)
                """)
                .bind("username", normalized)
                .bind("passwordHash", passwordHash)
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Long.class)
                .one();

        return new Account(id, normalized);
    }

    public boolean isEmpty(Handle handle) {
        return handle.createQuery("SELECT COUNT(*) FROM accounts")
                .mapTo(Long.class)
                .one() == 0;
    }

    private String requireNormalized(String username) {
        if (username == null) {
            throw new IllegalArgumentException("username is required");
        }

        final String normalized = normalize(username.strip(), NFKC).toLowerCase(ROOT);
        validateNormalized(normalized);
        return normalized;
    }

    private void validateNormalized(String normalized) {
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("username is required");
        }

        if (normalized.length() > 64) {
            throw new IllegalArgumentException("username must not exceed 64 characters");
        }
    }
}
