package br.com.devjf.salessync.view;

import javax.swing.event.ListSelectionEvent;

import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.model.UserType;
import br.com.devjf.salessync.util.UserSession;

public class MainAppView extends javax.swing.JFrame {
    public MainAppView() {
        initComponents();
        setupListeners();
    }

    private void setupListeners() {
        // Refresh title label when selected
        jList1.addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                titleLbl.setText(jList1.getSelectedValue());
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sideMenu = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jList1 = new javax.swing.JList<>();
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

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/SalesSync-logo2-small.png"))); // NOI18N
        jLabel1.setText(" SalesSync");
        sideMenu.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 8, -1, -1));

        jList1.setBackground(new java.awt.Color(42, 48, 66));
        jList1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jList1.setForeground(new java.awt.Color(160, 174, 192));
        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Dashboard", "Vendas", "Clientes", "Ordens de Serviço", "Despesas", "Relatórios", "Usuários", "Logs do Sistema" };
            @Override
            public int getSize() { return strings.length; }
            @Override
            public String getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setAutoscrolls(false);
        jList1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jList1.setSelectionBackground(new java.awt.Color(58, 63, 85));
        jList1.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jList1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        jList1.setFixedCellHeight(40);
        jList1.setCellRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 15, 8, 0));
                return this;
            }
        });
        jList1.setSelectedIndex(0);
        sideMenu.add(jList1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 190, 320));

        titlePanel.setBackground(new java.awt.Color(255, 255, 255));
        titlePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        titlePanel.setPreferredSize(new java.awt.Dimension(1007, 60));
        titlePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.JLayeredPane permissionLayeredPane = new javax.swing.JLayeredPane();
        permissionLayeredPane.setBounds(942, 7, 45, 45);
        
        permitionIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        permitionIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/EllipsePermission.png"))); // NOI18N
        permitionIcon.setToolTipText("Permissão");
        permitionIcon.setBounds(0, 0, 45, 45);
        permissionLayeredPane.add(permitionIcon, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        permitionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        permitionLabel.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        permitionLabel.setText(getPermitionLabel());
        permitionLabel.setFont(new java.awt.Font("Segoe UI", 1, 11));
        permitionLabel.setForeground(new java.awt.Color(60, 63, 65));
        permitionLabel.setBounds(0, 0, 45, 45);
        permissionLayeredPane.add(permitionLabel, javax.swing.JLayeredPane.PALETTE_LAYER);
        
        titlePanel.add(permissionLayeredPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(942, 7, 45, 45));

        titleLbl.setBackground(new java.awt.Color(51, 51, 51));
        titleLbl.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        titleLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLbl.setText(jList1.getSelectedValue());
        titleLbl.setToolTipText("");
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel permitionIcon;
    private javax.swing.JLabel permitionLabel;
    private javax.swing.JPanel selectedPanel;
    private javax.swing.JPanel sideMenu;
    private javax.swing.JLabel titleLbl;
    private javax.swing.JPanel titlePanel;
    // End of variables declaration//GEN-END:variables
}
