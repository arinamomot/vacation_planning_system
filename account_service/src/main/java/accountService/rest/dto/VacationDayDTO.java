package accountService.rest.dto;

import accountService.model.VacationDay;
import accountService.model.VacationPriority;
import accountService.model.VacationReason;

import javax.persistence.*;
import java.time.LocalDate;

public class VacationDayDTO {
    @Enumerated(EnumType.STRING)
    private VacationPriority priority;
    @Enumerated(EnumType.STRING)
    private VacationReason reason;
    private LocalDate day;
    private String name;
    private Integer vacationTaker;

    public VacationDayDTO() {
    }

    public VacationDayDTO(VacationDay vacationDay) {
        this.priority = vacationDay.getPriority();
        this.reason = vacationDay.getReason();
        this.day = vacationDay.getDay();
        this.name = vacationDay.getName();
        this.vacationTaker = vacationDay.getVacationTaker().getId();
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
        this.reason = reason;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVacationTaker() {
        return vacationTaker;
    }

    public void setVacationTaker(Integer vacationTaker) {
        this.vacationTaker = vacationTaker;
    }
}
