package finance.errors;

public class PasswordsNotMathException extends Exception {
    @Override
    public String getMessage() {
        return "Given passwords are not the same.";
    }
}
