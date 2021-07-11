package accountService.service;

import accountService.dao.GroupDao;
import accountService.dao.TeamDao;
import accountService.dao.UserDao;
import accountService.environment.Generator;
import accountService.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
public class GroupServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private GroupService sut;

    @Autowired
    private UserService sutUser;

    @Autowired
    private TeamService sutTeam;

    @Mock
    private TeamDao tDao;

    @Mock
    private UserDao uDao;

    @Mock
    private GroupDao gDao;

    @Before
    public void setUp() {
    //    this.sut = new GroupService(gDao, uDao, tDao);
    }

    @Test
    public void persistCreatesGroup() {
        this.sut = new GroupService(gDao, uDao, tDao);
        final Group g = Generator.generateGroup();
        em.persist(g);
        sut.createGroup(g);

        final Group result = em.find(Group.class, g.getId());
        assertEquals(result.getName(), g.getName());
    }

    @Test
    public void persistDeletesGroup() {
        final Group g = Generator.generateGroup();
        //final Group g2 = Generator.generateGroup();

        em.persist(g);
        sut.createGroup(g);
       // sut.createGroup(g2);
        sut.deleteGroup(g);

        final Group result = em.find(Group.class, g.getId());
        assertFalse(result.getName().equals(g.getName()));
    }

    @Test
    public void persistUpdatesGroup() {
        this.sut = new GroupService(gDao, uDao, tDao);
        final Group g = Generator.generateGroup();
        String name = g.getName();
        em.persist(g);
        sut.createGroup(g);
        g.setName("new name");
        em.persist(g);
        sut.updateGroup(g);

        final Group result = em.find(Group.class, g.getId());
        assertFalse(result.getName().equals(name));
        assertTrue(result.getName().equals("new name"));
    }
/*
    @Test
    public void addNewGroupToTeam() {
        this.sut = new GroupService(gDao, uDao, tDao);
        final Group g = Generator.generateGroup();
        final Team t = Generator.generateTeam();
        em.persist(t);
        sutTeam.createTeam(t);
        em.persist(g);
        sut.createGroup(g);
        sut.addGroupToTeam(g,t);

        final Group result = em.find(Group.class, g.getId());
        assertTrue(result.getTeam().equals(t));
    }

    @Test
    public void removeExistingGroupFromTeam() {
        final Group g = Generator.generateGroup();
        final Team t = Generator.generateTeam();
        em.persist(g);
        sut.createGroup(g);
        //em.persist(t);
        //sutTeam.createTeam(t);
        sut.addGroupToTeam(g,t);
        //sut.removeGroupFromTeam(g,t);

        //final Team result = em.find(Team.class, t.getId());
        //assertFalse(result.getGroups().contains(g));
        final Group result = em.find(Group.class, g.getId());
        assertTrue(result.getTeam().equals(t));
    }

    @Test
    public void addNewUserToGroup() {
        final Team t = Generator.generateTeam();
        final Group g = Generator.generateGroup();
        g.setTeam(t);
        final User u = generateUser();
        u.setTeam(t);
        sut.createGroup(g);
        sutUser.createAccount(u);
        sutTeam.createTeam(t);

        sut.addUserToGroup(u,g);

        final Group result = em.find(Group.class, g.getId());
        assertTrue(result.getUsers().contains(u));
    }

    @Test
    public void removeExistingUserFromGroup() {
        final Team t = generateTeam();
        final Group g = generateGroup();
        g.setTeam(t);
        final User u = generateUser();
        u.setTeam(t);
        sut.createGroup(g);
        userSut.createAccount(u);
        teamSut.createTeam(t);
        sut.addUserToGroup(u,g);

        sut.removeUserFromGroup(u,g);

        final ArgumentCaptor<Group> captor = ArgumentCaptor.forClass(Group.class);
        verify(groupDaoMock).persist(captor.capture());
        assertFalse(captor.getValue().getUsers().contains(u));
    }

    public static Group generateGroup() {
        final Group g = new Group();
        g.setName("GroupName" + RAND.nextInt());
        return g;
    }*/
}
