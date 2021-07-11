package holidayPlannerNotificationService.service;

import holidayPlannerNotificationService.dao.UserDao;
import holidayPlannerNotificationService.exception.ValidationException;
import holidayPlannerNotificationService.model.*;
import holidayPlannerNotificationService.rest.dto.UserRegistrationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserDao dao;

    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserDao dao, PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createAccount(UserRegistrationDTO userDto) {
        Objects.requireNonNull(userDto);
        if (exists(userDto.getUsername())) {
            // TODO: throw appropriate error
            throw new ValidationException("Username already taken!");
        }
        User user = User.fromDTO(userDto);
        user.encodePassword(passwordEncoder);
        user.setRole(Role.USER);
        dao.persist(user);
    }

    @Transactional
    public void deleteAccount(User user) {
        Objects.requireNonNull(user);
        if (exists(user.getUsername())) {
            dao.remove(user);
        }
    }

    @Transactional
    public void updateAccount(User user) {
        Objects.requireNonNull(user);
        if (exists(user.getUsername())) {
            dao.update(user);
        }
    }
    
    @Transactional
    public User find(Integer id) {
        return dao.find(id);
    }
    
    @Transactional
    public User findByUserId(Integer id) {
        return dao.findByUserId(id);
    }
    
    @Transactional
    public List<User> findAll() {
        return dao.findAll();
    }

    @Transactional
    public void addAdmin(User user) {
        Objects.requireNonNull(user);
        if (!user.isAdmin()) {
            user.setAdmin(true);
        }
        dao.update(user);
    }


    @Transactional(readOnly = true)
    public boolean exists(String username) {
        return dao.findByUsername(username) != null;
    }

    @Transactional
    public boolean loggedUser(User user) {
        return dao.checkUserPassword(user.getUsername(), user.getPassword());
    }
}
