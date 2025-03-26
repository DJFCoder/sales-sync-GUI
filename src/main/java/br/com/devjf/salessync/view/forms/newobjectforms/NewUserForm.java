package br.com.devjf.salessync.view.forms.newobjectforms;

import br.com.devjf.salessync.controller.UserController;
import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.model.UserType;
import br.com.devjf.salessync.util.ViewUtil;
import br.com.devjf.salessync.view.MainAppView;

public class NewUserForm extends javax.swing.JFrame {
    private User userToEdit;
    private UserController userController;
    private boolean isEditMode = false;

    public NewUserForm() {
        initComponents();
        this.userController = new UserController();
        setupUserTypeComboBox();
        // Set default status to active
        statusCheckBox.setSelected(true);
    }

    /**
     * Constructor for editing an existing user
     *
     * @param user The user to edit
     */
    public NewUserForm(User user) {
        initComponents();
        this.userToEdit = user;
        this.userController = new UserController();
        this.isEditMode = true;
        // Setup the form for editing
        setupUserTypeComboBox();
        loadUserData();
        // Change the title and button text
        titleField.setText("Editar Usuário");
        saveBtn.setText("Atualizar");
        // Password fields should be empty when editing
        passwordField.setText("");
        confirmPasswordField.setText("");
        // Add a note about password
        passwordLbl.setText("Senha (deixe em branco para manter a atual)");
    }

    /**
     * Setup the user type combo box with available user types
     */
    private void setupUserTypeComboBox() {
        userTypeCmb.removeAllItems();
        userTypeCmb.addItem("Selecione");
        // Add all user types
        for (UserType type : UserType.values()) {
            userTypeCmb.addItem(getUserTypeDisplayName(type));
        }
    }

    /**
     * Load user data into the form fields
     */
    private void loadUserData() {
        if (userToEdit != null) {
            // Set name and login
            nameField.setText(userToEdit.getName());
            loginField.setText(userToEdit.getLogin());
            // Set user type
            UserType userType = userToEdit.getType();
            for (int i = 0; i < userTypeCmb.getItemCount(); i++) {
                String item = userTypeCmb.getItemAt(i);
                if (item.equals(getUserTypeDisplayName(userType))) {
                    userTypeCmb.setSelectedIndex(i);
                    break;
                }
            }
            // Set active status
            statusCheckBox.setSelected(userToEdit.isActive());
        }
    }

    /**
     * Get display name for user type
     *
     * @param type The user type
     * @return The display name
     */
    private String getUserTypeDisplayName(UserType type) {
        switch (type) {
            case ADMIN:
                return "Administrador";
            case OWNER:
                return "Proprietário";
            case EMPLOYEE:
                return "Funcionário";
            default:
                return "";
        }
    }

    /**
     * Convert display name to UserType enum
     *
     * @param displayName The display name
     * @return The UserType enum
     */
    private UserType getUserTypeFromDisplayName(String displayName) {
        switch (displayName) {
            case "Administrador":
                return UserType.ADMIN;
            case "Proprietário":
                return UserType.OWNER;
            case "Funcionário":
                return UserType.EMPLOYEE;
            default:
                return null;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        userPnl = new javax.swing.JPanel();
        cancelBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        nameField = new javax.swing.JTextField();
        nameLbl = new javax.swing.JLabel();
        userTypeLbl = new javax.swing.JLabel();
        userTypeCmb = new javax.swing.JComboBox<>();
        inTitleLbl = new javax.swing.JLabel();
        loginLbl = new javax.swing.JLabel();
        loginField = new javax.swing.JTextField();
        passwordLbl = new javax.swing.JLabel();
        passwordField = new javax.swing.JTextField();
        confirmPasswordLbl = new javax.swing.JLabel();
        confirmPasswordField = new javax.swing.JTextField();
        statusCheckBox = new javax.swing.JCheckBox();
        statusLbl = new javax.swing.JLabel();
        titleField = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        userPnl.setPreferredSize(new java.awt.Dimension(813, 549));
        ViewUtil.standardCornerRadius(userPnl);

        cancelBtn.setBackground(new java.awt.Color(175, 76, 78));
        cancelBtn.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        cancelBtn.setForeground(new java.awt.Color(255, 255, 255));
        cancelBtn.setText("Cancelar");
        cancelBtn.setPreferredSize(new java.awt.Dimension(120, 40));
        ViewUtil.standardCornerRadius(cancelBtn);
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        saveBtn.setBackground(new java.awt.Color(76, 175, 80));
        saveBtn.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        saveBtn.setForeground(new java.awt.Color(255, 255, 255));
        saveBtn.setText("Salvar");
        saveBtn.setPreferredSize(new java.awt.Dimension(120, 40));
        ViewUtil.standardCornerRadius(saveBtn);
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        nameField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nameField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nameField.setPreferredSize(new java.awt.Dimension(376, 40));
        ViewUtil.standardCornerRadius(nameField);

        nameLbl.setText("Nome:");

        userTypeLbl.setText("Permissão:");

        userTypeCmb.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        userTypeCmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "ADMINISTRADOR", "DONO", "COLABORADOR" }));
        userTypeCmb.setPreferredSize(new java.awt.Dimension(178, 40));

        inTitleLbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        inTitleLbl.setText("Informações de Acesso");

        loginLbl.setText("Login:");

        loginField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        loginField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        loginField.setPreferredSize(new java.awt.Dimension(376, 40));
        ViewUtil.standardCornerRadius(loginField);

        passwordLbl.setText("Senha:");

        passwordField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        passwordField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        passwordField.setPreferredSize(new java.awt.Dimension(376, 40));
        ViewUtil.standardCornerRadius(passwordField);

        confirmPasswordLbl.setText("Confirmar Senha:");

        confirmPasswordField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        confirmPasswordField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        confirmPasswordField.setPreferredSize(new java.awt.Dimension(376, 40));
        ViewUtil.standardCornerRadius(confirmPasswordField);

        statusCheckBox.setText("Ativo");

        statusLbl.setText("Status:");

        javax.swing.GroupLayout userPnlLayout = new javax.swing.GroupLayout(userPnl);
        userPnl.setLayout(userPnlLayout);
        userPnlLayout.setHorizontalGroup(
            userPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPnlLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(userPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(userPnlLayout.createSequentialGroup()
                        .addGroup(userPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordLbl)
                            .addComponent(loginField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(loginLbl))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(userPnlLayout.createSequentialGroup()
                        .addComponent(inTitleLbl)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userPnlLayout.createSequentialGroup()
                        .addGroup(userPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(userPnlLayout.createSequentialGroup()
                                .addGroup(userPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(confirmPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(confirmPasswordLbl)
                                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(nameLbl))
                                .addGap(20, 20, 20)
                                .addGroup(userPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(userTypeCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(userTypeLbl))
                                .addGap(18, 18, 18)
                                .addGroup(userPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(statusLbl)
                                    .addComponent(statusCheckBox))
                                .addGap(0, 130, Short.MAX_VALUE))
                            .addGroup(userPnlLayout.createSequentialGroup()
                                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(20, 20, 20))))
        );
        userPnlLayout.setVerticalGroup(
            userPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userPnlLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(nameLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(inTitleLbl)
                .addGap(18, 18, 18)
                .addComponent(loginLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(loginField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(passwordLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(userPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(userPnlLayout.createSequentialGroup()
                        .addComponent(confirmPasswordLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(confirmPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(userPnlLayout.createSequentialGroup()
                        .addComponent(userTypeLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(userTypeCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(userPnlLayout.createSequentialGroup()
                        .addComponent(statusLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(statusCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addGroup(userPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        titleField.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        titleField.setText("Informações Pessoais");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(titleField)
                    .addComponent(userPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(71, 71, 71))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(titleField)
                .addGap(32, 32, 32)
                .addComponent(userPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
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

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        MainAppView.redirectToPanel(MainAppView.USERS_PANEL);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed

    }//GEN-LAST:event_saveBtnActionPerformed
    // new NewUserForm().setVisible(true);

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JTextField confirmPasswordField;
    private javax.swing.JLabel confirmPasswordLbl;
    private javax.swing.JLabel inTitleLbl;
    private javax.swing.JTextField loginField;
    private javax.swing.JLabel loginLbl;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLbl;
    private javax.swing.JTextField passwordField;
    private javax.swing.JLabel passwordLbl;
    private javax.swing.JButton saveBtn;
    private javax.swing.JCheckBox statusCheckBox;
    private javax.swing.JLabel statusLbl;
    private javax.swing.JLabel titleField;
    private javax.swing.JPanel userPnl;
    private javax.swing.JComboBox<String> userTypeCmb;
    private javax.swing.JLabel userTypeLbl;
    // End of variables declaration//GEN-END:variables
}
