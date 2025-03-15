package br.com.devjf.salessync.view.forms;

public class DashboardForm extends javax.swing.JFrame {
    public DashboardForm() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        welcomeLbl = new javax.swing.JLabel();
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        activitiesPnl = new javax.swing.JPanel();
        activitiesLbl = new javax.swing.JLabel();
        lastAccessLbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        welcomeLbl.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        welcomeLbl.setText("Bem vindo(a), Admin");

        sellPnl.setPreferredSize(new java.awt.Dimension(170, 125));

        sellsLbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        sellsLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sellsLbl.setText("Vendas");

        newSaleLink.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        newSaleLink.setForeground(new java.awt.Color(134, 157, 249));
        newSaleLink.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        newSaleLink.setText("Registrar nova venda");

        salesLink.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        salesLink.setForeground(new java.awt.Color(134, 157, 249));
        salesLink.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        salesLink.setText("Ver histórico");

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

        customersLbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        customersLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        customersLbl.setText("Clientes");

        newCustormerLink.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        newCustormerLink.setForeground(new java.awt.Color(134, 157, 249));
        newCustormerLink.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        newCustormerLink.setText("Novo Cliente");

        customersLink.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        customersLink.setForeground(new java.awt.Color(134, 157, 249));
        customersLink.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        customersLink.setText("Buscar Cliente");

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

        serviceOrdersLbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        serviceOrdersLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        serviceOrdersLbl.setText("Ordens de Serviço");

        newServiceOrderLink.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        newServiceOrderLink.setForeground(new java.awt.Color(134, 157, 249));
        newServiceOrderLink.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        newServiceOrderLink.setText("Nova O.S.");

        serviceOrdersLink.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        serviceOrdersLink.setForeground(new java.awt.Color(134, 157, 249));
        serviceOrdersLink.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        serviceOrdersLink.setText("Ver Pendentes");

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

        profileLbl.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        profileLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profileLbl.setText("Perfil");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Nome: ");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Permissão: ");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Status: ");

        javax.swing.GroupLayout userPnlLayout = new javax.swing.GroupLayout(userPnl);
        userPnl.setLayout(userPnlLayout);
        userPnlLayout.setHorizontalGroup(
            userPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPnlLayout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addGroup(userPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(profileLbl)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addContainerGap(107, Short.MAX_VALUE))
        );
        userPnlLayout.setVerticalGroup(
            userPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPnlLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(profileLbl)
                .addGap(60, 60, 60)
                .addComponent(jLabel1)
                .addGap(20, 20, 20)
                .addComponent(jLabel2)
                .addGap(20, 20, 20)
                .addComponent(jLabel3)
                .addContainerGap(146, Short.MAX_VALUE))
        );

        activitiesPnl.setPreferredSize(new java.awt.Dimension(570, 219));

        activitiesLbl.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        activitiesLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        activitiesLbl.setText("Suas Atividades Recentes");

        javax.swing.GroupLayout activitiesPnlLayout = new javax.swing.GroupLayout(activitiesPnl);
        activitiesPnl.setLayout(activitiesPnlLayout);
        activitiesPnlLayout.setHorizontalGroup(
            activitiesPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, activitiesPnlLayout.createSequentialGroup()
                .addContainerGap(141, Short.MAX_VALUE)
                .addComponent(activitiesLbl)
                .addGap(141, 141, 141))
        );
        activitiesPnlLayout.setVerticalGroup(
            activitiesPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(activitiesPnlLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(activitiesLbl)
                .addContainerGap(152, Short.MAX_VALUE))
        );

        lastAccessLbl.setText("Último acesso: ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(welcomeLbl)
                    .addComponent(lastAccessLbl)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(sellPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(customerPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(serviceOrderPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(activitiesPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addComponent(userPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(welcomeLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lastAccessLbl)
                .addGap(66, 66, 66)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sellPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(serviceOrderPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(customerPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(activitiesPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(userPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(148, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lastAccessLbl;
    private javax.swing.JLabel newCustormerLink;
    private javax.swing.JLabel newSaleLink;
    private javax.swing.JLabel newServiceOrderLink;
    private javax.swing.JLabel profileLbl;
    private javax.swing.JLabel salesLink;
    private javax.swing.JPanel sellPnl;
    private javax.swing.JLabel sellsLbl;
    private javax.swing.JPanel serviceOrderPnl;
    private javax.swing.JLabel serviceOrdersLbl;
    private javax.swing.JLabel serviceOrdersLink;
    private javax.swing.JPanel userPnl;
    private javax.swing.JLabel welcomeLbl;
    // End of variables declaration//GEN-END:variables
}
