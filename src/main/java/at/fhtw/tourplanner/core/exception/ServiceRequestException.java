package at.fhtw.tourplanner.core.exception;

public class ServiceRequestException extends RuntimeException {
    public ServiceRequestException(String message) {
        super(message);
    }

    public ServiceRequestException(String message, Throwable cause) {
      super(message, cause);
    }
}
