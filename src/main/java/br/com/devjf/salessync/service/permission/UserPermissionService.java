package br.com.devjf.salessync.service.permission;

import br.com.devjf.salessync.model.User;

/**
 * Service interface for user permission operations.
 */
public interface UserPermissionService {
    /**
     * Gets the permission label for a user.
     * 
     * @param user The user to get the permission label for
     * @return The permission label
     */
    String getPermissionLabel(User user);
    
    /**
     * Checks if a user has access to a panel.
     * 
     * @param user The user to check access for
     * @param panelKey The key of the panel to check access to
     * @return true if the user has access, false otherwise
     */
    boolean hasAccessToPanel(User user, String panelKey);
}