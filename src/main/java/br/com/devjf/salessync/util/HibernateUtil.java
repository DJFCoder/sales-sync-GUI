package br.com.devjf.salessync.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class HibernateUtil {
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY;
    
    static {
        try {
            ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("SalesSync-PU");
        } catch (Exception e) {
            System.err.println("Erro ao inicializar o EntityManagerFactory: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public static EntityManager getEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }
    
    public static void shutdown() {
        if (ENTITY_MANAGER_FACTORY != null && ENTITY_MANAGER_FACTORY.isOpen()) {
            ENTITY_MANAGER_FACTORY.close();
        }
    }
}