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
        if (user == null) {
            return false;
        }
        
        switch (user.getType()) {
            case ADMIN:
                return panelKey.equals("Dashboard") || 
                       panelKey.equals("Usuários") || 
                       panelKey.equals("Logs do Sistema");
            
            case OWNER:
            case EMPLOYEE:
                return panelKey.equals("Dashboard") || 
                       panelKey.equals("Vendas") || 
                       panelKey.equals("Clientes") || 
                       panelKey.equals("Despesas") || 
                       panelKey.equals("Relatórios");
            
            default:
                return false;
        }
    }
}