package finance.objects;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Password {
    private final String password;
    private String confirmation;

    public Password(final String password) {
        this.password = password;
    }

    public Password(final String password, final String confirmation) {
        this.password = password;
        this.confirmation = confirmation;
    }

    public void validate(final String password) throws PasswordConstraintViolation {
        if (null == password)
            throw new PasswordConstraintViolation();

        boolean valid = 5 <= password.length() && password.length() <= 20;

        if (!valid)
            throw new PasswordConstraintViolation();
    }

    public String ensuredSecrect()
            throws PasswordConstraintViolation, PasswordsNotMathException, NoSuchAlgorithmException {
        validate(password);

        if (null == confirmation || !password.equals(confirmation))
            throw new PasswordsNotMathException();

        return digest();
    }

    public String secret() throws NoSuchAlgorithmException {
        return digest();
    }

    private String digest() throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");

        final byte[] bytes = digest.digest(password.getBytes());

        final StringBuilder bf = new StringBuilder();

        for (byte b : bytes)
            bf.append(String.format("%x", b));

        return bf.toString();
    }

    public static class PasswordConstraintViolation extends Exception {
        public List<String> requirements() {
            return List.of("5-20 characters");
        }

        @Override
        public String getMessage() {
            return StringUtils.join(requirements(), ", ");
        }
    }

    public static class PasswordsNotMathException extends Exception {
    }
}
