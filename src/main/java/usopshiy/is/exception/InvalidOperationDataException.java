package usopshiy.is.exception;

/**
 * Исключение, выбрасываемое при передаче некорректных данных для операции.
 */
public class InvalidOperationDataException extends RuntimeException {

    public InvalidOperationDataException(String message) {
        super(message);
    }

    public InvalidOperationDataException(String message, Throwable cause) {
        super(message, cause);
    }
}