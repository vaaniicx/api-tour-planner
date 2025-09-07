package at.fhtw.tourplanner.core.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EntityUpdateException extends RuntimeException {

    public EntityUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
