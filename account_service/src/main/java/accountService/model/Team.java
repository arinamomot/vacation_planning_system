package accountService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@Entity
@NamedQueries({
        @NamedQuery(name = "Team.findByName", query = "SELECT t FROM Team t WHERE t.name = :name")
})
public class Team extends AbstractEntity {


    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    @Basic(optional = false)
    @Column(nullable = false)
    private int closestDayLimit;

    @Basic(optional = false)
    @Column(nullable = false)
    private int furthestDayLimit;

/*
    @OneToMany(fetch = FetchType.LAZY)
    private List<RequestToJoinTeam> requests;
 */

    @JsonIgnore
    @OneToMany(mappedBy = "team")
    @OrderBy("lastName")
    private List<User> users = new ArrayList<>();

    @JsonIgnore
    @OneToOne
    private User admin;

    //TODO: All, cascade type?
    @OneToMany(mappedBy = "team", orphanRemoval = true, cascade = CascadeType.REMOVE)
    @OrderBy("name")
    private List<Group> groups = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "TEAM_REASONS")
    @MapKeyColumn(name = "REASON")
    private Map<VacationReason, VacationPriority> reasons = new HashMap<>();

    @JsonIgnore
    @OneToMany (fetch = FetchType.EAGER)
    private List<VacationDay> vacationDays = new ArrayList<>();

    private Boolean removed = false;

    public Team() {
    }

    public Team(User admin) {
       this.users.add(admin);
       this.admin = admin;
    }

    public User getAdmin() { return admin; }

    public void setAdmin(User admin) {
        this.admin = admin;
        users.add(admin);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<VacationReason, VacationPriority> getReasons() {
        return reasons;
    }

    public void setReasons(Map<VacationReason, VacationPriority> reasons) {
        this.reasons = reasons;
    }

    public void addReason(VacationReason reason, VacationPriority vacationPriority){
        reasons.put(reason, vacationPriority);
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

    public void removeReason(String reason){
        reasons.remove(reason);
    }

    public VacationPriority getReasonsPriority(String reason){
        return reasons.get(reason);
    }

    public int getClosestDayLimit() {
        return closestDayLimit;
    }

    public void setClosestDayLimit(int closestDayLimit) {
        this.closestDayLimit = closestDayLimit;
    }

    public int getFurthestDayLimit() {
        return furthestDayLimit;
    }

    public void setFurthestDayLimit(int furthestDayLimit) {
        this.furthestDayLimit = furthestDayLimit;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        Objects.requireNonNull(user);
        if (users == null) {
            this.users = new ArrayList<>();
        }
        this.users.add(user);
    }

    public void removeUser(User user) {
        Objects.requireNonNull(user);
        final Iterator<User> it = users.iterator();
        while (it.hasNext()) {
            final User currentUser = it.next();
            if (currentUser.getId().equals(user.getId())) {
                it.remove();
                break;
            }
        }
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public void addGroup(Group group) {
        Objects.requireNonNull(group);
        if (groups == null) {
            this.groups = new ArrayList<>();
        }
        groups.add(group);
    }

    public void removeGroup(Group group) {
        Objects.requireNonNull(group);
        final Iterator<Group> it = groups.iterator();
        while (it.hasNext()) {
            final Group currentGroup = it.next();
            if (currentGroup.getId().equals(group.getId())) {
                it.remove();
                break;
            }
        }
    }

    public void removeTeam() {
        Objects.requireNonNull(this);
        for (User u : users) {
            u.setTeam(null);
            removeUser(u);
        }
        for (Group g : groups) {
            g.setTeam(null);
            removeGroup(g);
        }
        this.setName("");
        setRemoved(true);
    }

    public Boolean isRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", closestDayLimit=" + closestDayLimit +
                ", furthestDayLimit=" + furthestDayLimit +
                ", users=" + users +
                ", reasons=" + reasons +
                '}';
    }

    public List<VacationDay> getVacationDays() {
        return vacationDays;
    }

    public void setVacationDays(List<VacationDay> vacationDays) {
        this.vacationDays = vacationDays;
    }

    public Boolean getRemoved() {
        return removed;
    }
}
