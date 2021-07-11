package accountService.dao;

import accountService.model.Group;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class GroupDao extends BaseDao<Group> {

    public GroupDao() {
        super(Group.class);
    }

    public Group findByName(String name) {
        try {
            return em.createNamedQuery("Group.findByName", Group.class).setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
