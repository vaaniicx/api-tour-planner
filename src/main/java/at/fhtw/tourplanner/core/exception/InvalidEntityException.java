package at.fhtw.tourplanner.core.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidEntityException extends RuntimeException {

    public InvalidEntityException(String message) {
        super(message);
    }
}
