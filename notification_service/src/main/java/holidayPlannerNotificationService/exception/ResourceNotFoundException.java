package holidayPlannerNotificationService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public <T> ResourceNotFoundException(Class<T> clazz, Object identifier) {
        this(clazz.getSimpleName() + " identified by id = " + identifier + " not found");
    }

    public <T> ResourceNotFoundException(Class<T> clazz, Object identifier, String identifierType) {
        this(clazz.getSimpleName() + " identified by " + identifierType + " = " + identifier + " not found");
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

