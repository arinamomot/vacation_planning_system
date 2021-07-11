package accountService.service;

import accountService.dao.GroupDao;
import accountService.dao.TeamDao;
import accountService.dao.UserDao;
import accountService.model.Group;
import accountService.model.Team;
import accountService.model.User;
import accountService.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GroupService {

    private final GroupDao dao;

    private final UserDao userDao;

    private final TeamDao teamDao;

    @Autowired
    public GroupService(GroupDao dao, UserDao userDao, TeamDao teamDao) {
        this.dao = dao;
        this.userDao = userDao;
        this.teamDao = teamDao;
    }

    @Transactional
    public void createGroup(Group group, Team team) {
        Objects.requireNonNull(group);
        if (!exists(group.getName())) {
            group.setTeam(team);
            dao.persist(group);
            team.addGroup(group);
            teamDao.update(team);
        }
        //LOG OR EXCEPTION EXPECTED
    }

    @Transactional
    public void createGroup(Group group) {
        Objects.requireNonNull(group);
        if (!exists(group.getName())) {
            dao.persist(group);
        }
        //LOG OR EXCEPTION EXPECTED
    }

    @Transactional
    public void deleteGroup(Group group) {
        Objects.requireNonNull(group);
        if (exists(group.getName())) {
            Team team = group.getTeam();
            team.removeGroup(group);
            for (User u : group.getUsers()) {
                u.setGroup(null);
            }
            dao.remove(group);
        }
    }

    @Transactional
    public void updateGroup(Group group) {
        Objects.requireNonNull(group);
        if (exists(group.getName())) {
            dao.update(group);
        }
    }

    @Transactional
    public Group find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public List<Group> findAll() {
        return dao.findAll();
    }

    @Transactional
    public List<User> getGroupUsers(Group group) {
        Objects.requireNonNull(group);
        if (exists(group.getName())) {
            return group.getUsers();
        }
        return null; //no users in group
    }

    @Transactional
    public void addGroupToTeam(Group group, Team team) {
        Objects.requireNonNull(group);
        Objects.requireNonNull(team);
        if (exists(team.getName())) {
            if (!team.getGroups().contains(group)) {
                team.addGroup(group);
                group.setTeam(team);
                dao.update(group);
                teamDao.update(team);
            }
        }
    }

    //TODO: updatovat i usera
    @Transactional
    public void removeGroupFromTeam(Group group, Team team) {
        Objects.requireNonNull(group);
        Objects.requireNonNull(team);
        if (exists(team.getName())) {
            if (team.getGroups().contains(group)) {
                team.removeGroup(group);
                group.setTeam(null);
                dao.update(group);
                teamDao.update(team);
            }
        }
    }

    @Transactional
    public void addUserToGroup(User user, Group group) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(group);

        Optional<Group> previousGroup = group.getTeam().getGroups().stream().filter(g -> g.getUsers().stream().anyMatch(u -> u.getId().equals(user.getId()))).findAny();
        if (previousGroup.isPresent()){
            removeUserFromGroup(user, previousGroup.get());
        }

//      TODO: add validation that works here (previous one errored out)
//        if (user.getTeam().getGroups().contains(group)) {
        user.setGroup(group);
        group.addUser(user);
        dao.update(group);
        userDao.update(user);
//            } else {
//                throw new RuntimeException("did not add user to group!");   1
//            }
    }

    @Transactional
    public void removeUserFromGroup(User user, Group group) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(group);
//        if (exists(user.getUsername()) && exists(group.getName())) {
//        if (user.getGroup().equals(group)) {
        user.setGroup(null);
        group.removeUser(user);
        dao.update(group);
        userDao.update(user);
//        }
//        }
    }

    @Transactional
    public boolean exists(String name) {
        return dao.findByName(name) != null;
    }

    @Transactional
    public boolean isGroupMember(Group group){
        return group.getTeam().getUsers().stream().anyMatch(u -> u.getId().equals(SecurityUtils.getCurrentUser().getId()));
    }

}
