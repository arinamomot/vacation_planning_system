package holidayPlannerNotificationService.facade;

import holidayPlannerNotificationService.exception.ResourceNotFoundException;
import holidayPlannerNotificationService.model.RequestToTeamNotification;
import holidayPlannerNotificationService.model.User;
import holidayPlannerNotificationService.security.model.AuthenticationToken;
import holidayPlannerNotificationService.service.RequestToTeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.security.Principal;

@Component
public class RequestToTeamFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestToTeamFacade.class);

    private RequestToTeamService requestToTeamService;

    public RequestToTeamFacade(RequestToTeamService requestToTeamService) {
        this.requestToTeamService = requestToTeamService;
    }

    public void createRequestToTeam(Principal principal, int teamId) {
        final AuthenticationToken auth = (AuthenticationToken) principal;
        final User user = auth.getPrincipal().getUser();

        requestToTeamService.createRequestToTeam(user, teamId);
    }

    public void acceptRequestToTeam(Principal principal, int requestId) throws AuthenticationException {
        final AuthenticationToken auth = (AuthenticationToken) principal;
        User user = auth.getPrincipal().getUser();

        RequestToTeamNotification request = requestToTeamService.find(requestId);
        if (request == null){
            throw new ResourceNotFoundException(RequestToTeamNotification.class, requestId);
        }
        requestToTeamService.acceptRequestToTeam(user, request);
        LOGGER.info("Request accepted");
    }

    public void declineRequestToTeam(Principal principal, int requestId) throws AuthenticationException {
        final AuthenticationToken auth = (AuthenticationToken) principal;
        User user = auth.getPrincipal().getUser();

        RequestToTeamNotification request = requestToTeamService.find(requestId);
        if (request == null){
            throw new ResourceNotFoundException(RequestToTeamNotification.class, requestId);
        }
        requestToTeamService.declineRequestToTeam(user, request);
        LOGGER.info("Request decline");
    }
}
