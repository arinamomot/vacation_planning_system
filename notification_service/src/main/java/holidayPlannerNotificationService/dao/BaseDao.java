package holidayPlannerNotificationService.dao;

import holidayPlannerNotificationService.exception.PersistenceException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public abstract class BaseDao<T> implements GenericDao<T> {

    @PersistenceContext
    protected EntityManager em;

    protected final Class<T> type;

    protected BaseDao(Class<T> type) { this.type = type;}

    public T find(Integer id){
        Objects.requireNonNull(id);
        try {
            return em.find(type, id);
        }catch(RuntimeException e){
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<T> findAll(){
        try{
            return em.createQuery("SELECT c FROM " + type.getSimpleName() + " c", type).getResultList();
        } catch (RuntimeException e){
            throw new PersistenceException(e);
        }
    }


    @Override
    public void persist(T entity){
        Objects.requireNonNull(entity);
        try{
            em.persist(entity);
        } catch(RuntimeException e){
            throw new PersistenceException(e);
        }
    }

    @Override
    public void persist(Collection<T> entities){
        Objects.requireNonNull(entities);
        if (entities.isEmpty()){
            return;
        }
        try{
            entities.forEach(this::persist);
        } catch (RuntimeException e){
            throw new PersistenceException(e);
        }
    }

    @Override
    public T update (T entity){
        Objects.requireNonNull(entity);
        try{
            return em.merge(entity);
        } catch(RuntimeException e){
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(T entity){
        Objects.requireNonNull(entity);
        try{
            final T toRemove = em.merge(entity);
            if (toRemove != null) {
                em.remove(entity);
            }
        } catch(RuntimeException e){
            throw new PersistenceException(e);
        }
    }

    @Override
    public boolean exists(Integer id){
        return id != null && em.find(type, id) != null;
    }
}
