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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserService sut;

    @Autowired
    private TeamService teamSut;



    @Test
    public void persistCreatesAccount() {
        final User user = Generator.generateUser();
        em.persist(user);
        sut.createAccount(user);

        final User result = em.find(User.class, user.getId());
        assertFalse(result.isAdmin());
        assertTrue(result.getUsername().equals(user.getUsername()));
    }
/*
    @Test
    public void persistDeleteAccount() {
        final User user = Generator.generateUser();
        em.persist(user);
        sut.createAccount(user);
        sut.deleteAccount(user);

        final User result = em.find(User.class, user.getId());
        assertFalse(result.getUsername().equals(user.getUsername()));
    }*/

    @Test
    public void persistUpdateAccount() {
        final User user = Generator.generateUser();
        String name = user.getUsername();
        sut.createAccount(user);
        user.setUsername("new name");
        em.persist(user);
        sut.updateAccount(user);

        final User result = em.find(User.class, user.getId());
        assertFalse(result.getUsername().equals(name));
        assertTrue(result.getUsername().equals("new name"));
    }

}
