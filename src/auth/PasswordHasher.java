package auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class PasswordHasher {

    private final int cost;

    @Inject
    public PasswordHasher() {
        this(12);
    }

    public PasswordHasher(int cost) {
        this.cost = cost;
    }

    public String hash(String password) {
        return BCrypt.withDefaults().hashToString(cost, password.toCharArray());
    }

    public boolean verify(String password, String hash) {
        if (password == null || hash == null) {
            return false;
        }

        try {
            return BCrypt.verifyer().verify(password.toCharArray(), hash).verified;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
