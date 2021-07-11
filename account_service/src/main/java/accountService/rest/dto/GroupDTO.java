package accountService.rest.dto;

import accountService.model.Group;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupDTO {
    private Integer id;
    private String name;
    private List<TeamMemberDTO> members = new ArrayList<>();
    private Map<DayOfWeek, Integer> employeesPerDay = new HashMap<>();

    public GroupDTO(Group group) {
        this.id = group.getId();
        this.name = group.getName();
        this.members = group.getUsers().stream().map(TeamMemberDTO::new).collect(Collectors.toList());
        this.employeesPerDay = group.getEmployeesPerDay();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TeamMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<TeamMemberDTO> members) {
        this.members = members;
    }

    public Map<DayOfWeek, Integer> getEmployeesPerDay() {
        return employeesPerDay;
    }

    public void setEmployeesPerDay(Map<DayOfWeek, Integer> employeesPerDay) {
        this.employeesPerDay = employeesPerDay;
    }
}
