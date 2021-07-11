package accountService.service;

import accountService.dao.UserDao;
import accountService.model.*;
import accountService.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserDao dao;
    private final TeamService teamService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao dao, PasswordEncoder passwordEncoder, TeamService teamService) {
        this.dao = dao;
        this.passwordEncoder = passwordEncoder;
        this.teamService = teamService;
    }

    @Transactional
    public User createAccount(User user) {
        Objects.requireNonNull(user);
        user.encodePassword(passwordEncoder);
        if (!exists(user.getUsername())) {
            user.setAdmin(false);
            //user.setRole(Role.USER);
            dao.persist(user);
        }

        return user;
    }

    @Transactional
    @CacheEvict(key = "#user.id",value = "User")
    public void deleteAccount(User user) {
        Objects.requireNonNull(user);
        if (exists(user.getUsername())) {
            User toBeDeleted = dao.find(user.getId());
            dao.remove(toBeDeleted);
        }
    }

    @Transactional
    @CachePut(value = "User", key = "#user.id")
    public User updateAccount(User user) {
        Objects.requireNonNull(user);
        User result = user;
        if (exists(user.getUsername())) {
            result = dao.update(user);
        }

        return result;
    }

    @Transactional
    @Cacheable(key = "#id",value = "User")
    public User find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public List<User> findAll() {
        return dao.findAll();
    }

    @Transactional
    public void removeTeammate(User user) {
        user.getTeam().removeUser(user);
        teamService.updateTeam(user.getTeam());
        user.setTeam(null);
        dao.update(user);
    }

    @Transactional
    public Group getTeammateGroup(User user) {
        Objects.requireNonNull(user);
        if (exists(user.getUsername())) {
            return user.getGroup();
        }
        return null; //user does not exist
    }

    @Transactional
    public void addAdmin(User user) {
        Objects.requireNonNull(user);
        if (!user.isAdmin()) {
            user.setAdmin(true);
        }
        dao.update(user);
    }

    @Transactional
    public List<VacationDay> displayVacationDays(User user) {
        Objects.requireNonNull(user);
        if (exists(user.getUsername()))
            return user.getVacationDays();
        return null;
    }

    public boolean canTakeSickDay(User user, LocalDate day){
        //tady se bude kontrolovat jestli clovek ma jeste dost dnu na SickDay tenhle rok
        int k = 0;
        for (VacationDay day1: user.getVacationDays()){
            if (day1.getReason().equals(VacationReason.HOSPITAL)){
                k++;
            }
        }
        if (user.getSickDaysPerYear() <= k)
            return false;
        //anebo jestli clovek nema ten den nepracovni den
        for (DayOfWeek dayOfWeek: user.getNonWorkingDay()){
            if (dayOfWeek == day.getDayOfWeek()){
                return false;
            }
        }
        return true;
    }

    public boolean canTakeVacation(User user, LocalDate day){
        //tady se bude kontrolovat jestli clovek ma jeste dost dnu na dovolenou tenhle rok
        if (user.getVacationDays() != null) {
            if (user.getVacationDaysPerYear() + 10 <= user.getVacationDays().size())
                return false;
        } else {
            if (user.getVacationDaysPerYear() == 0)
                return false;
        }
        //anebo jestli clovek nema ten den nepracovni den
        if (user.getNonWorkingDay() != null){
            for (DayOfWeek dayOfWeek : user.getNonWorkingDay()){
                if (dayOfWeek == day.getDayOfWeek()){
                    return false;
                }
            }
        }
        return true;
    }

    @Transactional
    public Team getCurrentUserTeam() {
        final User currentUser = SecurityUtils.getCurrentUser();
        assert currentUser != null;
        return currentUser.getTeam();
    }

    @Transactional
    public Group getCurrentUserGroup() {
        final User currentUser = SecurityUtils.getCurrentUser();
        assert currentUser != null;
        return currentUser.getGroup();
    }

    @Transactional
    public List<VacationDay> getCurrentUserVacationDays() {
        final User currentUser = SecurityUtils.getCurrentUser();
        assert currentUser != null;
        return currentUser.getVacationDays();
    }

    @Transactional(readOnly = true)
    public boolean exists(String username) {
        return dao.findByUsername(username) != null;
    }

    @Transactional
    public boolean loggedUser(User user) {
        return dao.checkUserPassword(user.getUsername(), user.getPassword());
    }
}
