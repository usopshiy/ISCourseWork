package usopshiy.is.exception;

/**
 * Исключение, выбрасываемое при передаче некорректных данных
 * для создания или обновления декоративного элемента.
 */
public class InvalidDecorationDataException extends RuntimeException {

    public InvalidDecorationDataException(String message) {
        super(message);
    }

    public InvalidDecorationDataException(String message, Throwable cause) {
        super(message, cause);
    }
}