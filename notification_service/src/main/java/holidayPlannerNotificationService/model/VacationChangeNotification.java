package holidayPlannerNotificationService.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue("VACATION_CHANGE")
@NamedQueries({
  @NamedQuery(name = "VacationChange.findByAdmin", query = "SELECT v FROM VacationChangeNotification v WHERE v.teamId = :teamId"),
  @NamedQuery(name = "VacationChange.findByUser", query = "SELECT v FROM VacationChangeNotification v WHERE v.originator = :user")
})
public class VacationChangeNotification extends Notification {

    @Override
    public String toString() {
        return "ChangeOfPriorityNotification{}";
    }

    public String getNotificationType() {
        return "VACATION_CHANGE";
    }

}
