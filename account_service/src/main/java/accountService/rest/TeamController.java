package accountService.rest;

import accountService.facade.TeamFacade;
import accountService.model.Group;
import accountService.model.Team;
import accountService.rest.utils.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rest/teams")
public class TeamController {

    private final TeamFacade facade;

    @Autowired
    public TeamController(TeamFacade facade) {
        this.facade = facade;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createTeam(@RequestBody Team team) {
        facade.createTeam(team);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", team.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ROLE_MAIN_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Team getTeam(@PathVariable Integer id) {
        return facade.getTeam(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public Team getCurrentUserTeam() {
        return facade.getCurrentUserTeam();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Team> getTeams() {
        return facade.getTeams();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTeam(@PathVariable Integer id, @RequestBody Team updatedTeam) {
        facade.updateTeam(id, updatedTeam);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTeam(@PathVariable Integer id) {
        facade.removeTeam(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/current/groups", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Group> getGroups() {
        return facade.getGroupsInTeam();
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/current/groups", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addGroupToTeam(@RequestBody Group group) {
        facade.addGroupToTeam(group);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/current/members/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addMemberToTeam(@PathVariable Integer userId) {
        facade.addMemberToTeam(userId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/current/groups/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateGroupInTeam(@PathVariable Integer id, @RequestBody Group updatedGroup) {
        facade.updateGroupInTeam(id, updatedGroup);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/current/groups/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteGroupInTeam(@PathVariable Integer id) {
        facade.deleteGroupInTeam(id);
    }
}
