package accountService.rest;

import accountService.facade.GroupFacade;
import accountService.model.Group;
import accountService.model.User;
import accountService.rest.dto.GroupDTO;
import accountService.rest.utils.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("rest/groups")
public class GroupController {

    private final GroupFacade facade;

    @Autowired
    public GroupController(GroupFacade facade) {
        this.facade = facade;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createGroup(@RequestBody Group group) {
        facade.createGroup(group);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", group.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }


    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GroupDTO getGroup(@PathVariable Integer id) {
        return facade.getGroup(id);
    }


    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public Group getCurrentUserGroup() {
        return facade.getCurrentUserGroup();
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostFilter("(filterObject.team.admin.username == principal.username)")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Group> getGroups() {
        return facade.getGroups();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeGroup(@PathVariable Integer id) {
        facade.removeGroup(id);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/members", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getGroupMembers() {
        return facade.getGroupMembers();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping(value = "/{groupId}/members/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addUserToGroup(@PathVariable Integer groupId, @PathVariable Integer userId) {
        facade.addUserToGroup(groupId, userId);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping(value = "/{groupId}/members/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserFromGroup(@PathVariable Integer groupId, @PathVariable Integer userId) {
        facade.removeUserFromGroup(groupId, userId);
    }

}
