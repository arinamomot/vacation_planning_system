package accountService.facade;

import accountService.exception.NotFoundException;
import accountService.model.User;
import accountService.model.VacationDay;
import accountService.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFacade.class);

    private UserService userService;

    @Autowired
    public UserFacade(UserService userService) {
        this.userService = userService;
    }

    public User register(User user) {
        User createdUser = userService.createAccount(user);
        LOGGER.debug("User {} successfully registered", user);
        return createdUser;
    }

    public User getUser(int id) {
        final User user = userService.find(id);
        if (user == null)
            throw NotFoundException.create("User", id);
        return user;
    }

    public void updateAccount(int id, User updatedUser) {
        final User user = getUser(id);
        if (!(user.getId().equals(updatedUser.getId())))
            throw NotFoundException.create("No matching data in DB", id);
        userService.updateAccount(updatedUser);
        LOGGER.debug("Account of user {} successfully updated", updatedUser);
    }

    public void removeAccount(int id) {
        final User user = userService.find(id);
        if (user == null)
            throw NotFoundException.create("User", id);
        userService.deleteAccount(user);
        LOGGER.debug("User {} successfully removed", user);
    }

    public List<VacationDay> getVacationDaysByUser(int id) {
        final User user = userService.find(id);
        if (user == null)
            throw NotFoundException.create("User", id);
        return userService.displayVacationDays(user);
    }

    public List<User> getUsers() {
        return userService.findAll();
    }

}
