package br.com.devjf.salessync.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.model.UserActivity;
import br.com.devjf.salessync.model.UserType;
import br.com.devjf.salessync.service.UserActivityService;
import br.com.devjf.salessync.service.UserService;

public class UserController {
    private final UserService userService;
    private final UserActivityService activityService;

    public UserController() {
        this.userService = new UserService();
        this.activityService = new UserActivityService();
    }

    public String showUserLogged(String login, String password) {
        User getUserLogged = userService.authenticateUser(login, password);
        if (getUserLogged != null) {
            System.out.println("Usuário logado: " + getUserLogged.getName());
            System.out.println("Tipo: " + getUserLogged.getType().name());
            return getUserLogged.getName();
        } else {
            System.out.println("Falha na autenticação. Verifique login e senha.");
        }
        return null;
    }
    
    public boolean createUser(String name, String login, String password, UserType type) {
        return userService.createUser(name, login, password, type);
    }
    
    public boolean updateUser(Integer userId, String name, UserType type) {
        return userService.updateUser(userId, name, type);
    }
    
    public boolean changePassword(Integer userId, String currentPassword, String newPassword) {
        return userService.changePassword(userId, currentPassword, newPassword);
    }
    
    public boolean deactivateUser(Integer userId) {
        return userService.deactivateUser(userId);
    }
    
    public boolean reactivateUser(Integer userId) {
        return userService.reactivateUser(userId);
    }
    
    public List<User> listAllUsers() {
        return userService.getAllUsers();
    }
    
    public User findUserById(Integer id) {
        return userService.getUserById(id);
    }
    
    public User findUserByLogin(String login) {
        return userService.getUserByLogin(login);
    }
    
    public User authenticateUser(String login, String password) {
        User user = userService.authenticateUser(login, password);
        if (user != null) {
            // Registrar atividade de login
            activityService.registerActivity(user, "Login no sistema");
        }
        return user;
    }
    
    /**
     * Registra uma nova atividade para o usuário
     * 
     * @param user Usuário que realizou a atividade
     * @param description Descrição da atividade
     * @return true se a atividade foi registrada com sucesso
     */
    public boolean registerActivity(User user, String description) {
        return activityService.registerActivity(user, description);
    }
    
    /**
     * Obtém as atividades recentes do usuário
     * 
     * @param user Usuário para buscar as atividades
     * @param limit Número máximo de atividades a retornar
     * @return Lista de atividades recentes do usuário
     */
    public List<UserActivity> getRecentActivities(User user, int limit) {
        return activityService.getRecentActivities(user, limit);
    }
    
    /**
     * Obtém o último acesso do usuário formatado
     * 
     * @param user Usuário para buscar o último acesso
     * @return String formatada com a data e hora do último acesso ou "Primeiro acesso" se não houver registro
     */
    public String getLastAccessFormatted(User user) {
        LocalDateTime lastAccess = activityService.getLastAccess(user);
        if (lastAccess == null) {
            return "Primeiro acesso";
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return lastAccess.format(formatter);
    }
    
    /**
     * Traduz o tipo de usuário para português
     * 
     * @param type Tipo de usuário
     * @return Nome do tipo de usuário em português
     */
    public String getUserTypeInPortuguese(UserType type) {
        if (type == null) {
            return "Desconhecido";
        }
        
        switch (type) {
            case ADMIN:
                return "Administrador";
            case OWNER:
                return "Proprietário";
            case EMPLOYEE:
                return "Funcionário";
            default:
                return "Desconhecido";
        }
    }
}
