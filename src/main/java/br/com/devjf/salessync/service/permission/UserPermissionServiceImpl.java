package br.com.devjf.salessync.service.permission;

import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.model.UserType;

/**
 * Implementation of UserPermissionService.
 */
public class UserPermissionServiceImpl implements UserPermissionService {
    
    @Override
    public String getPermissionLabel(User user) {
        if (user == null) {
            return "???";
        }
        
        UserType userType = user.getType();
        switch (userType) {
            case ADMIN:
                return "ADM";
            case OWNER:
                return "OWN";
            case EMPLOYEE:
                return "EMP";
            default:
                return "???";
        }
    }
    
    @Override
    public boolean hasAccessToPanel(User user, String panelKey) {
        // Basic implementation - can be expanded with more complex permission logic
        if (user == null) {
            return false;
        }
        
        // Example: Only ADMIN and OWNER can access Users panel
        if ("Usuários".equals(panelKey)) {
            return user.getType() == UserType.ADMIN || user.getType() == UserType.OWNER;
        }
        
        // By default, all authenticated users have access to other panels
        return true;
    }
}