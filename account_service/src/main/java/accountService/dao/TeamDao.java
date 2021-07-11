package accountService.dao;

import accountService.model.Team;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class TeamDao extends BaseDao<Team> {

    public TeamDao() {
        super(Team.class);
    }

    /*
    public List<RequestToJoinTeam> findNewRequests(){
        try{
            return em.createQuery("SELECT r FROM RequestToJoinTeam r WHERE r.state = 'IN_PROGRESS'", RequestToJoinTeam.class).getResultList();
        } catch (RuntimeException e){
            throw new PersistenceException(e);
        }
    }
     */

    public Team findByName(String name) {
        try {
            return em.createNamedQuery("Team.findByName", Team.class).setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
