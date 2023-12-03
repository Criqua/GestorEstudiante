package service;

import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Named("implDAO")
public class ImplDAO implements IDAO{
    @Override
    public <T> List<T> getAll(String namedQuery, Class<T> clazz) {
        EntityManager em = EntityManagerAdmin.getInstance();

        try {
            TypedQuery<T> query = em.createNamedQuery(namedQuery, clazz);
            return query.getResultList();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally {
            em.close();
        }
    }

    @Override
    public <T> List<T> get(String namedQuery, Class<T> clazz, Object... param) {
        EntityManager em = EntityManagerAdmin.getInstance();
        try {
            TypedQuery<T> query = em.createNamedQuery(namedQuery, clazz);
            // Asegúrate de que la consulta nombrada tenga un parámetro llamado "cif"
            if (param.length > 0) {
                query.setParameter("cif", param[0]);
            }
            return query.getResultList();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally {
            em.close();
        }
    }

    @Override
    public <T, ID> T findById(Class<T> clazz, ID id) {
        EntityManager em = EntityManagerAdmin.getInstance();
        try {
            T entity = em.find(clazz,id);
            return entity;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            em.close();
        }
    }

    @Override
    public <T> void remove(T entity) {
        EntityManager em = EntityManagerAdmin.getInstance();
        try {
            em.getTransaction().begin();
            em.remove(em.merge(entity));
            em.flush();
            em.getTransaction().commit();
        }
        catch(Exception e){
            e.printStackTrace();
            em.getTransaction().rollback();
        }
        finally {
            em.close();
        }
    }

    @Override
    public <T> void insert(T entity) {
        EntityManager em = EntityManagerAdmin.getInstance();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.flush();
            em.getTransaction().commit();
        }
        catch(Exception e){
            e.printStackTrace();
            em.getTransaction().rollback();
        }
        finally {
            em.close();
        }
    }

    @Override
    public <T> T update(T entity) {
        EntityManager em = EntityManagerAdmin.getInstance();
        T entityUpdate = null;
        try {
            em.getTransaction().begin();
            entityUpdate = em.merge(entity);
            em.flush();
            em.getTransaction().commit();
        }
        catch(Exception e){
            e.printStackTrace();
            em.getTransaction().rollback();
        }
        finally {
            em.close();
        }
        return entityUpdate;
    }
}