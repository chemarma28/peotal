package finance.errors;

public class Errors {
    public final static String INTERNAL;

    static {
        INTERNAL = String.join("",
                "Sorry, this is not working properly due internal server errors. ",
                "Try again later, we are working to fix it.");
    }
}
