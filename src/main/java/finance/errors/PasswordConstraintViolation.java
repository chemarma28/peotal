package finance.errors;

public class PasswordConstraintViolation extends Exception {
    @Override
    public String getMessage() {
        return "Password must contains at least 3 ASCII characters.";
    }
}
