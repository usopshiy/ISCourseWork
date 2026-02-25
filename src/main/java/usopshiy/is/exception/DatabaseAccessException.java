package usopshiy.is.exception;

/**
 * Исключение, выбрасываемое при ошибках доступа к базе данных.
 */
public class DatabaseAccessException extends RuntimeException {

    public DatabaseAccessException(String message) {
        super(message);
    }

    public DatabaseAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}