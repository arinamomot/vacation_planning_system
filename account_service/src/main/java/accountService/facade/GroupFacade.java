package accountService.facade;

import accountService.exception.NoAccessException;
import accountService.exception.NotFoundException;
import accountService.model.Group;
import accountService.model.User;
import accountService.rest.dto.GroupDTO;
import accountService.security.SecurityUtils;
import accountService.service.GroupService;
import accountService.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupFacade.class);

    private GroupService groupService;
    private UserService userService;

    public GroupFacade(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    public void createGroup(Group group) {
        groupService.createGroup(group, userService.getCurrentUserTeam());
        LOGGER.debug("Group {} successfully created", group);
    }

    public GroupDTO getGroup(int id) {
        final Group group = groupService.find(id);
        if (group == null) {
            throw NotFoundException.create("Group", id);
        }
        return new GroupDTO(group);
    }

    public Group getCurrentUserGroup() {
        return userService.getCurrentUserGroup();
    }

    public void removeGroup(int id) {
        final Group group = groupService.find(id);
        if (group == null)
            throw NotFoundException.create("Group", id);
        if (group.getTeam().getAdmin().equals(SecurityUtils.getCurrentUser())) {
            groupService.deleteGroup(group);
            LOGGER.debug("Group {} successfully removed", group);
        } else {
            LOGGER.debug("Group {} NOT removed", group);
            throw new NoAccessException("No access to this method");
        }
    }

    public List<User> getGroupMembers() {
        final Group group = userService.getCurrentUserGroup();
        if (group == null)
            throw NotFoundException.create("Current user has no group", null);
        return groupService.getGroupUsers(userService.getCurrentUserGroup());
    }

    public void addUserToGroup(Integer id, Integer userId) {
        final Group group = groupService.find(id);
        final User user = userService.find(userId);
        if (group == null)
            throw NotFoundException.create("No such group", id);
//        TODO: refactor this out and reuse everywhere
        if (group.getTeam().getUsers().stream().anyMatch(u -> u.getId().equals(SecurityUtils.getCurrentUser().getId()))) {
            groupService.addUserToGroup(user, group);
            LOGGER.debug("User {} successfully added to group {}", user, group);
        } else {
            LOGGER.debug("User {} NOT added to group {}", user, group);
            throw new NoAccessException("No access to this method");
        }

    }

    public void removeUserFromGroup(Integer groupId, Integer userId) {
        final User user = userService.find(userId);
        final Group group = groupService.find(groupId);
        if (group == null)
            throw NotFoundException.create("No group for user", user.getId());
//        if (group.getUsers().contains(user)) {
        groupService.removeUserFromGroup(user, group);
        LOGGER.debug("User {} successfully removed from group {}", user, group);
//        } else {
//            LOGGER.debug("User {} NOT removed from group {}", user, group);
//            throw new NoAccessException("No access to this method");
//        }

    }

    public List<Group> getGroups() {
        return groupService.findAll();
    }

}
