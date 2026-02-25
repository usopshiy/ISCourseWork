package usopshiy.is.exception;

/**
 * Исключение, выбрасываемое при попытке обращения к несуществующему запросу.
 */
public class RequestNotFoundException extends RuntimeException {

    public RequestNotFoundException(String message) {
        super(message);
    }

    public RequestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}