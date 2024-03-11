package service;

import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Named("implDAO")
public class ImplDAO implements IDAO {
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
    public <T> List<T> get(String namedQuery, Class<T> clazz, Object... params) {
        try(EntityManager em = EntityManagerAdmin.getInstance()) {
            TypedQuery<T> query = em.createNamedQuery(namedQuery, clazz);
            // Si es necesario, se asignan parámetros a la consulta
            if (params != null && params.length > 0) {
                // Se asegura de que haya un número par de argumentos (nombre, valor)
                if (params.length % 2 != 0) {
                    throw new IllegalArgumentException("La cantidad de parámetros debe ser par.");
                }
                // Se establecen los parámetros en la consulta, de dos en dos tomando tanto el nombre como el valor del parámetro
                for (int i = 0; i < params.length; i += 2) {
                    String paramName = (String) params[i];
                    Object paramValue = params[i + 1];
                    query.setParameter(paramName, paramValue);
                }
            }
            return query.getResultList();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
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

    @Override
    public <T> T save(T entity) {
        EntityManager em = EntityManagerAdmin.getInstance();

        try {
            em.getTransaction().begin();

            if (em.contains(entity)) {
                // Si la entidad ya está gestionada por el EntityManager, realiza la actualización
                entity = em.merge(entity);
            } else {
                // Si la entidad no está gestionada, realiza la inserción
                em.persist(entity);
            }

            em.getTransaction().commit();
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return null;
        } finally {
            em.close();
        }
    }
}