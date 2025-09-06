package at.fhtw.tourplanner.core.exception;

public class EntityDeleteException extends RuntimeException {
    public EntityDeleteException(String message) {
        super(message);
    }

    public EntityDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
