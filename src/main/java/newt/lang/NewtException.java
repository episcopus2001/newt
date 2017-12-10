package newt.lang;

public class NewtException extends RuntimeException {

    public NewtException() {}

    public NewtException(String message) {
        super(message);
    }

    public NewtException(Throwable cause) {
        super(cause);
    }

    public NewtException(String message, Throwable cause) {
        super(message, cause);
    }
}
