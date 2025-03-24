package br.com.devjf.salessync.dao;

import br.com.devjf.salessync.model.Customer;
import br.com.devjf.salessync.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class CustomerDAO implements DAO<Customer> {
    @Override
    public boolean save(Customer customer) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(customer);
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
    public boolean update(Customer customer) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(customer);
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
            Customer customer = em.find(Customer.class, id);
            if (customer != null) {
                em.getTransaction().begin();
                em.remove(customer);
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
    public Customer findById(Integer id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Customer.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Customer> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Customer> query = em.createQuery(
                    "SELECT c FROM Customer c",
                    Customer.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Customer findByTaxId(String taxId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Customer> query = em.createQuery(
                    "SELECT c FROM Customer c WHERE c.taxId = :taxId",
                    Customer.class);
            query.setParameter("taxId", taxId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Find customers with pagination
     *
     * @param page The page number (starting from 0)
     * @param pageSize The number of records per page
     * @return List of customers for the specified page
     */
    public List<Customer> findWithPagination(int page, int pageSize) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Customer> query = em.createQuery(
                    "SELECT c FROM Customer c ORDER BY c.name",
                    Customer.class);
            query.setFirstResult(page * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Find customers by name pattern with pagination
     *
     * @param namePattern The pattern to search for in customer names
     * @param page The page number (starting from 0)
     * @param pageSize The number of records per page
     * @return List of customers matching the pattern for the specified page
     */
    public List<Customer> findByNameWithPagination(String namePattern, int page, int pageSize) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Customer> query = em.createQuery(
                    "SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(:pattern) ORDER BY c.name",
                    Customer.class);
            query.setParameter("pattern", "%" + namePattern + "%");
            query.setFirstResult(page * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
