package br.com.devjf.salessync.dao;

import br.com.devjf.salessync.model.Sale;
import br.com.devjf.salessync.model.SaleItem;
import br.com.devjf.salessync.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class SaleItemDAO implements DAO<SaleItem> {
    
    @Override
    public boolean save(SaleItem saleItem) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(saleItem);
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
    public boolean update(SaleItem saleItem) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(saleItem);
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
            SaleItem saleItem = em.find(SaleItem.class, id);
            if (saleItem != null) {
                em.getTransaction().begin();
                em.remove(saleItem);
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
    public SaleItem findById(Integer id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(SaleItem.class, id);
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<SaleItem> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<SaleItem> query = em.createQuery("SELECT si FROM SaleItem si", SaleItem.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<SaleItem> findBySale(Sale sale) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<SaleItem> query = em.createQuery(
                "SELECT si FROM SaleItem si WHERE si.sale = :sale", SaleItem.class);
            query.setParameter("sale", sale);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}