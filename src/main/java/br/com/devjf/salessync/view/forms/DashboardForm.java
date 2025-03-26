package br.com.devjf.salessync.view.forms;

import br.com.devjf.salessync.controller.UserController;
import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.model.UserActivity;
import br.com.devjf.salessync.util.UserSession;
import br.com.devjf.salessync.util.ViewUtil;
import br.com.devjf.salessync.view.Login;
import br.com.devjf.salessync.view.MainAppView;
import java.util.List;

public class DashboardForm extends javax.swing.JFrame {
    private final UserController userController;
    private User loggedUser;

    public DashboardForm() {
        initComponents();
        userController = new UserController();
        loggedUser = UserSession.getInstance().getLoggedUser();
        initUserData();
    }

    /**
     * Inicializa os dados do usuário no dashboard Preenche as informações do
     * usuário logado e suas atividades recentes
     */
    private void initUserData() {
        if (loggedUser == null) {
            return;
        }
        updateUserInfo();
    }

    /**
     * Atualiza as informações do usuário e suas atividades no dashboard Este
     * método pode ser chamado a qualquer momento para atualizar os dados
     * exibidos
     */
    public void updateUserInfo() {
        if (loggedUser == null) {
            return;
        }
        // Atualiza o nome de boas-vindas
        welcomeLbl.setText("Bem vindo(a), " + loggedUser.getName());
        // Atualiza o último acesso
        lastAccessLbl.setText(
                "Último acesso: " + userController.getLastAccessFormatted(
                        loggedUser));
        // Atualiza as informações do perfil do usuário
        nameLbl.setText("Nome: " + loggedUser.getName());
        permitionLbl.setText(
                "Permissão: " + userController.getUserTypeInPortuguese(
                        loggedUser.getType()));
        statusLbl.setText(
                "Status: " + (loggedUser.isActive() ? "Ativo" : "Inativo"));
        // Busca as atividades recentes do usuário (últimas 3)
        List<UserActivity> recentActivities = userController.getRecentActivities(
                loggedUser,
                3);
        // Atualiza as labels de atividades
        if (!recentActivities.isEmpty()) {
            // Primeira atividade (mais recente)
            if (recentActivities.size() >= 1) {
                UserActivity firstActivity = recentActivities.get(0);
                fstActivitieLbl.setText(firstActivity.getDescription());
                fstTimeActivitieLbl.setText(
                        firstActivity.getFormattedTimeShort());
            }
            // Segunda atividade
            if (recentActivities.size() >= 2) {
                UserActivity secondActivity = recentActivities.get(1);
                scdActivitieLbl.setText(secondActivity.getDescription());
                scdTimeActivitieLbl.setText(
                        secondActivity.getFormattedTimeShort());
            } else {
                scdActivitieLbl.setText("Nenhuma atividade");
                scdTimeActivitieLbl.setText("");
            }
            // Terceira atividade
            if (recentActivities.size() >= 3) {
                UserActivity thirdActivity = recentActivities.get(2);
                trdActivitieLbl.setText(thirdActivity.getDescription());
                trdTimeActivitieLbl.setText(
                        thirdActivity.getFormattedTimeShort());
            } else {
                trdActivitieLbl.setText("Nenhuma atividade");
                trdTimeActivitieLbl.setText("");
            }
        } else {
            // Caso não haja atividades
            fstActivitieLbl.setText("Nenhuma atividade registrada");
            fstTimeActivitieLbl.setText("");
            scdActivitieLbl.setText("");
            scdTimeActivitieLbl.setText("");
            trdActivitieLbl.setText("");
            trdTimeActivitieLbl.setText("");
        }
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
        logoffBtn = new javax.swing.JButton();

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
        newSaleLink.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        newSaleLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newSaleLinkMouseClicked(evt);
            }
        });

        salesLink.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        salesLink.setForeground(new java.awt.Color(134, 157, 249));
        salesLink.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        salesLink.setText("Ver histórico");
        salesLink.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        salesLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                salesLinkMouseClicked(evt);
            }
        });

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
                .addContainerGap(29, Short.MAX_VALUE))
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
        newCustormerLink.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        newCustormerLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newCustormerLinkMouseClicked(evt);
            }
        });

        customersLink.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        customersLink.setForeground(new java.awt.Color(134, 157, 249));
        customersLink.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        customersLink.setText("Buscar Cliente");
        customersLink.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        customersLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customersLinkMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout customerPnlLayout = new javax.swing.GroupLayout(customerPnl);
        customerPnl.setLayout(customerPnlLayout);
        customerPnlLayout.setHorizontalGroup(
            customerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, customerPnlLayout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
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
                .addContainerGap(29, Short.MAX_VALUE))
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
        newServiceOrderLink.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        newServiceOrderLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newServiceOrderLinkMouseClicked(evt);
            }
        });

        serviceOrdersLink.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        serviceOrdersLink.setForeground(new java.awt.Color(134, 157, 249));
        serviceOrdersLink.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        serviceOrdersLink.setText("Ver Pendentes");
        serviceOrdersLink.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        serviceOrdersLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                serviceOrdersLinkMouseClicked(evt);
            }
        });

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
                .addContainerGap(21, Short.MAX_VALUE))
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
                .addContainerGap(29, Short.MAX_VALUE))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userPnlLayout.createSequentialGroup()
                .addContainerGap(107, Short.MAX_VALUE)
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
                .addGap(60, 60, 60)
                .addComponent(profileLbl)
                .addGap(60, 60, 60)
                .addComponent(nameLbl)
                .addGap(20, 20, 20)
                .addComponent(permitionLbl)
                .addGap(20, 20, 20)
                .addComponent(statusLbl)
                .addContainerGap(120, Short.MAX_VALUE))
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
                .addContainerGap(127, Short.MAX_VALUE)
                .addGroup(activitiesPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(fstActivitieLbl)
                    .addComponent(activitiesLbl)
                    .addComponent(fstTimeActivitieLbl)
                    .addComponent(scdActivitieLbl)
                    .addComponent(scdTimeActivitieLbl)
                    .addComponent(trdActivitieLbl)
                    .addComponent(trdTimeActivitieLbl))
                .addGap(0, 141, Short.MAX_VALUE))
        );
        activitiesPnlLayout.setVerticalGroup(
            activitiesPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, activitiesPnlLayout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
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

        logoffBtn.setBackground(new java.awt.Color(175, 76, 78));
        logoffBtn.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        logoffBtn.setForeground(new java.awt.Color(255, 255, 255));
        logoffBtn.setText("Sair");
        logoffBtn.setPreferredSize(new java.awt.Dimension(85, 40));
        ViewUtil.standardCornerRadius(logoffBtn);
        logoffBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoffBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(logoffBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addComponent(userPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                .addComponent(logoffBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
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


    private void logoffBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoffBtnActionPerformed
        // Obter o usuário logado
        User loggedUser = UserSession.getInstance().getLoggedUser();
        // Registrar a atividade de logout
        if (loggedUser != null) {
            UserController userController = new UserController();
            userController.registerActivity(loggedUser,
                    "Logout do sistema");
        }
        // Limpar a sessão do usuário
        UserSession.getInstance().clearSession();
        // Fechar a janela principal
        MainAppView.getInstance().dispose();
        // Abrir a tela de login
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }//GEN-LAST:event_logoffBtnActionPerformed

    private void newSaleLinkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newSaleLinkMouseClicked
        MainAppView.updateSelectionList("Vendas");
        MainAppView.redirectToPanel(MainAppView.NEW_SALE_PANEL);
    }//GEN-LAST:event_newSaleLinkMouseClicked

    private void salesLinkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salesLinkMouseClicked
        MainAppView.updateSelectionList("Vendas");
        MainAppView.redirectToPanel(MainAppView.SALES_PANEL);
    }//GEN-LAST:event_salesLinkMouseClicked

    private void newCustormerLinkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newCustormerLinkMouseClicked
        MainAppView.updateSelectionList("Clientes");
        MainAppView.redirectToPanel(MainAppView.NEW_CUSTOMER_PANEL);
    }//GEN-LAST:event_newCustormerLinkMouseClicked

    private void customersLinkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customersLinkMouseClicked
        MainAppView.updateSelectionList("Clientes");
        MainAppView.redirectToPanel(MainAppView.CUSTOMERS_PANEL);
    }//GEN-LAST:event_customersLinkMouseClicked

    private void newServiceOrderLinkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newServiceOrderLinkMouseClicked
        MainAppView.updateSelectionList("Ordens de Serviço");
        MainAppView.redirectToPanel(MainAppView.NEW_SO_PANEL);
    }//GEN-LAST:event_newServiceOrderLinkMouseClicked

    private void serviceOrdersLinkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_serviceOrdersLinkMouseClicked
        MainAppView.updateSelectionList("Ordens de Serviço");
        MainAppView.redirectToPanel(MainAppView.SERVICE_ORDERS_PANEL);
    }//GEN-LAST:event_serviceOrdersLinkMouseClicked
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
    private javax.swing.JButton logoffBtn;
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
