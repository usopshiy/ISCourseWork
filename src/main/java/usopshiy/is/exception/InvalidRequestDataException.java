package usopshiy.is.exception;

/**
 * Исключение, выбрасываемое при передаче некорректных данных для запроса.
 */
public class InvalidRequestDataException extends RuntimeException {

    public InvalidRequestDataException(String message) {
        super(message);
    }

    public InvalidRequestDataException(String message, Throwable cause) {
        super(message, cause);
    }
}