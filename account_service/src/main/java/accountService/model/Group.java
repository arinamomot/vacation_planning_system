package accountService.model;

import accountService.security.SecurityUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "SUBGROUP")
@NamedQueries({
        @NamedQuery(name = "Group.findByName", query = "SELECT g FROM Group g WHERE g.name = :name")
})
public class Group extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "group")
    @OrderBy("lastName")
    private List<User> users = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "EMP_PER_DAY")
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "DAY")
    private Map<DayOfWeek, Integer> employeesPerDay = new HashMap<>();

    @JsonIgnore
    @ManyToOne
    private Team team;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        if (user != null){
            this.users.add(user);
        }
    }

    public void removeUser(User toBeRemoved) {
        this.users.removeIf(user -> user.getId().equals(toBeRemoved.getId()));
    }

    public Map<DayOfWeek, Integer> getEmployeesPerDay() {
        return employeesPerDay;
    }

    public void setEmployeesPerDay(Map<DayOfWeek, Integer> employeesPerDay) {
        this.employeesPerDay = employeesPerDay;
    }


    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", users=" + users +
                ", employeesPerDay=" + employeesPerDay +
                '}';
    }
}
