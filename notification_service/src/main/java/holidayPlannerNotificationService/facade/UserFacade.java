package holidayPlannerNotificationService.facade;

import holidayPlannerNotificationService.exception.ResourceNotFoundException;
import holidayPlannerNotificationService.model.User;
import holidayPlannerNotificationService.rest.dto.UserRegistrationDTO;
import holidayPlannerNotificationService.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFacade.class);

    private UserService userService;

    public UserFacade(UserService userService) {
        this.userService = userService;
    }

    public void register(UserRegistrationDTO user) {
        userService.createAccount(user);
        LOGGER.debug("User {} successfully registered", user);
    }

    public User getUser(int userId) {
        final User user = userService.findByUserId(userId);
        if (user == null)
            throw new ResourceNotFoundException(User.class, userId);
        return user;
    }

    public List<User> getUsers() {
        return userService.findAll();
    }

    public void updateAccount(int id, User updatedUser) {
        final User user = getUser(id);
        if (!(user.getId().equals(updatedUser.getId())))
            throw new ResourceNotFoundException("No matching data in DB " + id);
        userService.updateAccount(updatedUser);
        LOGGER.debug("Account of user {} successfully updated", updatedUser);
    }

    public void removeAccount(int id) {
        final User user = userService.findByUserId(id);
        if (user == null)
            throw new ResourceNotFoundException(User.class, id);
        userService.deleteAccount(user);
        LOGGER.debug("User {} successfully removed", user);
    }
}
