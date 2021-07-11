package holidayPlannerNotificationService.rest;

import holidayPlannerNotificationService.facade.UserFacade;
import holidayPlannerNotificationService.model.User;
import holidayPlannerNotificationService.rest.dto.UserRegistrationDTO;
import holidayPlannerNotificationService.rest.utils.RestUtils;
import holidayPlannerNotificationService.security.model.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserFacade facade;

    @Autowired
    public UserController(UserFacade facade) {
        this.facade = facade;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> register(@RequestBody UserRegistrationDTO user) {
        facade.register(user);
        final HttpHeaders header = RestUtils.createLocationHeaderFromCurrentUri("/current");
        return new ResponseEntity<>(header, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_MAIN_ADMIN') or (hasRole('ROLE_ADMIN') and userService.find(userId)!=null and userService.find(userId).team.admin.username == principal.username )")
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public User getUser(@PathVariable Integer userId) {
        return facade.getUser(userId);
    }

    // TODO: fix @Pre and @Post filter in these methods
    @PostFilter("hasRole('ROLE_MAIN_ADMIN') or (hasRole('ROLE_ADMIN') and filterObject.team.admin.username == principal.username)")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers() {
        return facade.getUsers();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') and updatedUser.team.admin.username == principal.username")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAccount(@PathVariable Integer id, @RequestBody User updatedUser) {
        facade.updateAccount(id, updatedUser);
    }


    @PreAuthorize("hasRole('ROLE_MAIN_ADMIN') or (hasRole('ROLE_USER') and userService.find(id)!=null and userService.find(id).username == principal.username )")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAccount(@PathVariable Integer id) {
        facade.removeAccount(id);
    }


    @PreAuthorize("hasAnyRole('ROLE_MAIN_ADMIN','ROLE_ADMIN','ROLE_USER')")
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getCurrentUser(Principal principal) {
        final AuthenticationToken auth = (AuthenticationToken) principal;
        return auth.getPrincipal().getUser();
    }
}
