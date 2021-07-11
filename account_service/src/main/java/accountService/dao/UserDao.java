package accountService.dao;

import accountService.exception.PersistenceException;
import accountService.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserDao extends BaseDao<User>{

    public UserDao() { super(User.class); }

    public List<User> findAllAdmin(){
        try{
            return em.createQuery("SELECT u FROM User u WHERE u.admin = true", User.class).getResultList();
        } catch (RuntimeException e){
            throw new PersistenceException(e);
        }
    }

    public List<User> findAllNotAdmin(){
        try{
            return em.createQuery("SELECT u FROM User u WHERE NOT u.admin", User.class).getResultList();
        } catch (RuntimeException e){
            throw new PersistenceException(e);
        }
    }

    //Criteria API
    public List<User> findUserByName(String firstName, String lastName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);

        Root<User> user = cq.from(User.class);
        Predicate userFirstName = cb.equal(user.get("firstName"),firstName);
        Predicate userLastName = cb.equal(user.get("lastName"), lastName);
        cq.where(userLastName, userFirstName);

        TypedQuery<User> query = em.createQuery(cq);
        return query.getResultList();
    }

    public User findByUsername(String username) {
        try {
            return em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean checkUserPassword(String username, String password) {
        try {
            User loginUser = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class).getSingleResult();
            return loginUser.getPassword().equals(password);
        } catch (NoResultException e) {
            return false;
        }
    }


    //ideas: findByGroup
}
