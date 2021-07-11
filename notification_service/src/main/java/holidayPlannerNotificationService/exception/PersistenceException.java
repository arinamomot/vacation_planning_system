package holidayPlannerNotificationService.exception;

public class PersistenceException extends PlannerException{

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }
}
