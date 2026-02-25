package usopshiy.is.exception;

/**
 * Исключение, выбрасываемое при попытке обращения к несуществующему предмету.
 */
public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(String message) {
        super(message);
    }

    public ItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}