package br.com.devjf.salessync.view;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;

import br.com.devjf.salessync.controller.UserController;
import br.com.devjf.salessync.controller.navigation.PanelFactory;
import br.com.devjf.salessync.controller.navigation.PanelNavigationController;
import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.service.activity.UserActivityService;
import br.com.devjf.salessync.service.activity.UserActivityServiceImpl;
import br.com.devjf.salessync.service.auth.UserSessionManager;
import br.com.devjf.salessync.service.navigation.PanelNavigationService;
import br.com.devjf.salessync.service.navigation.PanelNavigationServiceImpl;
import br.com.devjf.salessync.service.permission.UserPermissionService;
import br.com.devjf.salessync.service.permission.UserPermissionServiceImpl;

public final class MainAppView extends javax.swing.JFrame {
    // Singleton instance
    private static MainAppView instance;
    
    // Panel keys
    public static final String DASHBOARD_PANEL = "Dashboard";
    public static final String SALES_PANEL = "Vendas";
    public static final String CUSTOMERS_PANEL = "Clientes";
    public static final String SERVICE_ORDERS_PANEL = "Ordens de Serviço";
    public static final String EXPENSES_PANEL = "Despesas";
    public static final String REPORTS_PANEL = "Relatórios";
    public static final String USERS_PANEL = "Usuários";
    public static final String SYSTEM_LOGS_PANEL = "Logs do Sistema";
    public static final String NEW_SALE_PANEL = "Cadastrar Venda";
    public static final String EDIT_SALE_PANEL = "Editar Venda";
    public static final String NEW_CUSTOMER_PANEL = "Cadastrar Cliente";
    public static final String EDIT_CUSTOMER_PANEL = "Editar Cliente";
    public static final String NEW_SO_PANEL = "Cadastrar Ordem";
    public static final String EDIT_SO_PANEL = "Editar Ordem";
    public static final String NEW_EXPENSE_PANEL = "Cadastrar Despesa";
    public static final String EDIT_EXPENSE_PANEL = "Editar Despesa";
    public static final String NEW_USER_PANEL = "Cadastrar Usuário";
    public static final String EDIT_USER_PANEL = "Editar Usuário";

    // Services and controllers
    private final PanelNavigationController navigationController;
    private final UserPermissionService permissionService;
    private final UserActivityService activityService;

    public MainAppView() {
        initComponents();
        
        // Set this instance as the singleton instance
        instance = this;
        
        // Initialize services
        UserController userController = new UserController();
        PanelNavigationService navigationService = new PanelNavigationServiceImpl(selectedPanel);
        PanelFactory panelFactory = new PanelFactory();
        
        this.navigationController = new PanelNavigationController(navigationService, panelFactory);
        this.permissionService = new UserPermissionServiceImpl();
        this.activityService = new UserActivityServiceImpl(userController);
        
        initializePermissionLabel();
        
        initPanels();
        setupListeners();
    }

    /**
     * Updates the title label to match the current panel
     *
     * @param panelKey The key of the current panel
     */
    public void updateTitle(String panelKey) {
        titleLbl.setText(panelKey);
    }
    
    /**
     * Get the singleton instance of MainAppView
     *
     * @return The MainAppView instance
     */
    public static MainAppView getInstance() {
        return instance;
    }

    private void initPanels() {
        // Initialize only the dashboard panel initially
        navigationController.navigateToPanel(DASHBOARD_PANEL);
    }

    private void setupListeners() {
        // Refresh title label when selected and navigate to the corresponding panel
        selectionList.addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                String selectedItem = selectionList.getSelectedValue();
                updateTitle(selectedItem);
                
                // Check if user has permission to access this panel
                User loggedUser = UserSessionManager.getInstance().getLoggedUser();
                if (permissionService.hasAccessToPanel(loggedUser, selectedItem)) {
                    // Register user activity
                    activityService.registerActivity(loggedUser, "Acessou " + selectedItem);
                    
                    // Navigate to the panel
                    navigationController.navigateToPanel(getPanelKeyFromName(selectedItem));
                } else {
                    JOptionPane.showMessageDialog(this, 
                            "Você não tem permissão para acessar este painel", 
                            "Acesso Negado", 
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    /**
     * Método unificado para navegar entre painéis no CardLayout com objeto
     * opcional
     *
     * @param panelKey Chave do painel para o qual navegar
     * @param object Objeto opcional (Sale, Customer, etc.)
     */
    public static void redirectToPanel(String panelKey, Object object) {
        MainAppView instance = MainAppView.getInstance();
        if (instance == null) {
            System.err.println("Erro: Instância do MainAppView não encontrada");
            return;
        }
        
        if (object != null) {
            instance.navigationController.navigateToPanel(panelKey, object);
        } else {
            instance.navigationController.navigateToPanel(panelKey);
        }
        
        // Update the title
        instance.updateTitle(panelKey);
    }

    private String getPanelKeyFromName(String panelName) {
        switch (panelName) {
            case "Dashboard":
                return DASHBOARD_PANEL;
            case "Vendas":
                return SALES_PANEL;
            case "Clientes":
                return CUSTOMERS_PANEL;
            case "Ordens de Serviço":
                return SERVICE_ORDERS_PANEL;
            case "Despesas":
                return EXPENSES_PANEL;
            case "Relatórios":
                return REPORTS_PANEL;
            case "Usuários":
                return USERS_PANEL;
            case "Logs do Sistema":
                return SYSTEM_LOGS_PANEL;
            default:
                return DASHBOARD_PANEL;
        }
    }

    /**
     * This method to initialize permition label should be called after all 
     * services are initialized
     */
    public void initializePermissionLabel() {
        if (permitionLabel != null && permissionService != null) {
            User loggedUser = UserSessionManager.getInstance().getLoggedUser();
            permitionLabel.setText(permissionService.getPermissionLabel(loggedUser));
        }
    }

    /**
     * Updates the permission label after user login
     */
    public void updatePermissionLabel() {
        if (permitionLabel != null && permissionService != null) {
            User loggedUser = UserSessionManager.getInstance().getLoggedUser();
            permitionLabel.setText(permissionService.getPermissionLabel(loggedUser));
        }
    }

    /**
     * Registra uma atividade do usuário logado
     *
     * @param description Descrição da atividade
     */
    public void registerUserActivity(String description) {
        User loggedUser = UserSessionManager.getInstance().getLoggedUser();
        activityService.registerActivity(loggedUser, description);
    }

    /**
     * Sobrecarga do método redirectToPanel para compatibilidade com código
     * existente
     *
     * @param panelKey Chave do painel para o qual navegar
     */
    public static void redirectToPanel(String panelKey) {
        redirectToPanel(panelKey,
                null);
    }

    /**
     * Updates the selection in the side menu list
     *
     * @param itemName The name of the item to select
     */
    public static void updateSelectionList(String itemName) {
        MainAppView instance = getInstance();
        if (instance == null) {
            return;
        }
        // Find the index of the item in the list model
        for (int i = 0; i < instance.selectionList.getModel().getSize(); i++) {
            if (itemName.equals(
                    instance.selectionList.getModel().getElementAt(i))) {
                instance.selectionList.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * Retorna a lista de seleção do menu lateral
     *
     * @return Lista de seleção do menu lateral
     */
    public javax.swing.JList<String> getSelectionList() {
        return this.selectionList;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sideMenu = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        sSyncLogo = new javax.swing.JLabel();
        selectionList = new javax.swing.JList<>();
        titlePanel = new javax.swing.JPanel();
        permitionLabel = new javax.swing.JLabel();
        permitionIcon = new javax.swing.JLabel();
        titleLbl = new javax.swing.JLabel();
        selectedPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        sideMenu.setBackground(new java.awt.Color(42, 48, 66));
        sideMenu.setPreferredSize(new java.awt.Dimension(193, 100));
        sideMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator1.setForeground(new java.awt.Color(58, 63, 85));
        jSeparator1.setVerifyInputWhenFocusTarget(false);
        sideMenu.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 57, 193, -1));

        sSyncLogo.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        sSyncLogo.setForeground(new java.awt.Color(255, 255, 255));
        sSyncLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/SalesSync-logo2-small.png"))); // NOI18N
        sSyncLogo.setText(" SalesSync");
        sideMenu.add(sSyncLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 8, -1, -1));

        selectionList.setBackground(new java.awt.Color(42, 48, 66));
        selectionList.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        selectionList.setForeground(new java.awt.Color(160, 174, 192));
        selectionList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Dashboard", "Vendas", "Clientes", "Ordens de Serviço", "Despesas", "Relatórios", "Usuários", "Logs do Sistema" };
            @Override
            public int getSize() { return strings.length; }
            @Override
            public String getElementAt(int i) { return strings[i]; }
        });
        selectionList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        selectionList.setAutoscrolls(false);
        selectionList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        selectionList.setSelectionBackground(new java.awt.Color(58, 63, 85));
        selectionList.setSelectionForeground(new java.awt.Color(255, 255, 255));
        selectionList.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        selectionList.setFixedCellHeight(40);
        selectionList.setCellRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 15, 8, 0));
                return this;
            }
        });
        selectionList.setSelectedIndex(0);
        sideMenu.add(selectionList, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 190, 320));

        titlePanel.setBackground(new java.awt.Color(255, 255, 255));
        titlePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        titlePanel.setPreferredSize(new java.awt.Dimension(1007, 60));
        titlePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        permitionLabel.setBackground(new java.awt.Color(51, 51, 51));
        permitionLabel.setFont(new java.awt.Font("Liberation Sans", 0, 10)); // NOI18N
        permitionLabel.setForeground(new java.awt.Color(51, 51, 51));
        permitionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        permitionLabel.setText("???");
        titlePanel.add(permitionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 20, 30, 20));

        permitionIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        permitionIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/EllipsePermission.png"))); // NOI18N
        permitionIcon.setToolTipText("Permissão");
        titlePanel.add(permitionIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(942, 7, -1, -1));

        titleLbl.setBackground(new java.awt.Color(51, 51, 51));
        titleLbl.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        titleLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLbl.setText(selectionList.getSelectedValue());
        titleLbl.setToolTipText("");
        titlePanel.add(titleLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, -1, 45));

        selectedPanel.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(sideMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 995, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sideMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(titlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 728, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel permitionIcon;
    private javax.swing.JLabel permitionLabel;
    private javax.swing.JLabel sSyncLogo;
    private javax.swing.JPanel selectedPanel;
    private javax.swing.JList<String> selectionList;
    private javax.swing.JPanel sideMenu;
    private javax.swing.JLabel titleLbl;
    private javax.swing.JPanel titlePanel;
    // End of variables declaration//GEN-END:variables
}
