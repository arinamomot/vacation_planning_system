package accountService.facade;

import accountService.exception.NoAccessException;
import accountService.exception.NotFoundException;
import accountService.exception.PlannerException;
import accountService.model.Group;
import accountService.model.Team;
import accountService.model.User;
import accountService.security.SecurityUtils;
import accountService.service.GroupService;
import accountService.service.TeamService;
import accountService.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeamFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamFacade.class);

    private TeamService teamService;
    private UserService userService;
    private GroupService groupService;

    public TeamFacade(TeamService teamService, UserService userService, GroupService groupService) {
        this.teamService = teamService;
        this.userService = userService;
        this.groupService = groupService;
    }

    public void createTeam(Team team) {
        teamService.createTeam(team, SecurityUtils.getCurrentUser().getId());
        LOGGER.debug("Team {} successfully created", team);
    }

    public Team getTeam(int id) {
        final Team team = teamService.find(id);
        if (team == null)
            throw NotFoundException.create("Team", id);
        return team;
    }

    public void updateTeam(int id, Team updatedTeam) {
        if (!(teamService.find(id) != null && teamService.find(id).getId().equals(updatedTeam.getId())))
            throw NotFoundException.create("No such team", id);
        if (updatedTeam.getAdmin().equals(SecurityUtils.getCurrentUser())) {
            teamService.updateTeam(updatedTeam);
            LOGGER.debug("Team {} successfully updated", updatedTeam);
        } else {
            LOGGER.debug("Team {} NOT updated", updatedTeam);
            throw new NoAccessException("No access to this method");
        }
    }

    public Team getCurrentUserTeam() {
        return userService.getCurrentUserTeam();
    }

    public void removeTeam(int id) {
        final Team team = teamService.find(id);
        if (!(team.getId().equals(getCurrentUserTeam().getId()))) {
            LOGGER.debug("Team {} NOT removed", team);
            throw new PlannerException("Cannot remove someone's teams");
        }
        teamService.deleteTeam(team);
        LOGGER.debug("Team {} successfully removed", team);
    }

    public List<Group> getGroupsInTeam() {
        final Team team = getCurrentUserTeam();
        if (team == null)
            throw NotFoundException.create("Current user has no team", null);
        return team.getGroups();
    }

    public void addGroupToTeam(Group group) {
        final Team team = getCurrentUserTeam();
        if (team == null)
            throw NotFoundException.create("Current user has no team", null);
        team.addGroup(group);
        LOGGER.debug("Group {} successfully added to team {}", group, team);
    }

    public void updateGroupInTeam(int id, Group updatedGroup) {
        final Group group = groupService.find(id);
        if (!(group.getId().equals(updatedGroup.getId())))
            throw NotFoundException.create("No matching data in DB", id);
        if (group.getTeam().getAdmin().equals(SecurityUtils.getCurrentUser())) {
            groupService.updateGroup(updatedGroup);
            LOGGER.debug("Group {} successfully updated", updatedGroup);
        } else {
            LOGGER.debug("Group {} NOT updated", updatedGroup);
            throw new NoAccessException("No access to this method");
        }
    }

    public void addMemberToTeam(int userId) {
        final Team team = getCurrentUserTeam();
        if (team == null)
            throw NotFoundException.create("Current user has no team", null);
        final User user = userService.find(userId);
        if (user == null) {
            LOGGER.debug("No such user found");
            throw NotFoundException.create("User not found", null);
        }
        else {
            teamService.addUserToTeam(team,userId);
            userService.updateAccount(user);
            teamService.updateTeam(team);
            LOGGER.debug("User {} successfully added to team {}", user, team);
        }
    }

    public void deleteGroupInTeam(int id) {
        final Team team = getCurrentUserTeam();
        final Group group = groupService.find(id);
        if (group == null)
            throw NotFoundException.create("No such group", null);
        if (team == null)
            throw NotFoundException.create("Current user has no team", null);
        if (team.getAdmin().equals(SecurityUtils.getCurrentUser())) {
            if (team.getGroups().contains(group)) {
                team.removeGroup(group);
                LOGGER.debug("Group {} successfully removed from team {}", group, team);
            }
        } else {
            LOGGER.debug("Group {} NOT removed from team {}", group, team);
            throw new NoAccessException("No access to this method");
        }
    }

    public List<Team> getTeams() {
        return teamService.findAll();
    }

}
