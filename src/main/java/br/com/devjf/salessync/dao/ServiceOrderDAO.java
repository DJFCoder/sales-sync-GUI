package br.com.devjf.salessync.dao;

import br.com.devjf.salessync.model.Customer;
import br.com.devjf.salessync.model.ServiceOrder;
import br.com.devjf.salessync.model.ServiceStatus;
import br.com.devjf.salessync.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

public class ServiceOrderDAO implements DAO<ServiceOrder> {

    @Override
    public boolean save(ServiceOrder serviceOrder) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(serviceOrder);
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
    public boolean update(ServiceOrder serviceOrder) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(serviceOrder);
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
            ServiceOrder serviceOrder = em.find(ServiceOrder.class, id);
            if (serviceOrder != null) {
                em.getTransaction().begin();
                em.remove(serviceOrder);
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
    public ServiceOrder findById(Integer id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(ServiceOrder.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<ServiceOrder> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<ServiceOrder> query = em.createQuery("SELECT s FROM ServiceOrder s", ServiceOrder.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<ServiceOrder> findByCustomer(Customer customer) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<ServiceOrder> query = em.createQuery(
                "SELECT s FROM ServiceOrder s WHERE s.customer = :customer ORDER BY s.requestDate DESC", 
                ServiceOrder.class
            );
            query.setParameter("customer", customer);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<ServiceOrder> findByStatus(ServiceStatus status) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<ServiceOrder> query = em.createQuery(
                "SELECT s FROM ServiceOrder s WHERE s.status = :status ORDER BY s.requestDate DESC", 
                ServiceOrder.class
            );
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<ServiceOrder> findByDateRange(LocalDate start, LocalDate end) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<ServiceOrder> query = em.createQuery(
                "SELECT s FROM ServiceOrder s WHERE s.requestDate BETWEEN :start AND :end ORDER BY s.requestDate DESC", 
                ServiceOrder.class
            );
            query.setParameter("start", start);
            query.setParameter("end", end);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}