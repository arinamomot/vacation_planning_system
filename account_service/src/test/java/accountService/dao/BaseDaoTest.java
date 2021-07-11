package accountService.dao;


import accountService.AccountServiceApplication;
import accountService.environment.Generator;
import accountService.exception.PersistenceException;
import accountService.model.Team;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackageClasses = AccountServiceApplication.class)
public class BaseDaoTest {

    private static final Random RAND = new Random();

    @Autowired
    private TestEntityManager em;

    @Autowired
    private TeamDao sut;

    @Test
    public void persistSavesSpecifiedInstance() {
        final Team team = Generator.generateTeam();
        sut.persist(team);
        assertNotNull(team.getId());

        final Team result = em.find(Team.class, team.getId());
        assertNotNull(result);
        assertEquals(team.getId(), result.getId());
        assertEquals(team.getName(), result.getName());
    }


    @Test
    public void findRetrievesInstanceByIdentifier() {
        final Team team = Generator.generateTeam();
        em.persistAndFlush(team);
        assertNotNull(team.getId());

        final Team result = sut.find(team.getId());
        assertNotNull(result);
        assertEquals(team.getId(), result.getId());
        assertEquals(team.getName(), result.getName());
    }

    @Test
    public void findAllRetrievesAllInstancesOfType() {
        final Team teamOne = Generator.generateTeam();
        em.persistAndFlush(teamOne);
        final Team teamTwo = Generator.generateTeam();
        em.persistAndFlush(teamTwo);

        final List<Team> result = sut.findAll();
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getId().equals(teamOne.getId())));
        assertTrue(result.stream().anyMatch(c -> c.getId().equals(teamTwo.getId())));
    }

    @Test
    public void updateUpdatesExistingInstance() {
        final Team team = Generator.generateTeam();
        em.persistAndFlush(team);

        final Team update = new Team();
        update.setId(team.getId());
        final String newName = "New Team name";
        update.setName(newName);
        sut.update(update);

        final Team result = sut.find(team.getId());
        assertNotNull(result);
        assertEquals(team.getName(), result.getName());
    }

    @Test
    public void removeRemovesSpecifiedInstance() {
        final Team team = Generator.generateTeam();
        em.persist(team);
        assertNotNull(em.find(Team.class, team.getId()));

        sut.remove(team);
        assertNull(em.find(Team.class, team.getId()));
    }

    @Test
    public void removeDoesNothingWhenInstanceDoesNotExist() {
        final Team team = Generator.generateTeam();
        team.setId(123);
        assertNull(em.find(Team.class, team.getId()));

        sut.remove(team);
        assertNull(em.find(Team.class, team.getId()));
    }

    @Test(expected = PersistenceException.class)
    public void exceptionOnPersistInWrappedInPersistenceException() {
        final Team team = Generator.generateTeam();
        em.persistAndFlush(team);
        em.remove(team);
        sut.update(team);
    }

    @Test
    public void existsReturnsTrueForExistingIdentifier() {
        final Team team = Generator.generateTeam();
        em.persistAndFlush(team);
        assertTrue(sut.exists(team.getId()));
        assertFalse(sut.exists(-1));
    }
}
