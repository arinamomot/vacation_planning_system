package accountService.rest;

import accountService.facade.UserFacade;
import accountService.model.User;
import accountService.model.VacationDay;
import accountService.rest.utils.RestUtils;
import accountService.security.model.AuthenticationToken;
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
@RequestMapping("rest/users")
public class UserController {

    private final UserFacade facade;

    @Autowired
    public UserController(UserFacade facade) {
        this.facade = facade;
    }

    @PreAuthorize("hasRole('ROLE_GUEST') || anonymous")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public User register(@RequestBody User user) {
        User result = facade.register(user);
        return result;
    }

    @PreAuthorize("hasRole('ROLE_MAIN_ADMIN') or (hasRole('ROLE_ADMIN') and userService.find(id)!=null and  userService.find(id).team.admin.username == principal.username )")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUser(@PathVariable Integer id) {
        return facade.getUser(id);
    }

    @PostFilter("hasAnyRole('ROLE_MAIN_ADMIN','ROLE_ADMIN','ROLE_USER')")
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

    @PostFilter("(hasRole('ROLE_ADMIN') and (filterObject.vacationTaker.team.admin.username == principal.username)) or (hasRole('ROLE_USER') and filterObject.vacationTaker.username == principal.username)")
    @GetMapping(value = "/{id}/vacationDays", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VacationDay> getVacationDaysByUser(@PathVariable Integer id) {
        return facade.getVacationDaysByUser(id);
    }
}
