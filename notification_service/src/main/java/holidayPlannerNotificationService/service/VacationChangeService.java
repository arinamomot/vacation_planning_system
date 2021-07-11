package holidayPlannerNotificationService.service;

import holidayPlannerNotificationService.dao.UserDao;
import holidayPlannerNotificationService.dao.VacationChangeNotificationDao;
import holidayPlannerNotificationService.model.RequestToTeamNotification;
import holidayPlannerNotificationService.model.StateOfRequest;
import holidayPlannerNotificationService.model.User;
import holidayPlannerNotificationService.model.VacationChangeNotification;
import holidayPlannerNotificationService.rest.dto.UserRegistrationDTO;
import holidayPlannerNotificationService.rest.dto.VacationChangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

// TODO
@Service
public class VacationChangeService {

	private final UserDao userDao;
	private final VacationChangeNotificationDao vacationChangeNotificationDao;

	@Autowired
	public VacationChangeService(UserDao userDao, VacationChangeNotificationDao vacationChangeNotificationDao) {
		this.userDao = userDao;
		this.vacationChangeNotificationDao = vacationChangeNotificationDao;
	}

	@Transactional
	public List<VacationChangeNotification> findAllByUser(User user) throws AuthenticationException {
		Objects.requireNonNull(user);
		if (user.isAdmin()) {
			return vacationChangeNotificationDao.findAllByTeam(user.getTeamId());
		}
		return vacationChangeNotificationDao.findAllByUser(user);
	}

	@Transactional
	public VacationChangeNotification createVacationChangeNotification(User user, VacationChangeDTO vacationChange) {
		Objects.requireNonNull(user);

		// TODO:
		final VacationChangeNotification notification = new VacationChangeNotification();
		notification.setCreated(LocalDateTime.now());
		notification.setOriginator(user);
		notification.setText(vacationChange.getChanges());
		notification.setTeamId(user.getTeamId());

		vacationChangeNotificationDao.persist(notification);

		return notification;
	}
}
