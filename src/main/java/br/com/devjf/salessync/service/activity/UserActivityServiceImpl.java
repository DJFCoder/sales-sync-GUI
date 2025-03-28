package br.com.devjf.salessync.service.activity;

import br.com.devjf.salessync.controller.UserController;
import br.com.devjf.salessync.model.User;

/**
 * Implementation of UserActivityService.
 */
public class UserActivityServiceImpl implements UserActivityService {
    private final UserController userController;
    
    /**
     * Constructs a new UserActivityServiceImpl with a UserController.
     * 
     * @param userController The controller for user operations
     */
    public UserActivityServiceImpl(UserController userController) {
        this.userController = userController;
    }
    
    @Override
    public boolean registerActivity(User user, String description) {
        if (user == null || description == null || description.isEmpty()) {
            return false;
        }
        
        try {
            userController.registerActivity(user, description);
            return true;
        } catch (Exception e) {
            System.err.println("Error registering user activity: " + e.getMessage());
            return false;
        }
    }
}