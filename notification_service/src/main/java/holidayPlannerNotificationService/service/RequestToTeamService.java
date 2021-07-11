package holidayPlannerNotificationService.service;

import holidayPlannerNotificationService.dao.RequestToTeamNotificationDao;
import holidayPlannerNotificationService.dao.UserDao;
import holidayPlannerNotificationService.exception.ValidationException;
import holidayPlannerNotificationService.model.RequestToTeamNotification;
import holidayPlannerNotificationService.model.Role;
import holidayPlannerNotificationService.model.StateOfRequest;
import holidayPlannerNotificationService.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RequestToTeamService {

    private final UserDao userDao;
    private final RequestToTeamNotificationDao requestToTeamNotificationDao;
    private final RequestToTeamResultService requestToTeamResultService;

    @Autowired
    public RequestToTeamService(UserDao userDao, RequestToTeamNotificationDao requestToTeamNotificationDao, RequestToTeamResultService requestToTeamResultService) {
        this.userDao = userDao;
        this.requestToTeamNotificationDao = requestToTeamNotificationDao;
        this.requestToTeamResultService = requestToTeamResultService;
    }

    @Transactional
    public void createRequestToTeam(User user, Integer teamId) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(teamId);

        if (user.getTeamId() != null) {
            throw new ValidationException("You cannot request to join another team while being member of one team already!");
        }

        if (!user.isAdmin()) {
            final RequestToTeamNotification request = new RequestToTeamNotification();
            request.setState(StateOfRequest.IN_PROGRESS);
            request.setOriginator(user);
            request.setTeamId(teamId);
            request.setCreated(LocalDateTime.now());
            requestToTeamNotificationDao.persist(request);
        }
    }

    @Transactional
    public List<RequestToTeamNotification> findAllByUser(User admin) {
        Objects.requireNonNull(admin);
        if (!admin.isAdmin()) {
            return new ArrayList<>();
        }

        return requestToTeamNotificationDao.findAllByTeamIdAndState(admin.getTeamId());
    }


    @Transactional
    public void acceptRequestToTeam(User admin, RequestToTeamNotification request) throws AuthenticationException {
        Objects.requireNonNull(admin);
        Objects.requireNonNull(request);


        if (!(admin.getTeamId().equals(request.getTeamId()) || admin.getRole().equals(Role.MAIN_ADMIN))) {
            throw new AuthenticationException("User is not authorized for this operation!");
        }

        User requester = userDao.findByUserId(request.getOriginator().getUserId());
        if (requester.getTeamId() != null) {
            // TODO: might have to be handled to avoid this situation
            throw new ValidationException("User cannot be accepted into a new team while already a member of one!");
        }

        request.setState(StateOfRequest.ACCEPTED);
        //notify user
        requester.setTeamId(request.getTeamId());
        userDao.update(requester);
        requestToTeamNotificationDao.update(request);
        requestToTeamResultService.persist(true, requester, admin);
    }

    @Transactional
    public void declineRequestToTeam(User admin, RequestToTeamNotification request) throws AuthenticationException {
        Objects.requireNonNull(admin);
        Objects.requireNonNull(request);

        if (!(admin.getTeamId().equals(request.getTeamId()) || admin.getRole().equals(Role.MAIN_ADMIN))) {
            throw new AuthenticationException("User is not authorized for this operation!");
        }

        User requester = userDao.findByUserId(request.getOriginator().getUserId());
        request.setState(StateOfRequest.DECLINED);
        //notify user
        requestToTeamNotificationDao.update(request);
        requestToTeamResultService.persist(false, requester, admin);
    }

    @Transactional
    public RequestToTeamNotification find(Integer requestId) {
        return requestToTeamNotificationDao.find(requestId);
    }
}
