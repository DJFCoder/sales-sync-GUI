package br.com.devjf.salessync.dao;

import br.com.devjf.salessync.model.Sale;
import br.com.devjf.salessync.model.Customer;
import br.com.devjf.salessync.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

public class SaleDAO implements DAO<Sale> {

    @Override
    public boolean save(Sale sale) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(sale);
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
    public boolean update(Sale sale) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(sale);
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
            Sale sale = em.find(Sale.class, id);
            if (sale != null) {
                em.getTransaction().begin();
                em.remove(sale);
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
    public Sale findById(Integer id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Sale.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Sale> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Sale> query = em.createQuery(
                    "SELECT s FROM Sale s ORDER BY s.canceled ASC, s.id DESC", 
                    Sale.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Sale> findByCustomer(Customer customer) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Sale> query = em.createQuery(
                "SELECT s FROM Sale s WHERE s.customer = :customer ORDER BY s.date DESC", 
                Sale.class
            );
            query.setParameter("customer", customer);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Sale> findByCustomerId(Integer customerId) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Sale> query = em.createQuery(
                "SELECT s FROM Sale s WHERE s.customer.id = :customerId", Sale.class);
            query.setParameter("customerId", customerId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Sale> findByDateRange(LocalDateTime start, LocalDateTime end) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<Sale> query = em.createQuery(
                "SELECT s FROM Sale s WHERE s.date BETWEEN :start AND :end ORDER BY s.date DESC", 
                Sale.class
            );
            query.setParameter("start", start);
            query.setParameter("end", end);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Busca uma venda pelo ID com carregamento eager de itens, cliente e usuário.
     *
     * @param id O ID da venda a ser buscada
     * @return A venda encontrada com suas relações ou null se não existir
     */
    public Sale findByIdWithRelationships(Integer id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            try {
                // Buscar a venda com join fetch para carregar os itens
                Sale sale = em.createQuery(
                        "SELECT s FROM Sale s "
                        + "LEFT JOIN FETCH s.items "
                        + "LEFT JOIN FETCH s.customer "
                        + "LEFT JOIN FETCH s.user "
                        + "WHERE s.id = :id",
                        Sale.class)
                        .setParameter("id", id)
                        .getSingleResult();
                return sale;
            } catch (NoResultException e) {
                System.err.println("Venda não encontrada com ID: " + id);
                return null;
            } catch (Exception e) {
                System.err.println("Erro ao buscar venda por ID: " + e.getMessage());
                return null;
            }
        } finally {
            em.close();
        }
    }
}