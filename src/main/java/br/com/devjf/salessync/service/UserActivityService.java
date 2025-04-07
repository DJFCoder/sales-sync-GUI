package br.com.devjf.salessync.service;

import br.com.devjf.salessync.dao.UserActivityDAO;
import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.model.UserActivity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserActivityService {
    
    private final UserActivityDAO activityDAO;
    
    public UserActivityService() {
        this.activityDAO = new UserActivityDAO();
    }
    
    /**
     * Registra uma nova atividade para o usuário
     * 
     * @param user Usuário que realizou a atividade
     * @param description Descrição da atividade
     * @return true se a atividade foi registrada com sucesso
     */
    public boolean registerActivity(User user, String description) {
        if (user == null || description == null || description.trim().isEmpty()) {
            return false;
        }
        
        UserActivity activity = new UserActivity(user, description);
        return activityDAO.save(activity);
    }
    
    /**
     * Busca todas as atividades de um usuário
     * 
     * @param user Usuário para buscar as atividades
     * @return Lista de atividades do usuário
     */
    public List<UserActivity> getUserActivities(User user) {
        if (user == null) {
            return List.of();
        }
        return activityDAO.findByUser(user);
    }
    
    /**
     * Busca as atividades mais recentes de um usuário
     * 
     * @param user Usuário para buscar as atividades
     * @param limit Número máximo de atividades a retornar
     * @return Lista de atividades recentes do usuário
     */
    public List<UserActivity> getRecentActivities(User user, int limit) {
        if (user == null || limit <= 0) {
            return List.of();
        }
        return activityDAO.findRecentByUser(user, limit);
    }
    
    /**
     * Obtém o último acesso do usuário
     * 
     * @param user Usuário para buscar o último acesso
     * @return Data e hora do último acesso ou null se não houver registro
     */
    public LocalDateTime getLastAccess(User user) {
        if (user == null) {
            return null;
        }
        
        List<UserActivity> activities = activityDAO.findRecentByUser(user, 1);
        if (activities.isEmpty()) {
            return null;
        }
        
        return activities.get(0).getActivityTime();
    }
    
    /**
     * Retrieves all user activities.
     *
     * @return List of all UserActivity entries
     */
    public List<UserActivity> findAllUserActivities() {
        try {
            return activityDAO.findAllUserActivities();
        } catch (Exception e) {
            System.err.println("Erro no serviço ao buscar todas as atividades: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
