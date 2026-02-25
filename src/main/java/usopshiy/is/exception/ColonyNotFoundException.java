package usopshiy.is.exception;

/**
 * Исключение, выбрасываемое при попытке обращения к несуществующей колонии.
 */
public class ColonyNotFoundException extends RuntimeException {

    public ColonyNotFoundException(String message) {
        super(message);
    }
}