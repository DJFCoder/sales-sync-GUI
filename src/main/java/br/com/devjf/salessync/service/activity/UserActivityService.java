package br.com.devjf.salessync.service.activity;

import br.com.devjf.salessync.model.User;

/**
 * Service interface for user activity operations.
 */
public interface UserActivityService {
    /**
     * Registers a user activity.
     * 
     * @param user The user performing the activity
     * @param description The description of the activity
     * @return true if the activity was successfully registered, false otherwise
     */
    boolean registerActivity(User user, String description);
}