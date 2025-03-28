package br.com.devjf.salessync.service.navigation;

import java.awt.CardLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Implementation of PanelNavigationService using CardLayout.
 */
public class PanelNavigationServiceImpl implements PanelNavigationService {
    private final JPanel containerPanel;
    private final CardLayout cardLayout;
    private final Map<String, JPanel> loadedPanels;
    
    /**
     * Constructs a new PanelNavigationServiceImpl with the specified container panel.
     * 
     * @param containerPanel The panel that will contain the navigable panels
     */
    public PanelNavigationServiceImpl(JPanel containerPanel) {
        this.containerPanel = containerPanel;
        this.cardLayout = (CardLayout) containerPanel.getLayout();
        this.loadedPanels = new HashMap<>();
    }
    
    @Override
    public void showPanel(JPanel panel, String panelKey) {
        addPanel(panel, panelKey);
        cardLayout.show(containerPanel, panelKey);
        
        // Force layout recalculation
        SwingUtilities.invokeLater(() -> {
            containerPanel.revalidate();
            containerPanel.repaint();
        });
    }
    
    @Override
    public void addPanel(JPanel panel, String panelKey) {
        if (!loadedPanels.containsKey(panelKey)) {
            containerPanel.add(panel, panelKey);
            loadedPanels.put(panelKey, panel);
            containerPanel.validate();
        }
    }
    
    @Override
    public void removePanel(String panelKey) {
        JPanel panel = loadedPanels.get(panelKey);
        if (panel != null) {
            containerPanel.remove(panel);
            loadedPanels.remove(panelKey);
            containerPanel.validate();
        }
    }
    
    @Override
    public boolean isPanelLoaded(String panelKey) {
        return loadedPanels.containsKey(panelKey);
    }
    
    @Override
    public JPanel getPanel(String panelKey) {
        return loadedPanels.get(panelKey);
    }
}