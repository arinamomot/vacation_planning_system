package accountService.model;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
//cannot be User
@Table(name = "CLIENT")
@NamedQueries({
        @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
})
@RedisHash("User")
public class User extends AbstractEntity implements Serializable {

    @Basic(optional = false)
    @Column(nullable = false)
    private boolean admin = false;

    @Basic(optional = false)
    @Column(nullable = false)
    private String firstName;

    @Basic(optional = false)
    @Column(nullable = false)
    private String lastName;

    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Basic(optional = false)
    @Column(nullable = false)
    private String position;

    private int sickDaysPerYear = 15;

    private int vacationDaysPerYear = 20;

    @OneToMany(mappedBy = "vacationTaker", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<VacationDay> vacationDays = new ArrayList<>();

    /*
    @OneToOne(mappedBy = "requester", cascade = CascadeType.REMOVE)
    private RequestToJoinTeam request;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ChangeOfPriorityNotification> priorityNotifications = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CollisionNotification> collisionNotifications = new ArrayList<>();
     */

    @ManyToOne(fetch = FetchType.EAGER)
    private Group group;

    @ManyToOne(fetch = FetchType.EAGER)
    private Team team;

    @Transient
    private List<DayOfWeek> nonWorkingDay;

    public boolean isAdmin() {
        return admin;
    }

    public User() {
        this.role = Role.GUEST;
    }

    public Role getRole() {
        return role;
    }

    public void setRole() {
        if (isAdmin()) {
            this.role = Role.ADMIN;
        } else{
            this.role = Role.USER;
        }
    }

    public void setRole(Role role) {
        this.role = role;
        if (role == Role.ADMIN ){
            setAdmin(true);
        } else{
            setAdmin(false);
        }
    }

    public void addVacationDay (VacationDay vacationDay) {
        Objects.requireNonNull(vacationDay);
        if (vacationDays == null){
            vacationDays = new ArrayList<>();
        }
        vacationDays.add(vacationDay);
    }

    public void removeVacationDay (VacationDay vacationDay) {
        Objects.requireNonNull(vacationDay);
        if (vacationDays == null){
            return;
        }
        vacationDays.removeIf(v -> Objects.equals(v.getId(), vacationDay.getId()));
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
        setRole();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void encodePassword(PasswordEncoder encoder) {
        this.password = encoder.encode(password);
    }

    public void erasePassword() {
        this.password = null;
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

    public List<DayOfWeek> getNonWorkingDay() {
        return nonWorkingDay;
    }

    public void setNonWorkingDay(List<DayOfWeek> workingDay) {
        this.nonWorkingDay = workingDay;
    }

    public List<VacationDay> getVacationDays() {
        return vacationDays;
    }

    public void setVacationDays(List<VacationDay> vacationDays) {
        this.vacationDays = vacationDays;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "User{" +
                 firstName + " " +
                 lastName + " " +
                "(" + username + ")" +
                '}';
    }
}
