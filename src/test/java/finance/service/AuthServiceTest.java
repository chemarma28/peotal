package finance.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import finance.objects.Password;
import finance.objects.Password.PasswordConstraintViolation;
import finance.objects.Password.PasswordsNotMathException;

public class AuthServiceTest {
    @Test
    public void usual() throws NoSuchAlgorithmException, PasswordConstraintViolation, PasswordsNotMathException {
        final String password = "letmein";
        final String letmein = "letmein";

        final String sha256 = "1c8bfe8f801d79745c4631d09fff36c82aa37fc4cce4fc946683d7b336b63032";

        final String secret = new Password(password, letmein).ensuredSecrect();

        Assertions.assertEquals(sha256, secret);
    }

    @Test
    public void when_passwords_do_not_match_throw_exception() {
        final String letmein = "letmein";
        final String notletmein = "notletmein";

        assertThrows(PasswordsNotMathException.class, () -> new Password(letmein, notletmein).ensuredSecrect());
    }

    @Test
    public void when_null_password_then_throw_exception() {
        final String password = null;

        assertThrows(PasswordConstraintViolation.class, () -> new Password(password, null).ensuredSecrect());
    }

    @Test
    public void when_password_contains_less_than_5_characters_then_throw_exception() {
        final String password = "p";

        assertThrows(PasswordConstraintViolation.class, () -> new Password(password, null).ensuredSecrect());
    }
}
