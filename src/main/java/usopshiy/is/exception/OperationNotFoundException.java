package usopshiy.is.exception;

/**
 * Исключение, выбрасываемое при попытке выполнения несуществующей операции.
 */
public class OperationNotFoundException extends RuntimeException {

    public OperationNotFoundException(String message) {
        super(message);
    }

    public OperationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}