package br.com.devjf.salessync;

import br.com.devjf.salessync.controller.UserController;

/**
 *
 * @author devjf
 */
public class SalesSyncApp {
    public static void main(String[] args) {
        UserController admin = new UserController();
        admin.showUserLogged("admin",
                "@devjf123admin");
    }
}
