package usopshiy.is.exception;

/**
 * Исключение, выбрасываемое при передаче некорректных данных
 * для создания или обновления предмета.
 */
public class InvalidItemDataException extends RuntimeException {

    public InvalidItemDataException(String message) {
        super(message);
    }

    public InvalidItemDataException(String message, Throwable cause) {
        super(message, cause);
    }
}