package br.com.devjf.salessync.service.navigation;

import javax.swing.JPanel;

/**
 * Service interface for panel navigation operations.
 * Handles showing, adding, and removing panels in the application.
 */
public interface PanelNavigationService {
    /**
     * Shows a panel with the specified key.
     * 
     * @param panel The panel to show
     * @param panelKey The key identifying the panel
     */
    void showPanel(JPanel panel, String panelKey);
    
    /**
     * Adds a panel to the container with the specified key.
     * 
     * @param panel The panel to add
     * @param panelKey The key identifying the panel
     */
    void addPanel(JPanel panel, String panelKey);
    
    /**
     * Removes a panel with the specified key from the container.
     * 
     * @param panelKey The key identifying the panel to remove
     */
    void removePanel(String panelKey);
    
    /**
     * Checks if a panel with the specified key is loaded.
     * 
     * @param panelKey The key identifying the panel
     * @return true if the panel is loaded, false otherwise
     */
    boolean isPanelLoaded(String panelKey);
    
    /**
     * Gets a loaded panel by its key.
     * 
     * @param panelKey The key identifying the panel
     * @return The panel, or null if not found
     */
    JPanel getPanel(String panelKey);
}