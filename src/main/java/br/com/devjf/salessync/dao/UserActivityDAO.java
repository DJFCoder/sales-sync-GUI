package br.com.devjf.salessync.dao;

import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.model.UserActivity;
import br.com.devjf.salessync.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class UserActivityDAO {
    
    public boolean save(UserActivity activity) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(activity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Erro ao salvar atividade: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
    
    public List<UserActivity> findByUser(User user) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<UserActivity> query = builder.createQuery(UserActivity.class);
            Root<UserActivity> root = query.from(UserActivity.class);
            
            query.select(root)
                 .where(builder.equal(root.get("user"), user))
                 .orderBy(builder.desc(root.get("activityTime")));
            
            TypedQuery<UserActivity> typedQuery = em.createQuery(query);
            return typedQuery.getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar atividades do usuário: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
    
    public List<UserActivity> findRecentByUser(User user, int limit) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<UserActivity> query = builder.createQuery(UserActivity.class);
            Root<UserActivity> root = query.from(UserActivity.class);
            
            query.select(root)
                 .where(builder.equal(root.get("user"), user))
                 .orderBy(builder.desc(root.get("activityTime")));
            
            TypedQuery<UserActivity> typedQuery = em.createQuery(query);
            typedQuery.setMaxResults(limit);
            return typedQuery.getResultList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar atividades recentes do usuário: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }
    
    public UserActivity findById(Integer id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(UserActivity.class, id);
        } catch (Exception e) {
            System.err.println("Erro ao buscar atividade por ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }
}
