package holidayPlannerNotificationService.dao;
import holidayPlannerNotificationService.model.RequestToTeamNotification;
import holidayPlannerNotificationService.model.StateOfRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class RequestToTeamNotificationDao extends BaseDao<RequestToTeamNotification> {

    public RequestToTeamNotificationDao() {
        super(RequestToTeamNotification.class);
    }

    public List<RequestToTeamNotification> findAllByTeamIdAndState(Integer teamId) {
        try {
            return em.createNamedQuery("RequestToTeam.findByTeamId", RequestToTeamNotification.class)
                    .setParameter("teamId", teamId)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
