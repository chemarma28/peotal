package finance.objects;

import static org.apache.commons.lang3.StringUtils.isAsciiPrintable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import finance.errors.PasswordConstraintViolation;
import finance.errors.PasswordsNotMathException;

public class Password {
    private final String password;
    private final String repeated;

    public Password(final String password, final String repeated) {
        this.password = password;
        this.repeated = repeated;
    }

    public static boolean valid(final String password) {
        if (null == password)
            return false;

        boolean valid = true;

        valid = valid && isAsciiPrintable(password);

        valid = valid && 2 < password.length();

        return valid;
    }

    public String secret() throws PasswordConstraintViolation, PasswordsNotMathException, NoSuchAlgorithmException {
        if (!valid(password))
            throw new PasswordConstraintViolation();

        if (null == repeated || !password.equals(repeated))
            throw new PasswordsNotMathException();

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
}
