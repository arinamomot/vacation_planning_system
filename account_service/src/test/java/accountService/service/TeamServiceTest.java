package accountService.service;

import accountService.environment.Generator;
import accountService.model.Team;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class TeamServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private TeamService sut;

    @Test
    public void persistCreatesTeam() {
        final Team team = Generator.generateTeam();
        em.persist(team);
        sut.createTeam(team);

        final Team result = em.find(Team.class, team.getId());
        assertTrue(result.getName().equals(team.getName()));
    }

    @Test
    public void updateUpdatesTeam() {
        final Team team = Generator.generateTeam();
        String name = team.getName();
        sut.createTeam(team);
        team.setName("new name");
        em.persist(team);
        sut.updateTeam(team);

        final Team result = em.find(Team.class, team.getId());
        assertFalse(result.getName().equals(name));
        assertTrue(result.getName().equals("new name"));
    }


    @Test
    public void persistChecksIfTeamExists() {
        final Team team = Generator.generateTeam();
        String name = team.getName();
        em.persist(team);
        sut.createTeam(team);

        boolean exists = sut.exists(name);

        final Team result = em.find(Team.class, team.getId());
        assertEquals(!exists, result.getName().isEmpty());
    }
}
