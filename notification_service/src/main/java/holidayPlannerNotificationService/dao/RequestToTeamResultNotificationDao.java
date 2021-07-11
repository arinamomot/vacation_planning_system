package holidayPlannerNotificationService.dao;

import holidayPlannerNotificationService.model.RequestToTeamResultNotification;
import holidayPlannerNotificationService.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class RequestToTeamResultNotificationDao extends BaseDao<RequestToTeamResultNotification> {

    public RequestToTeamResultNotificationDao() {
        super(RequestToTeamResultNotification.class);
    }
    
    public List<RequestToTeamResultNotification> findAllByUser(User evaluatedUser) {
        try {
            return em.createNamedQuery("RequestToTeamResultNotification.findByUser", RequestToTeamResultNotification.class)
              .setParameter("evaluatedUser", evaluatedUser)
              .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
