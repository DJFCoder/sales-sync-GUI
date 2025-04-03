package br.com.devjf.salessync.view.forms.newobjectforms;

import java.awt.Cursor;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import br.com.devjf.salessync.controller.CustomerController;
import br.com.devjf.salessync.controller.SaleController;
import br.com.devjf.salessync.controller.ServiceOrderController;
import br.com.devjf.salessync.model.Customer;
import br.com.devjf.salessync.model.Sale;
import br.com.devjf.salessync.model.ServiceOrder;
import br.com.devjf.salessync.model.ServiceStatus;
import br.com.devjf.salessync.view.MainAppView;
import br.com.devjf.salessync.view.components.CustomerSelectionDialog;
import br.com.devjf.salessync.view.components.style.ViewComponentStyle;
import br.com.devjf.salessync.view.forms.validators.ServiceOrderFormValidator;

public class NewServiceOrderForm extends javax.swing.JFrame {
    private Customer selectedCustomer;
    private ServiceOrder serviceOrderToCreate;
    private boolean isEditMode = false;
    private final ServiceOrderController serviceOrderController;
    private final CustomerController customerController;
    private final SaleController saleController;
    
    public NewServiceOrderForm() {
        initComponents();
        this.serviceOrderController = new ServiceOrderController();
        this.customerController = new CustomerController();
        this.saleController = new SaleController();
        // Initialize the sale combo box
        saleIdCmb.addItem("Selecione");
        initForNewOs();
    }

    /**
     * Construtor para edição de ordem de serviço existente usando o ID
     *
     * @param serviceOrderId O ID da ordem de serviço a ser editada
     */
    public NewServiceOrderForm(Integer serviceOrderId) {
        initComponents();
        this.serviceOrderController = new ServiceOrderController();
        this.customerController = new CustomerController();
        this.saleController = new SaleController();
        loadOsFromDb(serviceOrderId);
    }
    
    private void findCustomerBtnActionPerformed(java.awt.event.ActionEvent evt) {
        // Disable the button and show wait cursor to indicate processing
        findCustomerBtn.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        // Use SwingWorker to perform database operation in background
        SwingWorker<Customer, Void> worker = new SwingWorker<Customer, Void>() {
            @Override
            protected Customer doInBackground() throws Exception {
                // Use the utility class to select a customer
                return CustomerSelectionDialog.selectCustomer(
                        NewServiceOrderForm.this);
            }
            
            @Override
            protected void done() {
                try {
                    // Get the selected customer from the background task
                    Customer customer = get();
                    // Update the form if a customer was selected
                    if (customer != null) {
                        selectedCustomer = customer;
                        customerField.setText(selectedCustomer.getName());
                        // Carregar as vendas do cliente
                        loadCustomerSales(selectedCustomer.getId());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    JOptionPane.showMessageDialog(NewServiceOrderForm.this,
                            "Erro ao buscar cliente: " + e.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    // Restore cursor and enable button regardless of outcome
                    setCursor(Cursor.getDefaultCursor());
                    findCustomerBtn.setEnabled(true);
                }
            }
        };
        // Start the background task
        worker.execute();
    }

    /**
     * Carrega as vendas do cliente no combobox
     *
     * @param customerId ID do cliente
     */
    private void loadCustomerSales(Integer customerId) {
        // Limpar o combobox mantendo apenas o item "Selecione"
        while (saleIdCmb.getItemCount() > 1) {
            saleIdCmb.removeItemAt(1);
        }
        if (customerId != null) {
            // Buscar as vendas do cliente
            List<Sale> sales = serviceOrderController.findSalesByCustomerId(
                    customerId);
            // Check if the list is empty (not null)
            if (sales == null || sales.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Cliente não possui vendas cadastradas",
                        "Informação",
                        JOptionPane.INFORMATION_MESSAGE);
                customerField.setText("");
                return;
            }
            // Adicionar as vendas ao combobox
            for (Sale sale : sales) {
                saleIdCmb.addItem(String.valueOf(sale.getId()));
            }
        }
    }
    
    private void loadStatusEnum() {
        // Carregar o status - Mapeando enum em inglês para strings em português
        if (serviceOrderToCreate.getStatus() != null) {
            String statusStr;
            switch (serviceOrderToCreate.getStatus()) {
                case PENDING:
                    statusStr = "PENDENTE";
                    break;
                case IN_PROGRESS:
                    statusStr = "EM ANDAMENTO";
                    break;
                case CANCELED:
                    statusStr = "CANCELADA";
                    break;
                case COMPLETED:
                    statusStr = "FINALIZADA";
                    break;
                default:
                    statusStr = "Selecione";
            }
            for (int i = 0; i < statusCmb.getItemCount(); i++) {
                if (statusCmb.getItemAt(i).equals(statusStr)) {
                    statusCmb.setSelectedIndex(i);
                    break;
                }
            }
        }
        // Configurar as vendas associadas no combobox
        if (serviceOrderToCreate.getSale() != null) {
            saleIdCmb.setSelectedItem(String.valueOf(
                    serviceOrderToCreate.getSale().getId()));
        }
    }
    
    private ServiceStatus getStatusEnum() {
        // Configurar o status - Mapeando strings em português para enum em inglês
        String statusStr = statusCmb.getSelectedItem().toString();
        switch (statusStr) {
            case "PENDENTE":
                return ServiceStatus.PENDING;
            case "EM ANDAMENTO":
                return ServiceStatus.IN_PROGRESS;
            case "CANCELADA":
                return ServiceStatus.CANCELED;
            case "FINALIZADA":
                return ServiceStatus.COMPLETED;
            default:
                throw new IllegalArgumentException(
                        "Status inválido: " + statusStr);
        }
    }
    
    private void loadOsFromDb(Integer serviceOrderId) {
        // Carregar a ordem de serviço do banco de dados usando o controller
        if (serviceOrderId == null) {
            serviceOrderToCreate = null;
            return;
        }
        isEditMode = true;
        serviceOrderToCreate = serviceOrderController.findServiceOrderById(
                serviceOrderId);
        titleField.setText(
                isEditMode ? "Atualizar" : "Salvar");
        // Preencher os campos com os dados da ordem de serviço
        idField.setText(String.valueOf(serviceOrderToCreate.getId()));
        // Configurar o cliente selecionado
        selectedCustomer = serviceOrderToCreate.getCustomer();
        if (selectedCustomer != null) {
            customerField.setText(selectedCustomer.getName());
            // Carregar as vendas do cliente
            loadCustomerSales(selectedCustomer.getId());
            // Carregar o status usando o método helper
            loadStatusEnum();
            // Preencher a descrição
            descriptionField.setText(serviceOrderToCreate.getDescription());
        }
    }
    
    private void initForNewOs() {
        isEditMode = false;
        serviceOrderToCreate = new ServiceOrder();
        idField.setText("");
        customerField.setText("");
        statusCmb.setSelectedIndex(0);
        saleIdCmb.setSelectedIndex(0);
        descriptionField.setText("");
        saveBtn.setText("Salvar");
    }

    /**
     * Handles the completion of a service order, setting its status to
     * COMPLETED and automatically recording the completion date.
     *
     * @param serviceOrderId The ID of the service order to complete
     * @param customerName The name of the customer for logging purposes
     */
    private void handleServiceOrderCompletion(Integer serviceOrderId, String customerName) {
        // Show a wait cursor while processing
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        saveBtn.setEnabled(false);
        // Use SwingWorker to avoid freezing the UI
        new SwingWorker<Boolean, Void>() {
            private String errorMessage = null;
            
            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    // Complete the service order
                    return serviceOrderController.completeServiceOrder(
                            serviceOrderId);
                } catch (Exception e) {
                    errorMessage = "Erro ao finalizar ordem de serviço: " + e.getMessage();
                    System.err.println(
                            "Exception during service order completion: " + e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }
            
            @Override
            protected void done() {
                try {
                    Boolean success = get();
                    if (success) {
                        // Log the activity if MainAppView has an instance method
                        if (MainAppView.getInstance() != null) {
                            MainAppView.getInstance().registerUserActivity(
                                    "Finalizou a ordem de serviço para o cliente: " + customerName);
                        }
                        JOptionPane.showMessageDialog(
                                NewServiceOrderForm.this,
                                "Ordem de serviço finalizada com sucesso!",
                                "Sucesso",
                                JOptionPane.INFORMATION_MESSAGE);
                        // Redirect to service orders panel
                        MainAppView.redirectToPanel(
                                MainAppView.SERVICE_ORDERS_PANEL);
                    } else if (errorMessage != null) {
                        System.err.println("Error message: " + errorMessage);
                        JOptionPane.showMessageDialog(
                                NewServiceOrderForm.this,
                                errorMessage,
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println(
                            "Exception in done(): " + e.getMessage());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(NewServiceOrderForm.this,
                            "Erro ao finalizar ordem de serviço: " + e.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    // Restore the default cursor
                    setCursor(Cursor.getDefaultCursor());
                    saveBtn.setEnabled(true);
                }
            }
        }.execute();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        serviceOrderPnl = new javax.swing.JPanel();
        cancelBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        idField = new javax.swing.JTextField();
        idLbl = new javax.swing.JLabel();
        customerField = new javax.swing.JTextField();
        taxIdLbl = new javax.swing.JLabel();
        statusLbl = new javax.swing.JLabel();
        saleLbl = new javax.swing.JLabel();
        descriptionLbl = new javax.swing.JLabel();
        scrollPane = new javax.swing.JScrollPane();
        descriptionField = new javax.swing.JTextArea();
        findCustomerBtn = new javax.swing.JButton();
        saleIdCmb = new javax.swing.JComboBox<>();
        statusCmb = new javax.swing.JComboBox<>();
        titleField = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        serviceOrderPnl.setPreferredSize(new java.awt.Dimension(813, 594));
        ViewComponentStyle.standardCornerRadius(serviceOrderPnl);

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

        idField.setEditable(false);
        idField.setPreferredSize(new java.awt.Dimension(180, 40));
        ViewComponentStyle.standardCornerRadius(idField);

        idLbl.setText("Código:");

        customerField.setEditable(false);
        customerField.setPreferredSize(new java.awt.Dimension(437, 40));
        ViewComponentStyle.standardCornerRadius(customerField);

        taxIdLbl.setText("Cliente:");

        statusLbl.setText("Status:");

        saleLbl.setText("Compra:");

        descriptionLbl.setText("Descrição:");

        descriptionField.setRows(20);
        descriptionField.setTabSize(2);
        scrollPane.setViewportView(descriptionField);

        findCustomerBtn.setBackground(new java.awt.Color(33, 150, 243));
        findCustomerBtn.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        findCustomerBtn.setForeground(new java.awt.Color(255, 255, 255));
        findCustomerBtn.setText("Buscar");
        findCustomerBtn.setPreferredSize(new java.awt.Dimension(120, 40));
        ViewComponentStyle.standardCornerRadius(findCustomerBtn);
        findCustomerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findCustomerBtnActionPerformed(evt);
            }
        });

        saleIdCmb.setPreferredSize(new java.awt.Dimension(180, 40));

        statusCmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "PENDENTE", "EM ANDAMENTO", "CANCELADA", "FINALIZADA" }));
        statusCmb.setPreferredSize(new java.awt.Dimension(180, 40));

        javax.swing.GroupLayout serviceOrderPnlLayout = new javax.swing.GroupLayout(serviceOrderPnl);
        serviceOrderPnl.setLayout(serviceOrderPnlLayout);
        serviceOrderPnlLayout.setHorizontalGroup(
            serviceOrderPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, serviceOrderPnlLayout.createSequentialGroup()
                .addGroup(serviceOrderPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(serviceOrderPnlLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, serviceOrderPnlLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(serviceOrderPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(serviceOrderPnlLayout.createSequentialGroup()
                                .addGroup(serviceOrderPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(serviceOrderPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(idLbl)
                                        .addComponent(statusCmb, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(statusLbl)
                                    .addComponent(descriptionLbl))
                                .addGap(18, 18, 18)
                                .addGroup(serviceOrderPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(serviceOrderPnlLayout.createSequentialGroup()
                                        .addComponent(customerField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)
                                        .addComponent(findCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(serviceOrderPnlLayout.createSequentialGroup()
                                        .addGroup(serviceOrderPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(saleLbl)
                                            .addComponent(saleIdCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(taxIdLbl))
                                        .addGap(0, 0, Short.MAX_VALUE)))))))
                .addGap(20, 20, 20))
        );
        serviceOrderPnlLayout.setVerticalGroup(
            serviceOrderPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, serviceOrderPnlLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(serviceOrderPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idLbl)
                    .addComponent(taxIdLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(serviceOrderPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(findCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(serviceOrderPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusLbl)
                    .addComponent(saleLbl, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(serviceOrderPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saleIdCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(descriptionLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(serviceOrderPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        titleField.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        titleField.setText("Informações da Ordem");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(titleField)
                    .addComponent(serviceOrderPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(titleField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(serviceOrderPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
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
        initForNewOs();
        MainAppView.redirectToPanel(MainAppView.SERVICE_ORDERS_PANEL);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        // Show a wait cursor while processing
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        saveBtn.setEnabled(false);
        try {
            // Validate all required fields
            ServiceOrderFormValidator.validateAllFields(
                    selectedCustomer,
                    statusCmb,
                    descriptionField);
            // If validation passes, add a small delay to ensure any previous transactions are complete
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // Make sure we have a service order object to work with
            if (serviceOrderToCreate == null) {
                serviceOrderToCreate = new ServiceOrder();
            }
            // Update service order information from form fields BEFORE starting the SwingWorker
            serviceOrderToCreate.setCustomer(selectedCustomer);
            serviceOrderToCreate.setStatus(getStatusEnum());
            // Configure the sale if selected
            if (saleIdCmb.getSelectedIndex() > 0) {
                String saleIdStr = saleIdCmb.getSelectedItem().toString();
                Integer saleId = Integer.valueOf(saleIdStr);
                Sale sale = saleController.findSaleById(saleId);
                serviceOrderToCreate.setSale(sale);
            } else {
                serviceOrderToCreate.setSale(null);
            }
            // Configure the description
            serviceOrderToCreate.setDescription(descriptionField.getText());
            // Prepare the service order object on the EDT before starting the background task
            final ServiceOrder preparedServiceOrder = serviceOrderToCreate;
            final boolean finalIsEditMode = isEditMode;
            // Use SwingWorker to avoid freezing the UI
            new SwingWorker<Boolean, Void>() {
                private String errorMessage = null;
                
                @Override
                protected Boolean doInBackground() throws Exception {
                    try {
                        Boolean result;
                        if (finalIsEditMode) {
                            // Update existing service order
                            result = serviceOrderController.updateServiceOrder(
                                    preparedServiceOrder);
                            if (!result) {
                                errorMessage = "Não foi possível atualizar a ordem de serviço. Verifique os dados e tente novamente.";
                                System.err.println(
                                        "Failed to update service order");
                            }
                            return result;
                        }
                        // Create new service order
                        result = serviceOrderController.createServiceOrder(
                                preparedServiceOrder);
                        if (!result) {
                            errorMessage = "Não foi possível registrar a ordem de serviço. Verifique os dados e tente novamente.";
                            System.err.println(
                                    "Failed to register service order");
                        }
                        return result;
                    } catch (Exception e) {
                        errorMessage = "Erro ao processar ordem de serviço: " + e.getMessage();
                        System.err.println(
                                "Exception during service order processing: " + e.getMessage());
                        e.printStackTrace();
                        return false;
                    }
                }
                
                @Override
                protected void done() {
                    try {
                        Boolean success = get();
                        if (success) {
                            // If the status is COMPLETED and this is an edit operation, handle completion
                            if (preparedServiceOrder.getStatus() == ServiceStatus.COMPLETED
                                    && preparedServiceOrder.getId() != null) {
                                // Call the handleServiceOrderCompletion method
                                handleServiceOrderCompletion(
                                        preparedServiceOrder.getId(),
                                        preparedServiceOrder.getCustomer().getName());
                            } else {
                                // Log the activity if MainAppView has an instance method
                                if (MainAppView.getInstance() != null) {
                                    MainAppView.getInstance().registerUserActivity(
                                            (finalIsEditMode ? "Atualizou" : "Registrou") + " a ordem de serviço para o cliente: "
                                            + preparedServiceOrder.getCustomer().getName());
                                }
                                JOptionPane.showMessageDialog(
                                        NewServiceOrderForm.this,
                                        "Ordem de serviço " + (finalIsEditMode ? "atualizada" : "salva") + " com sucesso!",
                                        "Sucesso",
                                        JOptionPane.INFORMATION_MESSAGE);
                                // Redirect to service orders panel
                                MainAppView.redirectToPanel(
                                        MainAppView.SERVICE_ORDERS_PANEL);
                            }
                        } else if (errorMessage != null) {
                            System.err.println(
                                    "Error message: " + errorMessage);
                            JOptionPane.showMessageDialog(
                                    NewServiceOrderForm.this,
                                    errorMessage,
                                    "Erro",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        System.err.println(
                                "Exception in done(): " + e.getMessage());
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(NewServiceOrderForm.this,
                                "Erro ao processar ordem de serviço: " + e.getMessage(),
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                    } finally {
                        // Restore the default cursor
                        setCursor(Cursor.getDefaultCursor());
                        saveBtn.setEnabled(true);
                    }
                }
            }.execute();
        } catch (IllegalStateException e) {
            // Handle validation errors
            System.err.println("Validation error: " + e.getMessage());
            JOptionPane.showMessageDialog(NewServiceOrderForm.this,
                    e.getMessage(),
                    "Erro de Validação",
                    JOptionPane.WARNING_MESSAGE);
            // Restore the default cursor and re-enable the save button
            setCursor(Cursor.getDefaultCursor());
            saveBtn.setEnabled(true);
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JTextField customerField;
    private javax.swing.JTextArea descriptionField;
    private javax.swing.JLabel descriptionLbl;
    private javax.swing.JButton findCustomerBtn;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel idLbl;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JComboBox<String> saleIdCmb;
    private javax.swing.JLabel saleLbl;
    private javax.swing.JButton saveBtn;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JPanel serviceOrderPnl;
    private javax.swing.JComboBox<String> statusCmb;
    private javax.swing.JLabel statusLbl;
    private javax.swing.JLabel taxIdLbl;
    private javax.swing.JLabel titleField;
    // End of variables declaration//GEN-END:variables
}
