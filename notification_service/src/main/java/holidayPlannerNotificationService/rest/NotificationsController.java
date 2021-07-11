package holidayPlannerNotificationService.rest;

import holidayPlannerNotificationService.facade.NotificationFacade;
import holidayPlannerNotificationService.model.Notification;
import holidayPlannerNotificationService.rest.dto.VacationChangeDTO;
import holidayPlannerNotificationService.security.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.security.Principal;

import java.util.List;

@RestController
public class NotificationsController {

	private final NotificationFacade facade;

	@Autowired
	public NotificationsController(NotificationFacade facade) {
		this.facade = facade;
	}
	
	@PreAuthorize("hasAnyRole('ROLE_MAIN_ADMIN','ROLE_ADMIN','ROLE_USER')")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/notifications")
	public List<Notification> getAllNotifications(Principal principal) throws AuthenticationException {
		return facade.getAllNotifications(principal);
	}
	
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/notifications/vacation" )
	public ResponseEntity<Void> createVacationChangeNotification(@RequestBody VacationChangeDTO vacationChange,@AuthenticationPrincipal UserDetails principal){
		facade.createVacationChangeNotification(vacationChange, principal);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

}
