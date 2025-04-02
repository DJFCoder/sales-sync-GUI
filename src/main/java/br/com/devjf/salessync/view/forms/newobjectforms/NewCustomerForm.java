package br.com.devjf.salessync.view.forms.newobjectforms;

import java.awt.Cursor;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import br.com.devjf.salessync.controller.CustomerController;
import br.com.devjf.salessync.controller.UserController;
import br.com.devjf.salessync.model.Customer;
import br.com.devjf.salessync.view.MainAppView;
import br.com.devjf.salessync.view.components.style.ViewComponentStyle;
import br.com.devjf.salessync.view.forms.CustomersForm;
import java.awt.HeadlessException;
import java.util.concurrent.ExecutionException;

public class NewCustomerForm extends javax.swing.JFrame {
    private Customer customerToCreate;
    private boolean isEditMode = false;
    // Controllers
    private final CustomerController customerController;
    private final UserController userController;

    public NewCustomerForm() {
        initComponents();
        this.customerController = new CustomerController();
        this.userController = new UserController();
        initForNewCustomer();
    }

    /**
     * Construtor para edição de cliente existente usando o ID
     *
     * @param customerId O ID do cliente a ser editado
     */
    public NewCustomerForm(Integer customerId) {
        initComponents();
        this.customerController = new CustomerController();
        this.userController = new UserController();
        // Carregar o cliente do banco de dados usando o controller
        if (customerId == null) {
            this.customerToCreate = null;
        }
        this.customerToCreate = customerController.findCustomerById(customerId);
        this.isEditMode = true;
        this.titleField.setText(
                isEditMode ? "Atualizar" : "Salvar");
        // Preencher os campos com os dados do cliente
        if (this.customerToCreate != null) {
            nameField.setText(this.customerToCreate.getName());
            taxIdField.setText(this.customerToCreate.getTaxId());
            emailField.setText(this.customerToCreate.getEmail());
            phoneField.setText(this.customerToCreate.getPhone());
            adressField.setText(this.customerToCreate.getAddress());
            observationField.setText(this.customerToCreate.getNotes());
        }
    }

    private void setCustomerInformations() {
        customerToCreate.setName(nameField.getText());
        customerToCreate.setTaxId(customerController.formatTaxId(taxIdField.getText()));
        customerToCreate.setEmail(emailField.getText());
        customerToCreate.setPhone(customerController.formatPhone(phoneField.getText()));
        customerToCreate.setAddress(adressField.getText());
        customerToCreate.setNotes(observationField.getText());
    }

    private void initForNewCustomer() {
        // Explicitar que não é modo de edição
        isEditMode = false;
        // Limpar o objeto customerToCreate para garantir que estamos criando um novo cliente
        customerToCreate = new Customer();
        // Limpar campos do formulário
        nameField.setText("");
        taxIdField.setText("");
        emailField.setText("");
        phoneField.setText("");
        adressField.setText("");
        observationField.setText("");
        // Atualizar o título do botão para "Registrar Cliente"
        saveBtn.setText("Salvar");
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
            @Override
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
            @Override
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
        // Show a wait cursor while processing
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        saveBtn.setEnabled(false);
        // Add a small delay to ensure any previous transactions are complete
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Make sure we have a customer object to work with
        if (customerToCreate == null) {
            customerToCreate = new Customer();
        }
        // Update customer information from form fields BEFORE starting the SwingWorker
        setCustomerInformations();
        // Prepare the customer object on the EDT before starting the background task
        final Customer preparedCustomer = customerToCreate;
        // Use SwingWorker to avoid freezing the UI
        new SwingWorker<Boolean, Void>() {
            private String errorMessage = null;

            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    Boolean result;
                    if (isEditMode) {
                        // Update existing customer
                        result = customerController.updateCustomer(
                                preparedCustomer);
                        if (!result) {
                            errorMessage = "Não foi possível atualizar o(a) cliente. Verifique os dados e tente novamente.";
                            System.err.println("Failed to update customer");
                        }
                        return result;
                    }
                    // Create new customer
                    result = customerController.createCustomer(preparedCustomer);
                    if (!result) {
                        errorMessage = "Não foi possível registrar o(a) cliente. Verifique os dados e tente novamente.";
                        System.err.println("Failed to register customer");
                    }
                    return result;
                } catch (Exception e) {
                    errorMessage = "Erro ao processar cliente: " + e.getMessage();
                    System.err.println(
                            "Exception during customer processing: " + e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void done() {
                try {
                    Boolean success = get();
                    if (success) {
                        // Log the activity
                        MainAppView.getInstance().registerUserActivity(
                                (isEditMode ? "Atualizou" : "Registrou") + " o cliente: " + preparedCustomer.getName());
                        JOptionPane.showMessageDialog(NewCustomerForm.this,
                                "Cliente " + (isEditMode ? "atualizado" : "registrado") + " com sucesso!",
                                "Sucesso",
                                JOptionPane.INFORMATION_MESSAGE);
                        // Close this form
                        dispose();
                        // Refresh the customers table
                        CustomersForm.getInstance().refreshTable();
                        // Redirect to customers panel
                        MainAppView.redirectToPanel(MainAppView.CUSTOMERS_PANEL);
                    } else if (errorMessage != null) {
                        System.err.println("Error message: " + errorMessage);
                        JOptionPane.showMessageDialog(NewCustomerForm.this,
                                errorMessage,
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (HeadlessException | InterruptedException | ExecutionException e) {
                    System.err.println("Exception in done(): " + e.getMessage());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(NewCustomerForm.this,
                            "Erro ao processar cliente: " + e.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    // Restore the default cursor
                    setCursor(Cursor.getDefaultCursor());
                    saveBtn.setEnabled(true);
                    initForNewCustomer();
                }
            }
        }.execute();
    }//GEN-LAST:event_saveBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        MainAppView.redirectToPanel(MainAppView.CUSTOMERS_PANEL);
    }//GEN-LAST:event_cancelBtnActionPerformed

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
