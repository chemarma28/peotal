package auth;

import java.util.Optional;

import org.jdbi.v3.core.Handle;

import io.jsonwebtoken.JwtException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class AuthService {

    private final AccountRepository accounts;
    private final PasswordHasher hasher;
    private final JwtService jwt;

    @Inject
    public AuthService(AccountRepository accounts, PasswordHasher hasher, JwtService jwt) {
        this.accounts = accounts;
        this.hasher = hasher;
        this.jwt = jwt;
    }

    public record LoginResult(Account account, String token) {
    }

    public LoginResult login(Handle handle, String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("username and password are required");
        }

        final AccountCredentials credentials;

        try {
            credentials = accounts.findByUsername(handle, username).orElse(null);
        } catch (IllegalArgumentException e) {
            throw new AuthException();
        }

        if (credentials == null || !hasher.verify(password, credentials.passwordHash)) {
            throw new AuthException();
        }

        return new LoginResult(new Account(credentials.id, credentials.username), jwt.issue(credentials.id));
    }

    public Account me(Handle handle, String token) {
        if (token == null || token.isBlank()) {
            throw new AuthException();
        }

        try {
            final long accountId = jwt.parse(token);
            return accounts.findById(handle, accountId).orElseThrow(AuthException::new);
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthException();
        }
    }

    public Optional<Account> bootstrap(Handle handle, String username, String password) {
        if (!accounts.isEmpty(handle)) {
            return Optional.empty();
        }

        if (username == null || username.isBlank() || password == null || password.isEmpty()) {
            throw new IllegalStateException(
                    "No account exists: set PEOTAL_USERNAME and PEOTAL_PASSWORD to create the first one");
        }

        return Optional.of(accounts.create(handle, username, hasher.hash(password)));
    }
}
