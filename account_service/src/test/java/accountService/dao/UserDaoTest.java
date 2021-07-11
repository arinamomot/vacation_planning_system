package accountService.dao;

import accountService.AccountServiceApplication;
import accountService.environment.Generator;
import accountService.model.User;

import accountService.service.SystemInitializer;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Random;
import static org.junit.Assert.*;

//base user team

@Ignore
@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackageClasses = AccountServiceApplication.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SystemInitializer.class)})
public class UserDaoTest {

    private static final Random RAND = new Random();

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserDao sut;

    @Test
    public void findAdmin_findsAllAdmins(){
        User user = Generator.generateUser();
        User admin = Generator.generateUser();
        admin.setAdmin(true);
        em.persist(user);
        em.persist(admin);

        User result = sut.findAllAdmin().get(0);
        assertNotNull(result);
        assertEquals(admin.getId(), result.getId());
    }


    @Test
    public void findByUsernameReturnsPersonWithMatchingUsername() {
        final User user = Generator.generateUser();
        em.persist(user);

        final User result = sut.findByUsername(user.getUsername());
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
    }

    @Test
    public void findByUsernameReturnsNullForUnknownUsername() {
        assertNull(sut.findByUsername("unknownUsername"));
    }
}
