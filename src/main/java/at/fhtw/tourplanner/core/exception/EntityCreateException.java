package at.fhtw.tourplanner.core.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EntityCreateException extends RuntimeException {

    public EntityCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
