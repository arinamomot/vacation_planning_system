package holidayPlannerNotificationService.dao;

import holidayPlannerNotificationService.model.RequestToTeamNotification;
import holidayPlannerNotificationService.model.StateOfRequest;
import holidayPlannerNotificationService.model.User;
import holidayPlannerNotificationService.model.VacationChangeNotification;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class VacationChangeNotificationDao extends BaseDao<VacationChangeNotification> {

    public VacationChangeNotificationDao() {
        super(VacationChangeNotification.class);
    }

    public List<VacationChangeNotification> findAllByUser(User user) {
        try {
            return em.createNamedQuery("VacationChange.findByUser", VacationChangeNotification.class)
                    .setParameter("user", user)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<VacationChangeNotification> findAllByTeam(Integer teamId) {
        try {
            return em.createNamedQuery("VacationChange.findByAdmin", VacationChangeNotification.class)
                    .setParameter("teamId", teamId)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

}
