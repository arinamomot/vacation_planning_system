package holidayPlannerNotificationService.exception;

public class NoAccessException extends PlannerException {

    public NoAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAccessException(Throwable cause) {
        super(cause);
    }

    public NoAccessException(String message) {
        super(message);
    }

}
