package accountService.model;

import accountService.rest.dto.VacationDayDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class VacationDay extends AbstractEntity {
    @Basic(optional = true)
    @Column(nullable = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private VacationPriority priority;

    @Enumerated(EnumType.STRING)
    private VacationReason reason;

    @JsonIgnore
    @ManyToOne
    private User vacationTaker;

    @Basic(optional = false)
    @Column(nullable = false)
    private LocalDate day;

    public VacationDay() {
    }

    public VacationDay(VacationDayDTO vacationDayDTO) {
        reason = vacationDayDTO.getReason();
        priority = vacationDayDTO.getPriority();
        day = vacationDayDTO.getDay();
        name = vacationDayDTO.getName();
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public VacationPriority getPriority() {
        return priority;
    }

    public void setPriority(VacationPriority priority) {
        this.priority = priority;
    }

    public VacationReason getReason() {
        return reason;
    }

    public void setReason(VacationReason reason) {
        if (vacationTaker.getTeam().getReasons().containsKey(reason)) {
            this.reason = reason;
            this.priority = vacationTaker.getTeam().getReasons().get(reason);
        }
    }

    public User getVacationTaker() {
        return vacationTaker;
    }

    public void setVacationTaker(User vacationTaker) {
        this.vacationTaker = vacationTaker;
    }

    @Override
    public String toString() {
        return "VacationDay{" +
                "priority=" + priority +
                ", reason='" + reason + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
