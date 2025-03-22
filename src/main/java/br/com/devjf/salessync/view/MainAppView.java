package br.com.devjf.salessync.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.model.UserType;
import br.com.devjf.salessync.util.UserSession;
import br.com.devjf.salessync.view.forms.CustomersForm;
import br.com.devjf.salessync.view.forms.DashboardForm;
import br.com.devjf.salessync.view.forms.ExpensesForm;
import br.com.devjf.salessync.view.forms.LogsForm;
import br.com.devjf.salessync.view.forms.ReportsForm;
import br.com.devjf.salessync.view.forms.SalesForm;
import br.com.devjf.salessync.view.forms.ServiceOrdersForm;
import br.com.devjf.salessync.view.forms.UsersForm;
import br.com.devjf.salessync.view.forms.newobjectforms.NewCustomerForm;
import br.com.devjf.salessync.view.forms.newobjectforms.NewExpenseForm;
import br.com.devjf.salessync.view.forms.newobjectforms.NewSaleForm;
import br.com.devjf.salessync.view.forms.newobjectforms.NewServiceOrderForm;
import br.com.devjf.salessync.view.forms.newobjectforms.NewUserForm;

public class MainAppView extends javax.swing.JFrame {
    // Singleton instance
    private static MainAppView instance;
    private static final String DASHBOARD_PANEL = "Dashboard";
    public static final String SALES_PANEL = "Vendas";
    public static final String CUSTOMERS_PANEL = "Clientes";
    public static final String SERVICE_ORDERS_PANEL = "Ordens de Serviço";
    private static final String EXPENSES_PANEL = "Despesas";
    private static final String REPORTS_PANEL = "Relatórios";
    private static final String USERS_PANEL = "Usuários";
    private static final String SYSTEM_LOGS_PANEL = "Logs do Sistema";
    public static final String NEW_SALE_PANEL = "Cadastrar Venda";
    public static final String NEW_CUSTOMER_PANEL = "Cadastrar Cliente";
    public static final String NEW_SO_PANEL = "Cadastrar Ordem";
    public static final String NEW_EXPENSE_PANEL = "Cadastrar Despesa";
    public static final String NEW_USER_PANEL = "Cadastrar Usuário";

    public MainAppView() {
        initComponents();
        // Set this instance as the singleton instance
        instance = this;
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
    // Adicione esta variável de instância para implementar lazy loading
    private final Map<String, JPanel> loadedPanels = new HashMap<>();

    private void initPanels() {
        CardLayout cardLayout = (CardLayout) selectedPanel.getLayout();
        // Inicializa apenas o painel inicial (Dashboard) para melhorar o tempo de inicialização
        try {
            // Carrega apenas o Dashboard inicialmente
            JPanel dashboardPanel = createPanelFromForm(new DashboardForm());
            selectedPanel.add(dashboardPanel,
                    DASHBOARD_PANEL);
            loadedPanels.put(DASHBOARD_PANEL,
                    dashboardPanel);
            // Mostra o painel inicial
            cardLayout.show(selectedPanel,
                    DASHBOARD_PANEL);
        } catch (Exception e) {
            System.err.println(
                    "Erro ao inicializar painel inicial: " + e.getMessage());
        }
    }

    private void navigateToPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) selectedPanel.getLayout();
        String panelKey = getPanelKeyFromName(panelName);
        // Verifica se o painel já foi carregado
        if (!loadedPanels.containsKey(panelKey)) {
            try {
                // Carrega o painel sob demanda
                JPanel newPanel = createPanelForKey(panelKey);
                if (newPanel != null) {
                    selectedPanel.add(newPanel,
                            panelKey);
                    loadedPanels.put(panelKey,
                            newPanel);
                    // Força o layout a ser calculado imediatamente
                    selectedPanel.validate();
                }
            } catch (Exception e) {
                System.err.println(
                        "Erro ao carregar painel " + panelName + ": " + e.getMessage());
                return;
            }
        }
        // Exibe o painel
        cardLayout.show(selectedPanel,
                panelKey);
        // Força a revalidação do layout após a exibição
        SwingUtilities.invokeLater(() -> {
            selectedPanel.revalidate();
            selectedPanel.repaint();
        });
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

    private JPanel createPanelFromForm(javax.swing.JFrame form) {
        // Configura o formulário para não fechar a aplicação quando fechado
        form.setDefaultCloseOperation(
                javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        // Força o layout a ser calculado antes de adicionar ao painel
        form.pack();
        // Obtém o contentPane do formulário
        Container contentPane = form.getContentPane();
        // Remove o contentPane do formulário para poder adicioná-lo ao nosso painel
        form.setContentPane(new javax.swing.JPanel());
        // Cria um novo painel com as dimensões corretas
        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.BorderLayout());
        panel.setPreferredSize(new java.awt.Dimension(995,
                728));
        // Adiciona o conteúdo do formulário ao painel
        panel.add(contentPane,
                BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPanelForKey(String panelKey) {
        try {
            switch (panelKey) {
                case DASHBOARD_PANEL:
                    return createPanelFromForm(new DashboardForm());
                case SALES_PANEL:
                    return createPanelFromForm(new SalesForm());
                case CUSTOMERS_PANEL:
                    return createPanelFromForm(new CustomersForm());
                case SERVICE_ORDERS_PANEL:
                    return createPanelFromForm(new ServiceOrdersForm());
                case EXPENSES_PANEL:
                    return createPanelFromForm(new ExpensesForm());
                case REPORTS_PANEL:
                    return createPanelFromForm(new ReportsForm());
                case USERS_PANEL:
                    return createPanelFromForm(new UsersForm());
                case SYSTEM_LOGS_PANEL:
                    return createPanelFromForm(new LogsForm());
                // New object forms
                case NEW_SALE_PANEL:
                    return createPanelFromForm(new NewSaleForm());
                case NEW_CUSTOMER_PANEL:
                    return createPanelFromForm(new NewCustomerForm());
                case NEW_SO_PANEL:
                    return createPanelFromForm(new NewServiceOrderForm());
                case NEW_EXPENSE_PANEL:
                    return createPanelFromForm(new NewExpenseForm());
                case NEW_USER_PANEL:
                    return createPanelFromForm(new NewUserForm());
                default:
                    return null;
            }
        } catch (Exception e) {
            System.err.println(
                    "Erro ao criar painel " + panelKey + ": " + e.getMessage());
            return null;
        }
    }

    private void setupListeners() {
        // Refresh title label when selected and navigate to the corresponding panel
        selectionList.addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                String selectedItem = selectionList.getSelectedValue();
                titleLbl.setText(selectedItem);
                // Navega para o painel correspondente
                navigateToPanel(selectedItem);
            }
        });
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
        selectionList.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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

        permitionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        permitionLabel.setText(getPermitionLabel());
        titlePanel.add(permitionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 20, 30, -1));

        permitionIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        permitionIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/EllipsePermission.png"))); // NOI18N
        permitionIcon.setToolTipText("Permissão");
        titlePanel.add(permitionIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(942, 7, -1, -1));

        titleLbl.setBackground(new java.awt.Color(51, 51, 51));
        titleLbl.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        titleLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLbl.setText(selectionList.getSelectedValue());
        titleLbl.setToolTipText("");
        // Definir uma largura fixa para o label e centralizá-lo
        titlePanel.add(titleLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1007, 45));

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

    private String getPermitionLabel() {
        User loggedUser = UserSession.getInstance().getLoggedUser();
        if (loggedUser == null) {
            return "???";
        }
        UserType userType = loggedUser.getType();
        switch (userType) {
            case ADMIN:
                return "ADM";
            case OWNER:
                return "OWN";
            case EMPLOYEE:
                return "EMPL";
            default:
                return "???";
        }
    }

    /**
     * Método unificado para navegar entre painéis no CardLayout
     *
     * @param panelKey Chave do painel para o qual navegar
     */
    public static void redirectToPanel(String panelKey) {
        MainAppView instance = MainAppView.getInstance();
        if (instance == null) {
            System.err.println("Erro: Instância do MainAppView não encontrada");
            return;
        }
        CardLayout cardLayout = (CardLayout) instance.selectedPanel.getLayout();
        // Verifica se o painel já está carregado
        if (instance.loadedPanels.containsKey(panelKey)) {
            // Se estiver, apenas mostra o painel
            cardLayout.show(instance.selectedPanel,
                    panelKey);
        } else {
            // Se não estiver, tenta criar o painel
            try {
                JPanel panel = instance.createPanelForKey(panelKey);
                if (panel != null) {
                    instance.selectedPanel.add(panel,
                            panelKey);
                    instance.loadedPanels.put(panelKey,
                            panel);
                    cardLayout.show(instance.selectedPanel,
                            panelKey);
                }
            } catch (Exception e) {
                System.err.println(
                        "Erro ao navegar para o painel: " + e.getMessage());
            }
        }
        // Atualiza a interface
        instance.selectedPanel.revalidate();
        instance.selectedPanel.repaint();
        // Atualiza o título
        instance.updateTitle(panelKey);
    }

    /**
     * Add a panel to the CardLayout
     *
     * @param panel The panel to add
     * @param name The name to identify the panel
     */
    public void addPanelToCardLayout(java.awt.Container panel, String name) {
        // Convert Container to JPanel if needed
        javax.swing.JPanel jpanel;
        if (panel instanceof javax.swing.JPanel) {
            jpanel = (JPanel) panel;
        } else {
            // Create a new JPanel and add the container's components
            jpanel = new javax.swing.JPanel();
            jpanel.setLayout(new java.awt.BorderLayout());
            // Add all components from the container
            Component[] components = panel.getComponents();
            for (Component component : components) {
                jpanel.add(component);
            }
        }
        // Add the panel to the CardLayout
        selectedPanel.add(jpanel,
                name);
        // Store the panel in the loadedPanels map
        loadedPanels.put(name,
                jpanel);
    }

    /**
     * Show a panel in the CardLayout
     *
     * @param name The name of the panel to show
     */
    public void showPanel(String name) {
        java.awt.CardLayout cardLayout = (java.awt.CardLayout) selectedPanel.getLayout();
        cardLayout.show(selectedPanel,
                name);
        // Update the UI
        selectedPanel.revalidate();
        selectedPanel.repaint();
    }

    /**
     * Updates the selection in the side menu list
     * 
     * @param itemName The name of the item to select
     */
    public static void updateSelectionList(String itemName) {
        MainAppView instance = getInstance();
        if (instance == null) return;
        
        // Find the index of the item in the list model
        for (int i = 0; i < instance.selectionList.getModel().getSize(); i++) {
            if (itemName.equals(instance.selectionList.getModel().getElementAt(i))) {
                instance.selectionList.setSelectedIndex(i);
                break;
            }
        }
    }

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