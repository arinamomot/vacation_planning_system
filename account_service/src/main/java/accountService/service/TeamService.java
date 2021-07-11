package accountService.service;

import accountService.dao.TeamDao;
import accountService.dao.UserDao;
import accountService.model.Role;
import accountService.model.Team;
import accountService.model.User;
import accountService.model.VacationDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TeamService {

    private final TeamDao dao;
    private final UserDao userDao;

    @Autowired
    public TeamService(TeamDao dao, UserDao userDao) {
        this.dao = dao;
        this.userDao = userDao;
    }

    @Transactional
    public void createTeam(Team team) {
        Objects.requireNonNull(team);
        if (!exists(team.getName())) {
            dao.persist(team);
        }
    }

    @Transactional
    public void createTeam(Team team, int userId) {
        Objects.requireNonNull(team);
        Objects.requireNonNull(userId);
        User user = userDao.find(userId);
        if (user != null) {
            user.setAdmin(true);
            user.setRole(Role.ADMIN);
            team.setAdmin(user);
            team.addUser(user);
            dao.persist(team);
            user.setTeam(team);
            userDao.update(user);
        } else {
            throw new RuntimeException("Create team error");
        }

    }

    @Transactional
    public void addUserToTeam(Team team, int userId) {
        Objects.requireNonNull(team);
        User user = userDao.find(userId);
        if (exists(team.getName()) && !user.isAdmin()) {
            team.addUser(user);
            user.setTeam(team);
            userDao.update(user);
            dao.update(team);
        }
    }

    @Transactional
    public void updateTeam(Team team) {
        Objects.requireNonNull(team);
        if (exists(team.getName())) {
            dao.update(team);
        }
    }

    @Transactional
    public void deleteTeam(Team team) {
        Objects.requireNonNull(team);
        if (exists(team.getName())) {
            User admin = team.getAdmin();
            admin.setAdmin(false);
            userDao.update(admin);
            team.removeTeam();
            dao.update(team);
            dao.remove(team);
        } else {
            throw new RuntimeException("Team not deleted");
        }
    }

    @Transactional
    public Team find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public List<Team> findAll() {
        return dao.findAll();
    }

    @Transactional
    public boolean exists(String name) {
        return dao.findByName(name) != null;
    }


    public List<VacationDay> getVacationDaysAtConcreteDay(Team team, LocalDate day){
        List<VacationDay> result = new ArrayList<VacationDay>();
        for (VacationDay vacationDay : team.getVacationDays()){
            if (vacationDay.getDay().equals(day)){
                result.add(vacationDay);
            }
        }
        return result;
    }

}
