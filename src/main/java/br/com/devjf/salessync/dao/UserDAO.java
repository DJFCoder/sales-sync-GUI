package br.com.devjf.salessync.dao;

import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.model.UserType;
import br.com.devjf.salessync.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class UserDAO implements DAO<User> {

    @Override
    public boolean save(User user) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean update(User user) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(Integer id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            User user = em.find(User.class, id);
            if (user != null) {
                em.getTransaction().begin();
                user.setActive(false);
                em.merge(user);
                em.getTransaction().commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public User findById(Integer id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<User> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public User findByLogin(String login) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.login = :login AND u.active = true", User.class);
            query.setParameter("login", login);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Find users with optional filtering
     *
     * @param nameFilter Optional name filter
     * @param loginFilter Optional login filter
     * @param typeFilter Optional user type filter
     * @param activeFilter Optional active status filter
     * @return List of users matching the filter criteria
     */
    public List<User> findUsersWithFilters(String nameFilter, String loginFilter, 
                                           UserType typeFilter, Boolean activeFilter) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            // Start building the JPQL query
            StringBuilder queryBuilder = new StringBuilder("SELECT u FROM User u WHERE 1=1");
            
            // Add filter conditions
            if (nameFilter != null && !nameFilter.isEmpty()) {
                queryBuilder.append(" AND LOWER(u.name) LIKE LOWER(:name)");
            }
            if (loginFilter != null && !loginFilter.isEmpty()) {
                queryBuilder.append(" AND LOWER(u.login) LIKE LOWER(:login)");
            }
            if (typeFilter != null) {
                queryBuilder.append(" AND u.type = :type");
            }
            if (activeFilter != null) {
                queryBuilder.append(" AND u.active = :active");
            }
            
            // Create the query
            TypedQuery<User> query = em.createQuery(queryBuilder.toString(), User.class);

            // Set parameters if filters are provided
            if (nameFilter != null && !nameFilter.isEmpty()) {
                query.setParameter("name", "%" + nameFilter + "%");
            }
            if (loginFilter != null && !loginFilter.isEmpty()) {
                query.setParameter("login", "%" + loginFilter + "%");
            }
            if (typeFilter != null) {
                query.setParameter("type", typeFilter);
            }
            if (activeFilter != null) {
                query.setParameter("active", activeFilter);
            }

            // Return filtered results
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}