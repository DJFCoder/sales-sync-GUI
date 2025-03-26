package br.com.devjf.salessync.dao;

import br.com.devjf.salessync.model.Expense;
import br.com.devjf.salessync.model.ExpenseCategory;
import br.com.devjf.salessync.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class ExpenseDAO implements DAO<Expense> {
    @Override
    public boolean save(Expense expense) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(expense);
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
    public boolean update(Expense expense) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(expense);
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
            Expense expense = em.find(Expense.class,
                    id);
            if (expense != null) {
                em.getTransaction().begin();
                em.remove(expense);
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
    public Expense findById(Integer id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Expense.class,
                    id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Expense> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Expense> query = em.createQuery("SELECT e FROM Expense e",
                    Expense.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Expense> findByCategory(ExpenseCategory category) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Expense> query = em.createQuery(
                    "SELECT e FROM Expense e WHERE e.category = :category ORDER BY e.date DESC",
                    Expense.class
            );
            query.setParameter("category",
                    category);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Expense> findByDateRange(LocalDate start, LocalDate end) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Expense> query = em.createQuery(
                    "SELECT e FROM Expense e WHERE e.date BETWEEN :start AND :end ORDER BY e.date DESC",
                    Expense.class
            );
            query.setParameter("start",
                    start);
            query.setParameter("end",
                    end);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Expense> findByDateRange(Integer categoryId, LocalDate start, LocalDate end) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            String queryString = "SELECT e FROM Expense e WHERE e.date BETWEEN :start AND :end";
            
            // Adiciona filtro de categoria se categoryId não for nulo
            if (categoryId != null) {
                queryString += " AND e.category.id = :categoryId";
            }
            
            queryString += " ORDER BY e.date DESC";
            
            TypedQuery<Expense> query = em.createQuery(queryString, Expense.class);
            query.setParameter("start", start);
            query.setParameter("end", end);
            
            if (categoryId != null) {
                query.setParameter("categoryId", categoryId);
            }
            
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
