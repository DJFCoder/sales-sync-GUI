package br.com.devjf.salessync.dao;

import br.com.devjf.salessync.model.SystemLog;
import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;

public class SystemLogDAO implements DAO<SystemLog> {

    @Override
    public boolean save(SystemLog log) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(log);
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
    public boolean update(SystemLog log) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(log);
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
            SystemLog log = em.find(SystemLog.class, id);
            if (log != null) {
                em.getTransaction().begin();
                em.remove(log);
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
    public SystemLog findById(Integer id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(SystemLog.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<SystemLog> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<SystemLog> query = em.createQuery("SELECT l FROM SystemLog l ORDER BY l.dateTime DESC", SystemLog.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<SystemLog> findByUser(User user) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<SystemLog> query = em.createQuery(
                "SELECT l FROM SystemLog l WHERE l.user = :user ORDER BY l.dateTime DESC", 
                SystemLog.class
            );
            query.setParameter("user", user);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<SystemLog> findByAction(String action) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<SystemLog> query = em.createQuery(
                "SELECT l FROM SystemLog l WHERE l.action LIKE :action ORDER BY l.dateTime DESC", 
                SystemLog.class
            );
            query.setParameter("action", "%" + action + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<SystemLog> findByDateRange(LocalDateTime start, LocalDateTime end) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<SystemLog> query = em.createQuery(
                "SELECT l FROM SystemLog l WHERE l.dateTime BETWEEN :start AND :end ORDER BY l.dateTime DESC", 
                SystemLog.class
            );
            query.setParameter("start", start);
            query.setParameter("end", end);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}