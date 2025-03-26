package br.com.devjf.salessync.dao;

import br.com.devjf.salessync.model.ExpenseCategory;
import br.com.devjf.salessync.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ExpenseCategoryDAO implements DAO<ExpenseCategory> {
    @Override
    public boolean save(ExpenseCategory category) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(category);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean update(ExpenseCategory category) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(category);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(Integer id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            ExpenseCategory category = em.find(ExpenseCategory.class,
                    id);
            if (category != null) {
                em.getTransaction().begin();
                em.remove(category);
                em.getTransaction().commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public ExpenseCategory findById(Integer id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(ExpenseCategory.class,
                    id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<ExpenseCategory> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<ExpenseCategory> query = em.createQuery(
                    "SELECT c FROM ExpenseCategory c",
                    ExpenseCategory.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public ExpenseCategory findByName(String name) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<ExpenseCategory> query = em.createQuery(
                    "SELECT c FROM ExpenseCategory c WHERE c.name = :name",
                    ExpenseCategory.class);
            query.setParameter("name",
                    name);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
