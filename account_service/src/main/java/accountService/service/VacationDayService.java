package accountService.service;

import accountService.dao.TeamDao;
import accountService.dao.UserDao;
import accountService.dao.VacationDayDao;
import accountService.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class VacationDayService {

    private VacationDayDao vacationDayDao;
    private UserDao userDao;
    private TeamDao teamDao;
    private UserService userService;

    @Autowired
    public VacationDayService(VacationDayDao vacationDayDao, UserDao userDao, TeamDao teamDao, UserService userService) {
        this.vacationDayDao = vacationDayDao;
        this.userDao = userDao;
        this.teamDao = teamDao;
        this.userService = userService;
    }

    @Transactional
    public boolean canUserTakeVacationOnThisDay(User user, LocalDate date) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(date);
        if (date.isBefore(LocalDate.now()))
            return false;
//        if (userDao.find(user.getId()) != null) {
////            if (userService.canTakeVacation(user, date)) {                                                              //day is working day and user has enough free days
////                int personLimit = user.getGroup().getEmployeesPerDay().get(date.getDayOfWeek());
////                int colleaguesOnVacation = 0;
////                for (VacationDay day : user.getTeam().getVacationDays()) {
////                    if (day.getDay().equals(date) && day.getVacationTaker().getGroup().equals(user.getGroup()))         //find all vacations on this day of people from user's group
////                        colleaguesOnVacation++;
////                }
////                return user.getGroup().getUsers().size() - colleaguesOnVacation > personLimit;
////            }
//        }
        return true;
    }

    @Transactional
    public void createVacation(User user, VacationDay vacationDay, LocalDate day) {
        Objects.requireNonNull(vacationDay);
        if (day.isBefore(LocalDate.now()))
            return;
        if (user.getVacationDays().stream().noneMatch(v -> v.getDay().equals(day))){
            if (!vacationDay.getReason().equals(VacationReason.HOSPITAL)) {
                if (userService.canTakeVacation(user, day)) {
                    if (canUserTakeVacationOnThisDay(user, day)) {
                        vacationDay.setVacationTaker(user);
                        vacationDay.setDay(day);
                        user.addVacationDay(vacationDay);
//                        user.getTeam().addVacationDay(vacationDay);
                        userDao.update(user);
//                        teamDao.update(user.getTeam());
                        vacationDayDao.persist(vacationDay);

                        Team team = teamDao.find(user.getTeam().getId());
                        team.addVacationDay(vacationDay);
                        teamDao.update(team);
                    }
                }
            } else {
                vacationDayDao.persist(vacationDay);
                if (userService.canTakeSickDay(user, day)) {
                    if (canUserTakeVacationOnThisDay(user, day)) {
                        vacationDay.setVacationTaker(user);
                        user.addVacationDay(vacationDay);
//                        user.getTeam().addVacationDay(vacationDay);
                        userDao.update(user);
//                        teamDao.update(user.getTeam());
                        vacationDayDao.persist(vacationDay);

                        Team team = teamDao.find(user.getTeam().getId());
                        team.addVacationDay(vacationDay);
                        teamDao.update(team);
                    }
                }
            }
        }
    }


    @Transactional
    public void deleteVacation(User user, VacationDay vacationDay){
        if (vacationDay.getDay().isBefore(LocalDate.now()))
            return;
        Objects.requireNonNull(vacationDay);
        vacationDayDao.remove(vacationDay);
        user.removeVacationDay(vacationDay);
        user.getTeam().removeVacationDay(vacationDay);
    }


    private void updateVacation(User user, VacationDay vacationDay, LocalDate day){
        if (day.isBefore(LocalDate.now()) || vacationDay.getDay().isBefore(LocalDate.now()))
            return;
        Objects.requireNonNull(vacationDay);
        deleteVacation(user, vacationDayDao.find(vacationDay.getId()));
        createVacation(user, vacationDay, day);
        vacationDayDao.update(vacationDay);
    }

    @Transactional
    public void editVacationReason(VacationDay vacationDay, VacationReason reason){
        if (vacationDay.getDay().isBefore(LocalDate.now()))
            return;
        Objects.requireNonNull(vacationDay);
        vacationDay.setReason(reason);
        updateVacation(vacationDay.getVacationTaker(), vacationDay, vacationDay.getDay());
    }

    @Transactional
    public void editVacationPriority(VacationDay vacationDay, VacationPriority priority) {
        if (vacationDay.getDay().isBefore(LocalDate.now()))
            return;
        Objects.requireNonNull(vacationDay);
        if (vacationDay.getPriority()!=priority) {
            vacationDay.setPriority(priority);
            //sendPriorityNotification(vacationDay.getVacationTaker(), vacationDay.getDay());
            updateVacation(vacationDay.getVacationTaker(), vacationDay, vacationDay.getDay());
        }
    }

    /*
    @Transactional
    public void sendPriorityNotification(User user, LocalDate day) {
        ChangeOfPriorityNotification notification = new ChangeOfPriorityNotification();
        notification.setText("User: " + user.getFirstName() + " " + user.getLastName() + " changed priority of his vacation at " + day.toString() + " !");

        for (User member : user.getTeam().getUsers()){
            if (member.isAdmin()){
                member.addPriorityNotifications(notification);
            }
        }
    }
     */

    @Transactional
    public VacationDay find (Integer id){
        return vacationDayDao.find(id);
    }

    @Transactional
    public List<VacationDay> findAll (){
        return vacationDayDao.findAll();
    }

    @Transactional
    public List<VacationDay> findAllByTeam(Integer teamId){
        Team team = teamDao.find(teamId);

        return team.getVacationDays();
    }

}
