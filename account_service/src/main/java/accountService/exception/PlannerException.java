package accountService.exception;

/**
 * Base for all application-specific exceptions.
 */
public class PlannerException extends RuntimeException {
    public PlannerException() {
    }

    public PlannerException(String message) {
        super(message);
    }

    public PlannerException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlannerException(Throwable cause) {
        super(cause);
    }
}
