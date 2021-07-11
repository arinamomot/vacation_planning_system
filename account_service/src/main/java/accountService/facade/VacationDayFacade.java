package accountService.facade;

import accountService.exception.NotFoundException;
import accountService.model.User;
import accountService.model.VacationDay;
import accountService.rest.dto.VacationDayDTO;
import accountService.security.SecurityUtils;
import accountService.service.UserService;
import accountService.service.VacationDayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VacationDayFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(VacationDayFacade.class);

    private VacationDayService vacationDayService;
    private UserService userService;

    public VacationDayFacade(VacationDayService vacationDayService, UserService userService) {
        this.vacationDayService = vacationDayService;
        this.userService = userService;
    }

    public VacationDay createVacationDay(VacationDayDTO vacationDayDTO) {
        final User user = userService.find(SecurityUtils.getCurrentUser().getId());

        VacationDay vacationDay = new VacationDay(vacationDayDTO);
        vacationDay.setVacationTaker(user);
        vacationDayService.createVacation(user, vacationDay, vacationDay.getDay());
        LOGGER.debug("VacationDay {} successfully created", vacationDay);

        return vacationDay;
    }

    public VacationDay getVacationDay(int id) {
        final VacationDay day = vacationDayService.find(id);
        if (day == null)
            throw NotFoundException.create("VacationDay", id);
        return day;
    }

    public List<VacationDay> getVacationDays() {
        return vacationDayService.findAll();
    }

    public void updateVacationDayPriority(int id, VacationDay updatedDay) {
        //TODO: vit jestli tady nedava vetsi smysl predavat jen samotnou priority a ne cely vacation day? - mozna ano, mozna ne. Neumim ted posoudit
        VacationDay day = getVacationDay(id);
        if (!(day.getId().equals(updatedDay.getId())))
            throw NotFoundException.create("No matching data in DB", id);
        vacationDayService.editVacationPriority(updatedDay, updatedDay.getPriority());
        LOGGER.debug("Vacation day successfully updated");
    }

    public void updateVacationDayReason(int id, VacationDay updatedDay) {
        VacationDay day = getVacationDay(id);
        if (!(day.getId().equals(updatedDay.getId())))
            throw NotFoundException.create("No matching data in DB", id);
        vacationDayService.editVacationReason(updatedDay, updatedDay.getReason());
        LOGGER.debug("Vacation day reason successfully updated");
    }

    public List<VacationDay> getCurrentUserVacationDays() {
        return userService.getCurrentUserVacationDays();
    }

    public void removeVacationDay(int id) {
        final User user = SecurityUtils.getCurrentUser();
        final VacationDay day = getVacationDay(id);
        if (day == null)
            throw NotFoundException.create("No such VacationDay for user", user.getId());
        vacationDayService.deleteVacation(user, day);
        LOGGER.debug("Vacation day {} successfully removed", day);
    }

    public List<VacationDayDTO> getVacationDaysByTeam(Integer teamId) {
        return vacationDayService.findAllByTeam(teamId).stream().map(VacationDayDTO::new).collect(Collectors.toList());
    }
}
