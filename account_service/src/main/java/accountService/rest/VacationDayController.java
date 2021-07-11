package accountService.rest;

import accountService.facade.VacationDayFacade;
import accountService.model.VacationDay;
import accountService.rest.dto.VacationDayDTO;
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
@RequestMapping("rest/vacationDays")
public class VacationDayController {

    private final VacationDayFacade facade;

    @Autowired
    public VacationDayController(VacationDayFacade facade) {
        this.facade = facade;
    }


    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createVacationDay(@RequestBody VacationDayDTO vacationDay) {
        VacationDay result = facade.createVacationDay(vacationDay);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", result.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PreAuthorize("(hasRole('ROLE_ADMIN') and vacationDayService.find(id) != null and vacationDayService.find(id).vacationTaker.team.admin.username == principal.username ) or (hasRole('ROLE_USER') and vacationDayService.find(id) != null and vacationDayService.find(id).vacationTaker.username == principal.username)")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public VacationDay getVacationDay(@PathVariable Integer id) {
        return facade.getVacationDay(id);
    }


    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/team/{teamId}")
    public List<VacationDayDTO> getVacationDaysByTeam(@PathVariable Integer teamId) {
        return facade.getVacationDaysByTeam(teamId);
    }

    @PostFilter("(hasRole('ROLE_ADMIN') and filterObject.vacationTaker.team.admin.username == principal.username) or (hasRole('ROLE_USER') and filterObject.vacationTaker.username == principal.username)")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VacationDay> getVacationDays() {
        return facade.getVacationDays();
    }

    @PreAuthorize("(hasRole('ROLE_ADMIN') and updatedDay.vacationTaker.team.admin.username == principal.username) or (hasRole('ROLE_USER') and updatedDay.vacationTaker.username == principal.username)")
    @PutMapping(value = "/{id}/priority", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateVacationDayPriority(@PathVariable Integer id, @RequestBody VacationDay updatedDay) {
        facade.updateVacationDayPriority(id, updatedDay);
    }

    @PreAuthorize("hasRole('ROLE_USER') and updatedDay.vacationTaker.username == principal.username")
    @PutMapping(value = "/{id}/reason", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateVacationDayReason(@PathVariable Integer id, @RequestBody VacationDay updatedDay) {
        facade.updateVacationDayReason(id, updatedDay);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VacationDay> getCurrentUserVacationDays() {
        return facade.getCurrentUserVacationDays();
    }


    @PreAuthorize("(hasRole('ROLE_ADMIN') and vacationDayService.find(id) != null and vacationDayService.find(id).vacationTaker.team.admin.username == principal.username ) or (hasRole('ROLE_USER') and vacationDayService.find(id) != null and vacationDayService.find(id).vacationTaker.username == principal.username)")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeVacationDay(@PathVariable Integer id) {
        facade.removeVacationDay(id);
    }
}
