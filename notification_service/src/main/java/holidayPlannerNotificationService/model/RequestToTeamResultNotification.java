package holidayPlannerNotificationService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;

@Entity
@DiscriminatorValue("REQUEST_RESULT")
@NamedQueries({
  @NamedQuery(name = "RequestToTeamResultNotification.findByUser", query = "SELECT u FROM RequestToTeamResultNotification u WHERE u.evaluatedUser = :evaluatedUser"),
})
public class RequestToTeamResultNotification extends Notification {

    @OneToOne
    @JsonIgnore
    private User evaluatedUser; // who got accepted or declined to team, originator is admin in this case

    // we only need the userId on the frontend to join with information from account service
    @JsonProperty
    public Integer getEvaluatedUserId() {
        return evaluatedUser.getUserId();
    }

    private boolean successful; // whether accepted or declined

    public User getEvaluatedUser() {
        return evaluatedUser;
    }

    public void setEvaluatedUser(User evaluatedUser) {
        this.evaluatedUser = evaluatedUser;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getNotificationType() {
        return "REQUEST_TO_TEAM_RESULT";
    }
}
