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
        
        User user = new User(name, login, password, type);
        return userDAO.save(user);
    }
    
    public User authenticateUser(String login, String password) {
        try {
            User user = userDAO.findByLogin(login);
            if (user != null && user.getPassword() != null) {
                // Verifica se a senha está no formato BCrypt
                if (user.getPassword().startsWith("$2a$") && BCrypt.checkpw(password, user.getPassword())) {
                    return user;
                }
                // Caso a senha não esteja no formato BCrypt, verifica igualdade direta (para testes)
                else if (user.getPassword().equals(password)) {
                    return user;
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("Erro na autenticação: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean changePassword(Integer userId, String currentPassword, String newPassword) {
        User user = userDAO.findById(userId);
        if (user != null && BCrypt.checkpw(currentPassword, user.getPassword())) {
            user.changePassword(newPassword);
            return userDAO.update(user);
        }
        return false;
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
}