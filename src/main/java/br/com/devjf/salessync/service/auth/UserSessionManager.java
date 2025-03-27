package br.com.devjf.salessync.service.auth;

import br.com.devjf.salessync.model.User;

public class UserSessionManager {
    private static UserSessionManager instance;
    private User loggedUser;

    private UserSessionManager() {}

    public static synchronized UserSessionManager getInstance() {
        if (instance == null) {
            instance = new UserSessionManager();
        }
        return instance;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void clearSession() {
        this.loggedUser = null;
    }

    public boolean isLoggedIn() {
        return loggedUser != null;
    }
    
    /*
    public boolean hasPermission(String permission) {
        // Lógica de verificação de permissão
        return loggedUser != null && loggedUser.getType();
    }
    */
}