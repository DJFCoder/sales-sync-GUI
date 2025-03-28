package br.com.devjf.salessync.controller.navigation;

import javax.swing.JPanel;
import br.com.devjf.salessync.service.navigation.PanelNavigationService;

/**
 * Controller class for panel navigation operations.
 */
public class PanelNavigationController {
    private final PanelNavigationService navigationService;
    private final PanelFactory panelFactory;
    
    /**
     * Constructs a new PanelNavigationController with the specified services.
     * 
     * @param navigationService The service for panel navigation
     * @param panelFactory The factory for creating panels
     */
    public PanelNavigationController(PanelNavigationService navigationService, PanelFactory panelFactory) {
        this.navigationService = navigationService;
        this.panelFactory = panelFactory;
    }
    
    /**
     * Navigates to a panel with the specified key.
     * 
     * @param panelKey The key of the panel to navigate to
     */
    public void navigateToPanel(String panelKey) {
        if (navigationService.isPanelLoaded(panelKey)) {
            JPanel panel = navigationService.getPanel(panelKey);
            navigationService.showPanel(panel, panelKey);
        } else {
            JPanel panel = panelFactory.createPanel(panelKey);
            if (panel != null) {
                navigationService.showPanel(panel, panelKey);
            }
        }
    }
    
    /**
     * Navigates to a panel with the specified key and object.
     * 
     * @param panelKey The key of the panel to navigate to
     * @param object The object to pass to the panel
     */
    public void navigateToPanel(String panelKey, Object object) {
        // For panels that require an object (like edit forms), we always create a new panel
        JPanel panel = panelFactory.createPanel(panelKey, object);
        if (panel != null) {
            // Remove existing panel with this key if it exists
            if (navigationService.isPanelLoaded(panelKey)) {
                navigationService.removePanel(panelKey);
            }
            navigationService.showPanel(panel, panelKey);
        }
    }
    
    /**
     * Refreshes a panel with the specified key.
     * 
     * @param panelKey The key of the panel to refresh
     */
    public void refreshPanel(String panelKey) {
        if (navigationService.isPanelLoaded(panelKey)) {
            navigationService.removePanel(panelKey);
        }
        navigateToPanel(panelKey);
    }
}