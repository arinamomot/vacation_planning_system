package accountService.rest.dto;

import accountService.model.Role;
import accountService.model.User;

public class TeamMemberDTO {
    private boolean admin;
    private String firstName;
    private String lastName;
    private String username;
    private Role role;
    private String position;
    private int sickDaysPerYear;
    private int vacationDaysPerYear;
    private Integer id;

    public TeamMemberDTO(User user) {
        this.admin = user.isAdmin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.position = user.getPosition();
        this.sickDaysPerYear = user.getSickDaysPerYear();
        this.vacationDaysPerYear = user.getVacationDaysPerYear();
        this.id = user.getId();
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getSickDaysPerYear() {
        return sickDaysPerYear;
    }

    public void setSickDaysPerYear(int sickDaysPerYear) {
        this.sickDaysPerYear = sickDaysPerYear;
    }

    public int getVacationDaysPerYear() {
        return vacationDaysPerYear;
    }

    public void setVacationDaysPerYear(int vacationDaysPerYear) {
        this.vacationDaysPerYear = vacationDaysPerYear;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
