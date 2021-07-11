package holidayPlannerNotificationService.service;

import holidayPlannerNotificationService.dao.RequestToTeamResultNotificationDao;
import holidayPlannerNotificationService.model.RequestToTeamResultNotification;
import holidayPlannerNotificationService.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

// TODO
@Service
public class RequestToTeamResultService {
	
//	private final UserDao userDao;
	private final RequestToTeamResultNotificationDao requestToTeamResultNotificationDao;
	
	@Autowired
	public RequestToTeamResultService(RequestToTeamResultNotificationDao requestToTeamResultNotificationDao) {
		this.requestToTeamResultNotificationDao = requestToTeamResultNotificationDao;
	}
	
	@Transactional
	public List<RequestToTeamResultNotification> findAllByUser(User user) throws AuthenticationException {
		Objects.requireNonNull(user);
		return requestToTeamResultNotificationDao.findAllByUser(user);
	}

	@Transactional
	public RequestToTeamResultNotification persist(boolean successful, User evaluatedUser, User originator){
		RequestToTeamResultNotification notification = new RequestToTeamResultNotification();
		notification.setSuccessful(successful);
		notification.setEvaluatedUser(evaluatedUser);
		notification.setCreated(LocalDateTime.now());
		notification.setTeamId(originator.getTeamId());
		notification.setOriginator(originator);

		requestToTeamResultNotificationDao.persist(notification);

		return notification;
	}
}
