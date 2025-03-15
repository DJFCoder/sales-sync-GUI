package br.com.devjf.salessync.view;

import javax.swing.JOptionPane;

public class Login extends javax.swing.JFrame {
    public Login() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        borderRight = new javax.swing.JPanel();
        borderLeft = new javax.swing.JPanel();
        borderBotton = new javax.swing.JPanel();
        borderTop = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();
        loginField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        loginBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SalesSync Login");
        setResizable(false);

        borderRight.setPreferredSize(new java.awt.Dimension(407, 10));

        javax.swing.GroupLayout borderRightLayout = new javax.swing.GroupLayout(borderRight);
        borderRight.setLayout(borderRightLayout);
        borderRightLayout.setHorizontalGroup(
            borderRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 407, Short.MAX_VALUE)
        );
        borderRightLayout.setVerticalGroup(
            borderRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
        );

        getContentPane().add(borderRight, java.awt.BorderLayout.LINE_END);

        borderLeft.setPreferredSize(new java.awt.Dimension(407, 10));

        javax.swing.GroupLayout borderLeftLayout = new javax.swing.GroupLayout(borderLeft);
        borderLeft.setLayout(borderLeftLayout);
        borderLeftLayout.setHorizontalGroup(
            borderLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 407, Short.MAX_VALUE)
        );
        borderLeftLayout.setVerticalGroup(
            borderLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
        );

        getContentPane().add(borderLeft, java.awt.BorderLayout.LINE_START);

        borderBotton.setPreferredSize(new java.awt.Dimension(10, 160));

        javax.swing.GroupLayout borderBottonLayout = new javax.swing.GroupLayout(borderBotton);
        borderBotton.setLayout(borderBottonLayout);
        borderBottonLayout.setHorizontalGroup(
            borderBottonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1200, Short.MAX_VALUE)
        );
        borderBottonLayout.setVerticalGroup(
            borderBottonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 160, Short.MAX_VALUE)
        );

        getContentPane().add(borderBotton, java.awt.BorderLayout.PAGE_END);

        borderTop.setPreferredSize(new java.awt.Dimension(10, 130));
        borderTop.setRequestFocusEnabled(false);

        javax.swing.GroupLayout borderTopLayout = new javax.swing.GroupLayout(borderTop);
        borderTop.setLayout(borderTopLayout);
        borderTopLayout.setHorizontalGroup(
            borderTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1200, Short.MAX_VALUE)
        );
        borderTopLayout.setVerticalGroup(
            borderTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
        );

        getContentPane().add(borderTop, java.awt.BorderLayout.PAGE_START);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));

        loginField.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        loginField.setToolTipText("");
        loginField.setActionCommand("<Not Set>");
        loginField.setMargin(new java.awt.Insets(2, 25, 2, 25));
        loginField.setMinimumSize(new java.awt.Dimension(310, 50));
        loginField.setName(""); // NOI18N
        loginField.setPreferredSize(new java.awt.Dimension(310, 50));
        loginField.putClientProperty("FlatLaf.style",
                "arc: 10");
        loginField.putClientProperty("JTextField.placeholderText", "Usuário");
        
        passwordField.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        passwordField.setToolTipText("");
        passwordField.setMargin(new java.awt.Insets(2, 25, 2, 25));
        passwordField.setPreferredSize(new java.awt.Dimension(310, 50));
        passwordField.putClientProperty("FlatLaf.style",
                "arc: 10; showRevealButton: true");
        passwordField.putClientProperty("JTextField.placeholderText", "Senha");

        loginBtn.setBackground(new java.awt.Color(33, 150, 243));
        loginBtn.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        loginBtn.setForeground(new java.awt.Color(255, 255, 255));
        loginBtn.setText("ENTRAR");
        loginBtn.setPreferredSize(new java.awt.Dimension(310, 50));
        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtnActionPerformed(evt);
            }
        });
        loginBtn.putClientProperty("FlatLaf.style",
                "arc: 10");

        jLabel1.setForeground(new java.awt.Color(134, 157, 249));
        jLabel1.setText("Esqueceu a senha?");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(51, 51, 51));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("SalesSync");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/SalesSync-logo1.png"))); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loginField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(38, 38, 38))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(loginField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(90, 90, 90))
        );
        mainPanel.putClientProperty("FlatLaf.style",
                "arc: 10");

        passwordField.getAccessibleContext().setAccessibleName("");

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBtnActionPerformed
        new MainAppView().setVisible(true);
        dispose();
    }//GEN-LAST:event_loginBtnActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        JOptionPane.showMessageDialog(null,
                "Contate o administrador:\ndjfcoder@outlook.com",
                "CONTATO",
                JOptionPane.INFORMATION_MESSAGE
        );
    }//GEN-LAST:event_jLabel1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel borderBotton;
    private javax.swing.JPanel borderLeft;
    private javax.swing.JPanel borderRight;
    private javax.swing.JPanel borderTop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton loginBtn;
    private javax.swing.JTextField loginField;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPasswordField passwordField;
    // End of variables declaration//GEN-END:variables
}
