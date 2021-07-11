package holidayPlannerNotificationService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "NOTIFICATION_TYPE")
public abstract class Notification extends AbstractEntity {

    @JsonIgnore
    @OneToOne
    private User originator;

    // we only need the userId on the frontend to join with information from account service
    @JsonProperty
    public Integer getOriginatorUserId() {
        return originator.getUserId();
    }

    private Integer teamId;

    private String text;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime created;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getOriginator() {
        return originator;
    }

    public void setOriginator(User originatorUserId) {
        this.originator = originatorUserId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    @Column(name="NOTIFICATION_TYPE", insertable = true, updatable = false)
    protected String type;

    @JsonProperty
    public String getType() {
        return type;
    }
}
