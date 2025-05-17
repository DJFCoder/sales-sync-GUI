package br.com.devjf.salessync.service;

import br.com.devjf.salessync.dao.UserDAO;
import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.model.UserType;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UserService {
    
    private final UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    public boolean createUser(String name, String login, String password, UserType type) {
        // Check if login already exists
        if (userDAO.findByLogin(login) != null) {
            return false;
        }
        
        // Create user directly, password will be hashed in the User constructor
        User user = new User(name, login, password, type);
        return userDAO.save(user);
    }
    
    public User authenticateUser(String login, String password) {
        try {
            User user = userDAO.findByLogin(login);
            if (user != null && user.getPassword() != null) {
                System.out.println("User found: " + login);
                System.out.println("Stored password format: " + user.getPassword());
                System.out.println("Password provided: " + password);
                // password encrypted
                Boolean passwordEncrypted = BCrypt.checkpw(password, user.getPassword());
                System.out.println("Password encrypted: " + passwordEncrypted);
                
                // Only allow BCrypt authentication
                if (user.getPassword().startsWith("$2a$")) {
                    try {
                        // Verify password using BCrypt
                        if (BCrypt.checkpw(password, user.getPassword())) {
                            System.out.println("BCrypt authentication successful");
                            return user;
                        } else {
                            System.out.println("Authentication failed: Incorrect password");
                        }
                    } catch (Exception e) {
                        System.err.println("BCrypt verification error: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Invalid password format: Missing BCrypt signature");
                }
            } else {
                System.out.println("User not found or password is null");
            }
            return null;
        } catch (Exception e) {
            System.err.println("Authentication error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean changePassword(Integer userId, String currentPassword, String newPassword) {
        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                System.err.println("User not found with ID: " + userId);
                return false;
            }

            // Verify current password
            if (BCrypt.checkpw(currentPassword, user.getPassword())) {
                // Hash the new password
                String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                
                // Update user's password
                user.setPassword(hashedNewPassword);
                
                // Save updated user
                return userDAO.update(user);
            } else {
                System.err.println("Current password verification failed for user: " + userId);
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error changing password: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateUser(Integer userId, String name, UserType type) {
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setName(name);
            user.setType(type);
            return userDAO.update(user);
        }
        return false;
    }
    
    public boolean deactivateUser(Integer userId) {
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setActive(false);
            return userDAO.update(user);
        }
        return false;
    }
    
    public boolean reactivateUser(Integer userId) {
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setActive(true);
            return userDAO.update(user);
        }
        return false;
    }
    
    public User getUserById(Integer userId) {
        return userDAO.findById(userId);
    }
    
    public User getUserByLogin(String login) {
        return userDAO.findByLogin(login);
    }
    
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }
    
    /**
     * List all users
     *
     * @return List of all users
     */
    public List<User> listAllUsers() {
        return userDAO.findAll();
    }

    /**
     * List users with optional filtering
     *
     * @param nameFilter Optional name filter
     * @param loginFilter Optional login filter
     * @param typeFilter Optional user type filter
     * @param activeFilter Optional active status filter
     * @return List of users matching the filter criteria
     */
    public List<User> listUsersWithFilters(String nameFilter, String loginFilter, 
                                           UserType typeFilter, Boolean activeFilter) {
        try {
            // If no filters are provided, return all users
            if (nameFilter == null && loginFilter == null && 
                typeFilter == null && activeFilter == null) {
                return listAllUsers();
            }

            // Use UserDAO to fetch filtered users
            return userDAO.findUsersWithFilters(
                    nameFilter, 
                    loginFilter, 
                    typeFilter,
                    activeFilter
            );
        } catch (Exception e) {
            System.err.println("Erro ao filtrar usuários: " + e.getMessage());
            e.printStackTrace();
            return listAllUsers(); // Fallback to listing all users
        }
    }
}