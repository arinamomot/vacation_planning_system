package accountService.service;

import accountService.environment.Generator;
import accountService.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class VacationDayServiceTest {

    @Autowired
    private VacationDayService sut;

    @PersistenceContext
    private EntityManager em;

    @Test
    public void createVacation_isNotCreatedWhenPersonDontHaveEnoughVacationDays(){
        User user = Generator.generateUser(0, null);
        em.persist(user);

        LocalDate day = LocalDate.now();

        VacationDay vacationDay = Generator.generateVacationDay(user);
        vacationDay.setId(1);

        sut.createVacation(user, vacationDay, day);

        final User resultUser = em.find(User.class, user.getId());
        final Team resultTeam = resultUser.getTeam();
        assertFalse(resultUser.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay.getId())));
        assertFalse(resultTeam.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay.getId())));
        assertNull(em.find(VacationDay.class, vacationDay.getId()));
    }

    @Test
    public void createVacation_isNotCreatedWhenPersonHaveThisDayNonWorkingDay(){
        Group group = Generator.generateGroup(new int[]{0, 0, 0, 0, 0, 0, 0});
        em.persist(group);

        User user = Generator.generateUser(1, group);
        user.setNonWorkingDay(new ArrayList<DayOfWeek>(Arrays.asList(LocalDate.now().getDayOfWeek())));
        em.persist(user);

        group.addUser(user);
        em.persist(group);

        LocalDate day = LocalDate.now();

        VacationDay vacationDay = Generator.generateVacationDay(user);
        vacationDay.setId(200);


        sut.createVacation(user, vacationDay, day);

        final User resultUser = em.find(User.class, user.getId());
        final Team resultTeam = resultUser.getTeam();
        assertFalse(resultUser.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay.getId())));
        assertFalse(resultTeam.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay.getId())));
        assertNull(em.find(VacationDay.class, vacationDay.getId()));
    }

    @Test
    public void createVacation_isNotCreatedWhenThisDayNotEnoughWorkersInGroup(){
        Group group = Generator.generateGroup(new int[]{1, 1, 1, 1, 1, 1, 1});
        em.persist(group);

        User user = Generator.generateUser(1, group);
        em.persist(user);

        group.addUser(user);
        em.persist(group);

        LocalDate day = LocalDate.now();

        VacationDay vacationDay = Generator.generateVacationDay(user);
        vacationDay.setId(200);

        sut.createVacation(user, vacationDay, day);

        final User resultUser = em.find(User.class, user.getId());
        final Team resultTeam = resultUser.getTeam();
        assertFalse(resultUser.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay.getId())));
        assertFalse(resultTeam.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay.getId())));
        assertNull(em.find(VacationDay.class, vacationDay.getId()));
    }


    @Test
    public void createVacation_vacationIsNotCreatedWhenPersonHasAlreadyVacationThatDay(){
        Group group = Generator.generateGroup(new int[]{0, 0, 0, 0, 0, 0, 0});
        em.persist(group);

        User user = Generator.generateUser(1, group);
        em.persist(user);

        group.addUser(user);
        em.persist(group);

        LocalDate day = LocalDate.now();

        VacationDay vacationDay1 = Generator.generateVacationDay(user);
        vacationDay1.setId(200);

        sut.createVacation(user, vacationDay1, day);

        final User resultUser1 = em.find(User.class, user.getId());
        final Team resultTeam1 = resultUser1.getTeam();
        assertTrue(resultUser1.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay1.getId())));
        assertTrue(resultTeam1.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay1.getId())));
        assertNotNull(em.find(VacationDay.class, vacationDay1.getId()));

        VacationDay vacationDay2 = Generator.generateVacationDay(user);
        vacationDay2.setId(201);

        sut.createVacation(user, vacationDay2, day);

        final User resultUser2 = em.find(User.class, user.getId());
        final Team resultTeam2 = resultUser2.getTeam();
        assertFalse(resultUser2.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay2.getId())));
        assertFalse(resultTeam2.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay2.getId())));
        assertNull(em.find(VacationDay.class, vacationDay2.getId()));
    }

    @Test
    public void createVacation_created(){
        Group group = Generator.generateGroup(new int[]{0, 0, 0, 0, 0, 0, 0});
        em.persist(group);

        User user = Generator.generateUser(1, group);
        em.persist(user);

        group.addUser(user);
        em.persist(group);

        LocalDate day = LocalDate.now();

        VacationDay vacationDay = Generator.generateVacationDay(user);
        vacationDay.setId(200);

        sut.createVacation(user, vacationDay, day);

        final User resultUser = em.find(User.class, user.getId());
        final Team resultTeam = resultUser.getTeam();
        assertTrue(resultUser.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay.getId())));
        assertTrue(resultTeam.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay.getId())));
        assertNotNull(em.find(VacationDay.class, vacationDay.getId()));
    }

    @Test
    public void creatFewVacationDays_isCreated(){
        Group group = Generator.generateGroup(new int[]{0, 0, 0, 0, 0, 0, 0});
        em.persist(group);

        User user = Generator.generateUser(10, group);
        em.persist(user);

        group.addUser(user);
        em.persist(group);

        LocalDate day1 = LocalDate.now();
        LocalDate day2 = LocalDate.now().plus(1, ChronoUnit.DAYS);


        VacationDay vacationDay1 = Generator.generateVacationDay(user);
        VacationDay vacationDay2 = Generator.generateVacationDay(user);

        vacationDay1.setId(200);
        vacationDay2.setId(201);

        sut.createVacation(user, vacationDay1, day1);
        sut.createVacation(user, vacationDay2, day2);

        final User resultUser = em.find(User.class, user.getId());
        final Team resultTeam1 = resultUser.getTeam();
        final Team resultTeam2 = resultUser.getTeam();

        assertTrue(resultUser.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay1.getId())));
        assertTrue(resultUser.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay2.getId())));
        assertTrue(resultTeam1.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay1.getId())));
        assertTrue(resultTeam2.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay2.getId())));
        assertNotNull(em.find(VacationDay.class, vacationDay1.getId()));
        assertNotNull(em.find(VacationDay.class, vacationDay2.getId()));
    }

    @Test
    public void creatVacationsForFewPeople_isCreated(){
        Group group = Generator.generateGroup(new int[]{0, 0, 0, 0, 0, 0, 0});
        em.persist(group);

        User user1 = Generator.generateUser(10, group);
        em.persist(user1);

        User user2 = Generator.generateUser(10, group);
        em.persist(user2);

        group.addUser(user1);
        group.addUser(user2);
        user2.setTeam(user1.getTeam());
        em.persist(group);

        LocalDate day = LocalDate.now();


        VacationDay vacationDay1 = Generator.generateVacationDay(user1);
        VacationDay vacationDay2 = Generator.generateVacationDay(user2);

        vacationDay1.setId(200);
        vacationDay2.setId(201);

        sut.createVacation(user1, vacationDay1, day);
        sut.createVacation(user2, vacationDay2, day);

        final User resultUser1 = em.find(User.class, user1.getId());
        final User resultUser2 = em.find(User.class, user2.getId());


        assertTrue(resultUser1.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay1.getId())));
        assertTrue(resultUser2.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay2.getId())));
        assertTrue(resultUser1.getTeam().getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay1.getId())));
        assertTrue(resultUser2.getTeam().getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay2.getId())));
        assertNotNull(em.find(VacationDay.class, vacationDay1.getId()));
        assertNotNull(em.find(VacationDay.class, vacationDay2.getId()));

    }

    @Test
    public void deleteVacation_isDeleted(){
        Group group = Generator.generateGroup(new int[]{0, 0, 0, 0, 0, 0, 0});
        em.persist(group);

        User user = Generator.generateUser(1, group);
        em.persist(user);

        group.addUser(user);
        em.persist(group);

        LocalDate day = LocalDate.now();


        VacationDay vacationDay = Generator.generateVacationDay(user);
        vacationDay.setId(200);

        sut.createVacation(user, vacationDay, day);
        sut.deleteVacation(user, vacationDay);

        final User resultUser = em.find(User.class, user.getId());
        final Team resultTeam = resultUser.getTeam();
        assertFalse(resultUser.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay.getId())));
        assertFalse(resultTeam.getVacationDays().stream().anyMatch(v -> v.getId().equals(vacationDay.getId())));
        assertNull(em.find(VacationDay.class, vacationDay.getId()));
    }

    @Test
    public void editVacationReason_edited(){
        Team team = Generator.generateTeam();
        em.persist(team);

        Group group = Generator.generateGroup(new int[]{0, 0, 0, 0, 0, 0, 0});
        em.persist(group);

        User user = Generator.generateUser(1, group);
        user.setTeam(team);
        em.persist(user);

        group.addUser(user);
        em.persist(group);

        LocalDate day = LocalDate.now();


        VacationDay vacationDay = Generator.generateVacationDay(user);
        vacationDay.setId(200);
        sut.createVacation(user, vacationDay, day);

        VacationReason newReason = VacationReason.HOLIDAY;
        sut.editVacationReason(vacationDay, newReason);

        final User resultUser = em.find(User.class, user.getId());
        final Team resultTeam = resultUser.getTeam();
        assertTrue(resultUser.getVacationDays().stream().filter(v -> v.getId().equals(vacationDay.getId())).findFirst().get().getReason().equals(newReason));
        assertTrue(resultTeam.getVacationDays().stream().filter(v -> v.getId().equals(vacationDay.getId())).findFirst().get().getReason().equals(newReason));
        assertTrue(em.find(VacationDay.class, vacationDay.getId()).getReason().equals(newReason));
    }

    @Test
    public void editVacationPriority_edited(){
        Group group = Generator.generateGroup(new int[]{0, 0, 0, 0, 0, 0, 0});
        em.persist(group);

        User admin = Generator.generateUser(0, null);
        admin.setAdmin(true);
        em.persist(admin);

        User user = Generator.generateUser(1, group);
        em.persist(user);

        group.addUser(user);
        em.persist(group);


        LocalDate day = LocalDate.now();


        VacationDay vacationDay = Generator.generateVacationDay(user);
        vacationDay.setId(200);

        sut.createVacation(user, vacationDay, day);
        VacationPriority newPriority = VacationPriority.HIGH;
        sut.editVacationPriority(vacationDay, newPriority);

        final User resultUser = em.find(User.class, user.getId());
        final Team resultTeam = resultUser.getTeam();
        assertEquals(resultUser.getVacationDays().stream().filter(v -> v.getId().equals(vacationDay.getId())).findFirst().get().getPriority(), newPriority);
        assertEquals(resultTeam.getVacationDays().stream().filter(v -> v.getId().equals(vacationDay.getId())).findFirst().get().getPriority(), newPriority);
        assertEquals(em.find(VacationDay.class, vacationDay.getId()).getPriority(), newPriority);
    }

}
