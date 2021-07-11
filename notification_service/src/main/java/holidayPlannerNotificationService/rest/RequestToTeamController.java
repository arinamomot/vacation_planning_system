package holidayPlannerNotificationService.rest;

import holidayPlannerNotificationService.facade.RequestToTeamFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.security.Principal;


@RestController
@RequestMapping("/team/{teamId}/join-request")
public class RequestToTeamController {

	private final RequestToTeamFacade facade;

	@Autowired
	public RequestToTeamController(RequestToTeamFacade facade) {
		this.facade = facade;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping
	public ResponseEntity<Void> createRequestToTeam(Principal principal, @PathVariable Integer teamId) {
		facade.createRequestToTeam(principal, teamId);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_MAIN_ADMIN','ROLE_ADMIN')")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{requestId}/accept")
	public void acceptRequestToTeam(Principal principal, @PathVariable Integer requestId) throws AuthenticationException {
		facade.acceptRequestToTeam(principal, requestId);
	}

	@PreAuthorize("hasAnyRole('ROLE_MAIN_ADMIN','ROLE_ADMIN')")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{requestId}/decline")
	public void declineRequestToTeam(Principal principal, @PathVariable Integer requestId) throws AuthenticationException {
		facade.declineRequestToTeam(principal, requestId);
	}
}
