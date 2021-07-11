package holidayPlannerNotificationService.facade;

import holidayPlannerNotificationService.model.Notification;
import holidayPlannerNotificationService.model.User;
import holidayPlannerNotificationService.model.VacationChangeNotification;
import holidayPlannerNotificationService.rest.dto.VacationChangeDTO;
import holidayPlannerNotificationService.security.model.AuthenticationToken;
import holidayPlannerNotificationService.security.model.UserDetails;
import holidayPlannerNotificationService.service.RequestToTeamResultService;
import holidayPlannerNotificationService.service.RequestToTeamService;
import holidayPlannerNotificationService.service.VacationChangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationFacade.class);

    private final VacationChangeService vacationChangeService;
    private final RequestToTeamService requestToTeamService;
    private final RequestToTeamResultService requestToTeamResultService;

    public NotificationFacade(VacationChangeService vacationChangeService, RequestToTeamService requestToTeamService, RequestToTeamResultService requestToTeamResultService) {
        this.vacationChangeService = vacationChangeService;
        this.requestToTeamService = requestToTeamService;
        this.requestToTeamResultService = requestToTeamResultService;
    }

    public List<Notification> getAllNotifications(Principal principal) throws AuthenticationException {
        List<Notification> result = new ArrayList<>();
        final AuthenticationToken auth = (AuthenticationToken) principal;
        User user = auth.getPrincipal().getUser();

        result.addAll(vacationChangeService.findAllByUser(user));
        result.addAll(requestToTeamService.findAllByUser(user));
        result.addAll(requestToTeamResultService.findAllByUser(user));
        //TODO sort
        result = result.stream().sorted(Comparator.comparing(Notification::getCreated).reversed()).collect(Collectors.toList());
        return result;
    }

    public void createVacationChangeNotification(VacationChangeDTO vacationChange, UserDetails principal) {
        VacationChangeNotification vacationChangeNotification = vacationChangeService.createVacationChangeNotification(principal.getUser(), vacationChange);
        LOGGER.debug("VacationChangeNotification {} successfully created", vacationChangeNotification);
    }
}
