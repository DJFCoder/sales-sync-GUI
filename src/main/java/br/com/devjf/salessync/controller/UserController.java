package br.com.devjf.salessync.controller;

import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.service.UserService;

public class UserController {
    private final UserService user;

    public UserController() {
        this.user = new UserService();
    }

    public String showUserLogged(String login, String password) {
        User getUserLogged = user.authenticateUser(login, password);
        if (getUserLogged != null) {
            System.out.println("Usuário logado: " + getUserLogged.getName());
            System.out.println("Tipo: " + getUserLogged.getType().name());
            return getUserLogged.getName();
        } else {
            System.out.println("Falha na autenticação. Verifique login e senha.");
        }
        return null;
    }
    
}
