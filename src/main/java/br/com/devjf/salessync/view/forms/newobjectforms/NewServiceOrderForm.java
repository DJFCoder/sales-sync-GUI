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
import br.com.devjf.salessync.util.CustomerSelectionUtil;
import br.com.devjf.salessync.util.ViewUtil;
import br.com.devjf.salessync.view.MainAppView;

public class NewServiceOrderForm extends javax.swing.JFrame {
    private Customer selectedCustomer;
    private ServiceOrder serviceOrder;
    private boolean isEditMode = false;
    private final ServiceOrderController serviceOrderController;
    private final CustomerController customerController;
    private final SaleController saleController;

    public NewServiceOrderForm() {
        initComponents();
        this.serviceOrderController = new ServiceOrderController();
        this.customerController = new CustomerController();
        this.saleController = new SaleController();
        this.isEditMode = false;
        this.titleField.setText("Cadastrar Ordem de Serviço");
        
        // Initialize the sale combo box
        saleIdCmb.addItem("Selecione");
    }
    
    /**
     * Construtor para edição de ordem de serviço existente
     * 
     * @param serviceOrder A ordem de serviço a ser editada
     */
    public NewServiceOrderForm(ServiceOrder serviceOrder) {
        initComponents();
        this.serviceOrderController = new ServiceOrderController();
        this.customerController = new CustomerController();
        this.saleController = new SaleController();
        
        // Initialize the sale combo box
        saleIdCmb.addItem("Selecione");
        
        // Se a ordem de serviço for fornecida diretamente, usá-la
        if (serviceOrder != null) {
            this.serviceOrder = serviceOrder;
        } else {
            this.serviceOrder = null;
        }
        
        this.isEditMode = (this.serviceOrder != null);
        this.titleField.setText(isEditMode ? "Editar Ordem de Serviço" : "Cadastrar Ordem de Serviço");
        
        // Preencher os campos com os dados da ordem de serviço
        if (this.serviceOrder != null) {
            idField.setText(String.valueOf(this.serviceOrder.getId()));
            
            // Configurar o cliente selecionado
            this.selectedCustomer = this.serviceOrder.getCustomer();
            if (selectedCustomer != null) {
                customerField.setText(selectedCustomer.getName());
                
                // Carregar as vendas do cliente
                loadCustomerSales(selectedCustomer.getId());
            }
            
            // Configurar o status
            if (this.serviceOrder.getStatus() != null) {
                for (int i = 0; i < statusCmb.getItemCount(); i++) {
                    if (statusCmb.getItemAt(i).equals(this.serviceOrder.getStatus().toString())) {
                        statusCmb.setSelectedIndex(i);
                        break;
                    }
                }
            }
            
            // Configurar a venda associada, se houver
            if (this.serviceOrder.getSale() != null) {
                // Selecionar a venda no combobox
                saleIdCmb.setSelectedItem(String.valueOf(this.serviceOrder.getSale().getId()));
            }
            
            // Configurar a descrição
            descriptionField.setText(this.serviceOrder.getDescription());
        }
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
        
        // Initialize the sale combo box
        saleIdCmb.addItem("Selecione");
        
        // Carregar a ordem de serviço do banco de dados usando o controller
        if (serviceOrderId != null) {
            this.serviceOrder = serviceOrderController.findServiceOrderById(serviceOrderId);
        } else {
            this.serviceOrder = null;
        }
        
        this.isEditMode = (this.serviceOrder != null);
        this.titleField.setText(isEditMode ? "Editar Ordem de Serviço" : "Cadastrar Ordem de Serviço");
        
        // Preencher os campos com os dados da ordem de serviço
        if (this.serviceOrder != null) {
            idField.setText(String.valueOf(this.serviceOrder.getId()));
            
            // Configurar o cliente selecionado
            this.selectedCustomer = this.serviceOrder.getCustomer();
            if (selectedCustomer != null) {
                customerField.setText(selectedCustomer.getName());
                
                // Carregar as vendas do cliente
                loadCustomerSales(selectedCustomer.getId());
            }
            
            // Configurar o status
            if (this.serviceOrder.getStatus() != null) {
                for (int i = 0; i < statusCmb.getItemCount(); i++) {
                    if (statusCmb.getItemAt(i).equals(this.serviceOrder.getStatus().toString())) {
                        statusCmb.setSelectedIndex(i);
                        break;
                    }
                }
            }
            
            // Configurar a venda associada, se houver
            if (this.serviceOrder.getSale() != null) {
                // Selecionar a venda no combobox
                saleIdCmb.setSelectedItem(String.valueOf(this.serviceOrder.getSale().getId()));
            }
            
            // Configurar a descrição
            descriptionField.setText(this.serviceOrder.getDescription());
        }
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
        ViewUtil.standardCornerRadius(serviceOrderPnl);

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

        idField.setEditable(false);
        idField.setPreferredSize(new java.awt.Dimension(180, 40));
        ViewUtil.standardCornerRadius(idField);

        idLbl.setText("Código:");

        customerField.setEditable(false);
        customerField.setPreferredSize(new java.awt.Dimension(437, 40));
        ViewUtil.standardCornerRadius(customerField);

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
        ViewUtil.standardCornerRadius(findCustomerBtn);
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
            List<Sale> sales = serviceOrderController.findSalesByCustomerId(customerId);
            
            // Adicionar as vendas ao combobox
            for (Sale sale : sales) {
                saleIdCmb.addItem(String.valueOf(sale.getId()));
            }
        }
    }

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        MainAppView.redirectToPanel(MainAppView.SERVICE_ORDERS_PANEL);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        // Verificar se um cliente foi selecionado
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, selecione um cliente.",
                    "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Verificar se um status foi selecionado
        if (statusCmb.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, selecione um status.",
                    "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Criar ou atualizar a ordem de serviço
        if (serviceOrder == null) {
            serviceOrder = new ServiceOrder();
        }
        
        // Configurar o cliente
        serviceOrder.setCustomer(selectedCustomer);
        
        // Configurar o status
        String statusStr = statusCmb.getSelectedItem().toString();
        serviceOrder.setStatus(ServiceStatus.valueOf(statusStr));
        
        // Configurar a venda, se selecionada
        if (saleIdCmb.getSelectedIndex() > 0) {
            String saleIdStr = saleIdCmb.getSelectedItem().toString();
            Integer saleId = Integer.parseInt(saleIdStr);
            Sale sale = saleController.findSaleById(saleId);
            serviceOrder.setSale(sale);
        } else {
            serviceOrder.setSale(null);
        }
        
        // Configurar a descrição
        serviceOrder.setDescription(descriptionField.getText());
        
        // Salvar a ordem de serviço
        boolean success;
        if (isEditMode) {
            success = serviceOrderController.updateServiceOrder(serviceOrder);
        } else {
            success = serviceOrderController.createServiceOrder(serviceOrder);
        }
        
        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Ordem de serviço " + (isEditMode ? "atualizada" : "cadastrada") + " com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            MainAppView.redirectToPanel(MainAppView.SERVICE_ORDERS_PANEL);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Erro ao " + (isEditMode ? "atualizar" : "cadastrar") + " ordem de serviço.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void findCustomerBtnActionPerformed(java.awt.event.ActionEvent evt) {
        // Disable the button and show wait cursor to indicate processing
        findCustomerBtn.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        // Use SwingWorker to perform database operation in background
        SwingWorker<Customer, Void> worker = new SwingWorker<Customer, Void>() {
            @Override
            protected Customer doInBackground() throws Exception {
                // Use the utility class to select a customer
                return CustomerSelectionUtil.selectCustomer(NewServiceOrderForm.this);
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
    }//GEN-LAST:event_findCustomerBtnActionPerformed
    // new NewServiceOrderForm().setVisible(true);

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