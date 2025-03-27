package br.com.devjf.salessync.view.forms.newobjectforms;

import br.com.devjf.salessync.controller.CustomerController;
import br.com.devjf.salessync.model.Customer;
import br.com.devjf.salessync.view.components.style.ViewComponentStyle;
import br.com.devjf.salessync.view.MainAppView;

public class NewCustomerForm extends javax.swing.JFrame {
    private Customer customer;
    private boolean isEditMode = false;
    private final CustomerController customerController;

    public NewCustomerForm() {
        initComponents();
        this.customerController = new CustomerController();
        this.isEditMode = false;
        this.titleField.setText("Cadastrar Cliente");
    }

    /**
     * Construtor para edição de cliente existente
     *
     * @param customer O cliente a ser editado
     */
    public NewCustomerForm(Customer customer) {
        initComponents();
        this.customerController = new CustomerController();
        // Se o cliente for fornecido diretamente, usá-lo
        if (customer != null) {
            this.customer = customer;
        } else {
            this.customer = null;
        }
        this.isEditMode = (this.customer != null);
        this.titleField.setText(
                isEditMode ? "Editar Cliente" : "Cadastrar Cliente");
        // Preencher os campos com os dados do cliente
        if (this.customer != null) {
            nameField.setText(this.customer.getName());
            taxIdField.setText(this.customer.getTaxId());
            emailField.setText(this.customer.getEmail());
            phoneField.setText(this.customer.getPhone());
            adressField.setText(this.customer.getAddress());
            observationField.setText(this.customer.getNotes());
        }
    }

    /**
     * Construtor para edição de cliente existente usando o ID
     *
     * @param customerId O ID do cliente a ser editado
     */
    public NewCustomerForm(Integer customerId) {
        initComponents();
        this.customerController = new CustomerController();
        // Carregar o cliente do banco de dados usando o controller
        if (customerId != null) {
            this.customer = customerController.findCustomerById(customerId);
        } else {
            this.customer = null;
        }
        this.isEditMode = (this.customer != null);
        this.titleField.setText(
                isEditMode ? "Editar Cliente" : "Cadastrar Cliente");
        // Preencher os campos com os dados do cliente
        if (this.customer != null) {
            nameField.setText(this.customer.getName());
            taxIdField.setText(this.customer.getTaxId());
            emailField.setText(this.customer.getEmail());
            phoneField.setText(this.customer.getPhone());
            adressField.setText(this.customer.getAddress());
            observationField.setText(this.customer.getNotes());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        customerPnl = new javax.swing.JPanel();
        cancelBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        nameField = new javax.swing.JTextField();
        nameLbl = new javax.swing.JLabel();
        taxIdField = new javax.swing.JTextField();
        taxIdLbl = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        emailLbl = new javax.swing.JLabel();
        phoneLbl = new javax.swing.JLabel();
        phoneField = new javax.swing.JTextField();
        adressLbl = new javax.swing.JLabel();
        adressField = new javax.swing.JTextField();
        observationLbl = new javax.swing.JLabel();
        scrollPane = new javax.swing.JScrollPane();
        observationField = new javax.swing.JTextArea();
        titleField = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        customerPnl.setPreferredSize(new java.awt.Dimension(813, 624));
        ViewComponentStyle.standardCornerRadius(customerPnl);

        cancelBtn.setBackground(new java.awt.Color(175, 76, 78));
        cancelBtn.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        cancelBtn.setForeground(new java.awt.Color(255, 255, 255));
        cancelBtn.setText("Cancelar");
        cancelBtn.setPreferredSize(new java.awt.Dimension(120, 40));
        ViewComponentStyle.standardCornerRadius(cancelBtn);
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
        ViewComponentStyle.standardCornerRadius(saveBtn);
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        nameField.setPreferredSize(new java.awt.Dimension(475, 40));
        ViewComponentStyle.standardCornerRadius(nameField);

        nameLbl.setText("Nome:");

        taxIdField.setPreferredSize(new java.awt.Dimension(278, 40));
        ViewComponentStyle.standardCornerRadius(taxIdField);

        taxIdLbl.setText("CPF/CNPJ:");

        emailField.setPreferredSize(new java.awt.Dimension(475, 40));
        ViewComponentStyle.standardCornerRadius(emailField);

        emailLbl.setText("E-mail:");

        phoneLbl.setText("Telefone:");

        phoneField.setPreferredSize(new java.awt.Dimension(278, 40));
        ViewComponentStyle.standardCornerRadius(phoneField);

        adressLbl.setText("Endereço:");

        adressField.setPreferredSize(new java.awt.Dimension(475, 40));
        ViewComponentStyle.standardCornerRadius(adressField);

        observationLbl.setText("Observação:");

        observationField.setRows(20);
        observationField.setTabSize(2);
        scrollPane.setViewportView(observationField);

        javax.swing.GroupLayout customerPnlLayout = new javax.swing.GroupLayout(customerPnl);
        customerPnl.setLayout(customerPnlLayout);
        customerPnlLayout.setHorizontalGroup(
            customerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, customerPnlLayout.createSequentialGroup()
                .addGroup(customerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(customerPnlLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, customerPnlLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(customerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(adressField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(customerPnlLayout.createSequentialGroup()
                                .addGroup(customerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(emailLbl)
                                    .addComponent(adressLbl))
                                .addGap(18, 18, 18)
                                .addGroup(customerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(customerPnlLayout.createSequentialGroup()
                                        .addComponent(phoneLbl)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(phoneField, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)))
                            .addGroup(customerPnlLayout.createSequentialGroup()
                                .addGroup(customerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(nameLbl))
                                .addGap(18, 18, 18)
                                .addGroup(customerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(customerPnlLayout.createSequentialGroup()
                                        .addComponent(taxIdLbl)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(taxIdField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(customerPnlLayout.createSequentialGroup()
                                .addComponent(observationLbl)
                                .addGap(401, 708, Short.MAX_VALUE)))))
                .addGap(20, 20, 20))
        );
        customerPnlLayout.setVerticalGroup(
            customerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, customerPnlLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(customerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLbl)
                    .addComponent(taxIdLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(customerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(taxIdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(customerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailLbl)
                    .addComponent(phoneLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(customerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(adressLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(adressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(observationLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(customerPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        titleField.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        titleField.setText("Informações do Cliente");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(titleField)
                    .addComponent(customerPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(customerPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
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

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed

    }//GEN-LAST:event_saveBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        MainAppView.redirectToPanel(MainAppView.CUSTOMERS_PANEL);
    }//GEN-LAST:event_cancelBtnActionPerformed
    // new NewCustomerForm().setVisible(true);

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField adressField;
    private javax.swing.JLabel adressLbl;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JPanel customerPnl;
    private javax.swing.JTextField emailField;
    private javax.swing.JLabel emailLbl;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLbl;
    private javax.swing.JTextArea observationField;
    private javax.swing.JLabel observationLbl;
    private javax.swing.JTextField phoneField;
    private javax.swing.JLabel phoneLbl;
    private javax.swing.JButton saveBtn;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextField taxIdField;
    private javax.swing.JLabel taxIdLbl;
    private javax.swing.JLabel titleField;
    // End of variables declaration//GEN-END:variables
}
