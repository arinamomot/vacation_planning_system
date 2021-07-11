package holidayPlannerNotificationService.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@DiscriminatorValue("REQUEST_TO_TEAM")
@NamedQueries({
        @NamedQuery(name = "RequestToTeam.findByTeamId", query = "SELECT t FROM RequestToTeamNotification t WHERE t.teamId = :teamId"),
})
public class RequestToTeamNotification extends Notification {
    private StateOfRequest state;

    public StateOfRequest getState() {
        return state;
    }

    public void setState(StateOfRequest state) {
        this.state = state;
    }

    @Override
    public int hashCode() {
        int result = getCreated() != null ? getCreated().hashCode() : 0;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (getOriginator() != null ? getOriginator().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RequestToJoinTeam{" +
                "created=" + this.getCreated() +
                ", state=" + state +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestToTeamNotification that = (RequestToTeamNotification) o;

        if (!Objects.equals(this.getCreated(), that.getCreated())) return false;
        if (state != that.getState()) return false;
        return Objects.equals(this.getOriginator(), that.getOriginator());
    }

    public String getNotificationType() {
        return "REQUEST_TO_TEAM";
    }
}
