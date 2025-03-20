package br.com.devjf.salessync.view.forms;

import br.com.devjf.salessync.util.ViewUtil;

public class DashboardForm extends javax.swing.JFrame {
    public DashboardForm() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        welcomeLbl = new javax.swing.JLabel();
        lastAccessLbl = new javax.swing.JLabel();
        sellPnl = new javax.swing.JPanel();
        sellsLbl = new javax.swing.JLabel();
        newSaleLink = new javax.swing.JLabel();
        salesLink = new javax.swing.JLabel();
        customerPnl = new javax.swing.JPanel();
        customersLbl = new javax.swing.JLabel();
        newCustormerLink = new javax.swing.JLabel();
        customersLink = new javax.swing.JLabel();
        serviceOrderPnl = new javax.swing.JPanel();
        serviceOrdersLbl = new javax.swing.JLabel();
        newServiceOrderLink = new javax.swing.JLabel();
        serviceOrdersLink = new javax.swing.JLabel();
        userPnl = new javax.swing.JPanel();
        profileLbl = new javax.swing.JLabel();
        nameLbl = new javax.swing.JLabel();
        permitionLbl = new javax.swing.JLabel();
        statusLbl = new javax.swing.JLabel();
        activitiesPnl = new javax.swing.JPanel();
        activitiesLbl = new javax.swing.JLabel();
        fstActivitieLbl = new javax.swing.JLabel();
        fstTimeActivitieLbl = new javax.swing.JLabel();
        scdActivitieLbl = new javax.swing.JLabel();
        scdTimeActivitieLbl = new javax.swing.JLabel();
        trdActivitieLbl = new javax.swing.JLabel();
        trdTimeActivitieLbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        welcomeLbl.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        welcomeLbl.setText("Bem vindo(a), Admin");

        lastAccessLbl.setText("Último acesso: ");

        sellPnl.setPreferredSize(new java.awt.Dimension(170, 125));
        ViewUtil.standardCornerRadius(sellPnl);

        sellsLbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        sellsLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sellsLbl.setText("Vendas");

        newSaleLink.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        newSaleLink.setForeground(new java.awt.Color(134, 157, 249));
        newSaleLink.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        newSaleLink.setText("Registrar nova venda");
        newSaleLink.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        salesLink.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        salesLink.setForeground(new java.awt.Color(134, 157, 249));
        salesLink.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        salesLink.setText("Ver histórico");
        salesLink.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout sellPnlLayout = new javax.swing.GroupLayout(sellPnl);
        sellPnl.setLayout(sellPnlLayout);
        sellPnlLayout.setHorizontalGroup(
            sellPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sellPnlLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(sellPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(newSaleLink)
                    .addComponent(salesLink))
                .addGap(26, 26, 26))
            .addGroup(sellPnlLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(sellsLbl)
                .addGap(61, 61, 61))
        );
        sellPnlLayout.setVerticalGroup(
            sellPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sellPnlLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(sellsLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(newSaleLink)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(salesLink)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        customerPnl.setPreferredSize(new java.awt.Dimension(170, 125));
        ViewUtil.standardCornerRadius(customerPnl);

        customersLbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        customersLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        customersLbl.setText("Clientes");

        newCustormerLink.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        newCustormerLink.setForeground(new java.awt.Color(134, 157, 249));
        newCustormerLink.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        newCustormerLink.setText("Novo Cliente");
        newCustormerLink.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        customersLink.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        customersLink.setForeground(new java.awt.Color(134, 157, 249));
        customersLink.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        customersLink.setText("Buscar Cliente");
        customersLink.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout customerPnlLayout = new javax.swing.GroupLayout(customerPnl);
        customerPnl.setLayout(customerPnlLayout);
        customerPnlLayout.setHorizontalGroup(
            customerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, customerPnlLayout.createSequentialGroup()
                .addContainerGap(46, Short.MAX_VALUE)
                .addGroup(customerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(customersLink)
                    .addComponent(newCustormerLink)
                    .addComponent(customersLbl))
                .addGap(45, 45, 45))
        );
        customerPnlLayout.setVerticalGroup(
            customerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerPnlLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(customersLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(newCustormerLink)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customersLink)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        serviceOrderPnl.setPreferredSize(new java.awt.Dimension(170, 125));
        ViewUtil.standardCornerRadius(serviceOrderPnl);

        serviceOrdersLbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        serviceOrdersLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        serviceOrdersLbl.setText("Ordens de Serviço");

        newServiceOrderLink.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        newServiceOrderLink.setForeground(new java.awt.Color(134, 157, 249));
        newServiceOrderLink.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        newServiceOrderLink.setText("Nova O.S.");
        newServiceOrderLink.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        serviceOrdersLink.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        serviceOrdersLink.setForeground(new java.awt.Color(134, 157, 249));
        serviceOrdersLink.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        serviceOrdersLink.setText("Ver Pendentes");
        serviceOrdersLink.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout serviceOrderPnlLayout = new javax.swing.GroupLayout(serviceOrderPnl);
        serviceOrderPnl.setLayout(serviceOrderPnlLayout);
        serviceOrderPnlLayout.setHorizontalGroup(
            serviceOrderPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceOrderPnlLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(serviceOrderPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(serviceOrdersLbl)
                    .addComponent(newServiceOrderLink)
                    .addComponent(serviceOrdersLink))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        serviceOrderPnlLayout.setVerticalGroup(
            serviceOrderPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceOrderPnlLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(serviceOrdersLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(newServiceOrderLink)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(serviceOrdersLink)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        userPnl.setPreferredSize(new java.awt.Dimension(291, 373));
        ViewUtil.standardCornerRadius(userPnl);

        profileLbl.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        profileLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profileLbl.setText("Perfil");

        nameLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nameLbl.setText("Nome: ");

        permitionLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        permitionLbl.setText("Permissão: ");

        statusLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        statusLbl.setText("Status: ");

        javax.swing.GroupLayout userPnlLayout = new javax.swing.GroupLayout(userPnl);
        userPnl.setLayout(userPnlLayout);
        userPnlLayout.setHorizontalGroup(
            userPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPnlLayout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addGroup(userPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(nameLbl)
                    .addComponent(profileLbl)
                    .addComponent(permitionLbl)
                    .addComponent(statusLbl))
                .addContainerGap(107, Short.MAX_VALUE))
        );
        userPnlLayout.setVerticalGroup(
            userPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPnlLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(profileLbl)
                .addGap(60, 60, 60)
                .addComponent(nameLbl)
                .addGap(20, 20, 20)
                .addComponent(permitionLbl)
                .addGap(20, 20, 20)
                .addComponent(statusLbl)
                .addContainerGap(146, Short.MAX_VALUE))
        );

        activitiesPnl.setPreferredSize(new java.awt.Dimension(570, 219));
        ViewUtil.standardCornerRadius(activitiesPnl);

        activitiesLbl.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        activitiesLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        activitiesLbl.setText("Suas Atividades Recentes");

        fstActivitieLbl.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        fstActivitieLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fstActivitieLbl.setText("Última Atividade");

        fstTimeActivitieLbl.setFont(new java.awt.Font("Liberation Sans", 0, 10)); // NOI18N
        fstTimeActivitieLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fstTimeActivitieLbl.setText("horário");

        scdActivitieLbl.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        scdActivitieLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        scdActivitieLbl.setText("Penúltima Atividade");

        scdTimeActivitieLbl.setFont(new java.awt.Font("Liberation Sans", 0, 10)); // NOI18N
        scdTimeActivitieLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        scdTimeActivitieLbl.setText("horário");

        trdActivitieLbl.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        trdActivitieLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        trdActivitieLbl.setText("Antepenultima Atividade");

        trdTimeActivitieLbl.setFont(new java.awt.Font("Liberation Sans", 0, 10)); // NOI18N
        trdTimeActivitieLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        trdTimeActivitieLbl.setText("horário");

        javax.swing.GroupLayout activitiesPnlLayout = new javax.swing.GroupLayout(activitiesPnl);
        activitiesPnl.setLayout(activitiesPnlLayout);
        activitiesPnlLayout.setHorizontalGroup(
            activitiesPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(activitiesPnlLayout.createSequentialGroup()
                .addContainerGap(134, Short.MAX_VALUE)
                .addGroup(activitiesPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(fstActivitieLbl)
                    .addComponent(activitiesLbl)
                    .addComponent(fstTimeActivitieLbl)
                    .addComponent(scdActivitieLbl)
                    .addComponent(scdTimeActivitieLbl)
                    .addComponent(trdActivitieLbl)
                    .addComponent(trdTimeActivitieLbl))
                .addGap(0, 148, Short.MAX_VALUE))
        );
        activitiesPnlLayout.setVerticalGroup(
            activitiesPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, activitiesPnlLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(activitiesLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fstActivitieLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fstTimeActivitieLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scdActivitieLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scdTimeActivitieLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(trdActivitieLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(trdTimeActivitieLbl)
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(welcomeLbl)
                    .addComponent(lastAccessLbl)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(sellPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(customerPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(serviceOrderPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(activitiesPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                        .addComponent(userPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(welcomeLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lastAccessLbl)
                .addGap(66, 66, 66)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sellPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(serviceOrderPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(customerPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(activitiesPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(userPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(150, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // new DashboardForm().setVisible(true);

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel activitiesLbl;
    private javax.swing.JPanel activitiesPnl;
    private javax.swing.JPanel customerPnl;
    private javax.swing.JLabel customersLbl;
    private javax.swing.JLabel customersLink;
    private javax.swing.JLabel fstActivitieLbl;
    private javax.swing.JLabel fstTimeActivitieLbl;
    private javax.swing.JLabel lastAccessLbl;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel nameLbl;
    private javax.swing.JLabel newCustormerLink;
    private javax.swing.JLabel newSaleLink;
    private javax.swing.JLabel newServiceOrderLink;
    private javax.swing.JLabel permitionLbl;
    private javax.swing.JLabel profileLbl;
    private javax.swing.JLabel salesLink;
    private javax.swing.JLabel scdActivitieLbl;
    private javax.swing.JLabel scdTimeActivitieLbl;
    private javax.swing.JPanel sellPnl;
    private javax.swing.JLabel sellsLbl;
    private javax.swing.JPanel serviceOrderPnl;
    private javax.swing.JLabel serviceOrdersLbl;
    private javax.swing.JLabel serviceOrdersLink;
    private javax.swing.JLabel statusLbl;
    private javax.swing.JLabel trdActivitieLbl;
    private javax.swing.JLabel trdTimeActivitieLbl;
    private javax.swing.JPanel userPnl;
    private javax.swing.JLabel welcomeLbl;
    // End of variables declaration//GEN-END:variables
}
