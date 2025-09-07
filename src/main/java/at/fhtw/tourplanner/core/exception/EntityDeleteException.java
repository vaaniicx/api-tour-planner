package at.fhtw.tourplanner.core.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EntityDeleteException extends RuntimeException {

    public EntityDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
