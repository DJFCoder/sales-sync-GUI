package br.com.devjf.salessync.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.model.UserActivity;
import br.com.devjf.salessync.model.UserType;
import br.com.devjf.salessync.service.UserActivityService;
import br.com.devjf.salessync.service.UserService;
import br.com.devjf.salessync.service.activity.UserActivityServiceImpl;
import br.com.devjf.salessync.service.permission.UserPermissionService;
import br.com.devjf.salessync.service.permission.UserPermissionServiceImpl;

/**
 * Controller class for managing user-related operations.
 * Provides methods for user authentication, creation, updates, and activity tracking.
 */
public class UserController {
    private final UserService userService;
    private final UserActivityService activityService;
    private final UserActivityServiceImpl userActivityService;
    private final UserPermissionService permissionService;

    /**
     * Constructs a new UserController with UserService and UserActivityService instances.
     */
    public UserController() {
        this.userService = new UserService();
        this.activityService = new UserActivityService();
        this.userActivityService = new UserActivityServiceImpl(this);
        this.permissionService = new UserPermissionServiceImpl();
    }

    /**
     * Displays information about the logged-in user.
     *
     * @param login The user's login
     * @param password The user's password
     * @return The name of the logged-in user, or null if authentication fails
     */
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
    
    /**
     * Creates a new user in the system.
     *
     * @param name The user's name
     * @param login The user's login (username)
     * @param password The user's password
     * @param type The user's type/role
     * @return true if the user was successfully created, false otherwise
     */
    public boolean createUser(String name, String login, String password, UserType type) {
        return userService.createUser(name, login, password, type);
    }
    
    /**
     * Updates an existing user's information.
     *
     * @param userId The ID of the user to update
     * @param name The new name for the user
     * @param type The new type/role for the user
     * @return true if the user was successfully updated, false otherwise
     */
    public boolean updateUser(Integer userId, String name, UserType type) {
        return userService.updateUser(userId, name, type);
    }
    
    /**
     * Changes a user's password.
     *
     * @param userId The ID of the user
     * @param currentPassword The user's current password
     * @param newPassword The new password to set
     * @return true if the password was successfully changed, false otherwise
     */
    public boolean changePassword(Integer userId, String currentPassword, String newPassword) {
        return userService.changePassword(userId, currentPassword, newPassword);
    }
    
    /**
     * Deactivates a user account.
     *
     * @param userId The ID of the user to deactivate
     * @return true if the user was successfully deactivated, false otherwise
     */
    public boolean deactivateUser(Integer userId) {
        return userService.deactivateUser(userId);
    }
    
    /**
     * Reactivates a previously deactivated user account.
     *
     * @param userId The ID of the user to reactivate
     * @return true if the user was successfully reactivated, false otherwise
     */
    public boolean reactivateUser(Integer userId) {
        return userService.reactivateUser(userId);
    }
    
    /**
     * Lists all users in the system.
     *
     * @return A list of all users
     */
    public List<User> listAllUsers() {
        return userService.getAllUsers();
    }
    
    /**
     * Finds a user by their ID.
     *
     * @param id The ID of the user to find
     * @return The user object if found, null otherwise
     */
    public User findUserById(Integer id) {
        return userService.getUserById(id);
    }
    
    /**
     * Finds a user by their login (username).
     *
     * @param login The login of the user to find
     * @return The user object if found, null otherwise
     */
    public User findUserByLogin(String login) {
        return userService.getUserByLogin(login);
    }
    
    /**
     * Authenticates a user and records the login activity.
     *
     * @param login The user's login
     * @param password The user's password
     * @return The authenticated user object if successful, null otherwise
     */
    public User authenticateUser(String login, String password) {
        User user = userService.authenticateUser(login, password);
        if (user != null) {
            // Registrar atividade de login
            activityService.registerActivity(user, "Login no sistema");
        }
        return user;
    }
    
    /**
     * Registers a new activity for the user.
     * 
     * @param user User who performed the activity
     * @param description Description of the activity
     * @return true if the activity was successfully registered
     */
    public boolean registerActivity(User user, String description) {
        return activityService.registerActivity(user, description);
    }
    
    /**
     * Gets the recent activities of a user.
     * 
     * @param user User to get activities for
     * @param limit Maximum number of activities to return
     * @return List of recent user activities
     */
    public List<UserActivity> getRecentActivities(User user, int limit) {
        return activityService.getRecentActivities(user, limit);
    }
    
    /**
     * Gets the formatted last access time of a user.
     * 
     * @param user User to get the last access for
     * @return Formatted string with date and time of last access or "Primeiro acesso" if no record exists
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
     * Translates the user type to Portuguese.
     * 
     * @param type User type
     * @return Name of the user type in Portuguese
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
    
    /**
     * Checks if a user has access to a specific panel.
     * 
     * @param user The user to check permissions for
     * @param panelName The name of the panel to check access for
     * @return true if the user has access, false otherwise
     */
    public boolean hasAccessToPanel(User user, String panelName) {
        return permissionService.hasAccessToPanel(user, panelName);
    }
    
    /**
     * Gets the permission label for a user.
     * 
     * @param user The user to get the permission label for
     * @return The permission label string
     */
    public String getPermissionLabel(User user) {
        return permissionService.getPermissionLabel(user);
    }
    
    /**
     * Registers a user activity through the activity service.
     * 
     * @param user The user who performed the activity
     * @param description Description of the activity
     */
    public void registerUserActivity(User user, String description) {
        userActivityService.registerActivity(user, description);
    }
}
