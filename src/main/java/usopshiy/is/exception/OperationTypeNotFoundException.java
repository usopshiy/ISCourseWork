package usopshiy.is.exception;

/**
 * Исключение, выбрасываемое при попытке использования неподдерживаемого типа операции.
 */
public class OperationTypeNotFoundException extends RuntimeException {

    public OperationTypeNotFoundException(String message) {
        super(message);
    }

    public OperationTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}