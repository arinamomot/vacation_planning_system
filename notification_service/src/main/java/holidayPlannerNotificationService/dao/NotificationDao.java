package holidayPlannerNotificationService.dao;
import holidayPlannerNotificationService.model.Notification;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationDao extends BaseDao<Notification> {

    public NotificationDao() {
        super(Notification.class);
    }
}
