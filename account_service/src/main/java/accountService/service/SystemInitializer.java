package accountService.service;

import accountService.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;

@Component
public class SystemInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(SystemInitializer.class);

    private final UserService userService;

    private User loggedUser;

    private final PlatformTransactionManager txManager;

    @Autowired
    public SystemInitializer(UserService userService, PlatformTransactionManager txManager) {
        this.userService = userService;
        this.txManager = txManager;
    }

    @PostConstruct
    private void initSystem() {
        TransactionTemplate txTemplate = new TransactionTemplate(txManager);
        txTemplate.execute((status) -> {
            generateFirstUser();
            return null;
        });
    }

//    @PostConstruct
//    private void initSystem(User user) {
//        TransactionTemplate txTemplate = new TransactionTemplate(txManager);
//        txTemplate.execute((status) -> {
//            if (userService.exists(user.getUsername())){
//                login(user);
//            } else {
//                userService.createAccount(user);
//            }
//            return null;
//        });
//    }

    private void generateFirstUser(){
        final User first = new User();
        first.setAdmin(false);
        first.setFirstName("firstName");
        first.setLastName("lastName");
        first.setUsername("username");
        first.setPassword("password");
        first.setPosition("guest");
        userService.createAccount(first);
    }

    /**
     * Generates an admin account if it does not already exist.
     */
    private void login(User user) {
        if (userService.loggedUser(user)) {
            loggedUser = user;
            return;
        }
        userService.createAccount(user);
    }
}
