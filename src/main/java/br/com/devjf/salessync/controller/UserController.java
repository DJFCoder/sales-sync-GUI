package br.com.devjf.salessync.controller;

import java.util.List;
import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.model.UserType;
import br.com.devjf.salessync.service.UserService;

public class UserController {
    private final UserService userService;

    public UserController() {
        this.userService = new UserService();
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
        return userService.authenticateUser(login, password);
    }
}
