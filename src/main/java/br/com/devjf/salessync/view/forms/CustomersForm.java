package br.com.devjf.salessync.view.forms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import br.com.devjf.salessync.controller.CustomerController;
import br.com.devjf.salessync.model.Customer;
import br.com.devjf.salessync.view.MainAppView;
import br.com.devjf.salessync.view.components.style.ViewComponentStyle;
import br.com.devjf.salessync.view.components.table.TableEditButtonEditor;
import br.com.devjf.salessync.view.components.table.TableEditButtonRenderer;
import br.com.devjf.salessync.view.components.table.TableFormInterface;
import br.com.devjf.salessync.view.components.table.TableManager;
import java.awt.HeadlessException;

public class CustomersForm extends javax.swing.JFrame implements TableFormInterface {
    private static CustomersForm instance;
    private final CustomerController customerController;

    public CustomersForm() {
        initComponents();
        instance = this;
        this.customerController = new CustomerController();
        // Configurar a tabela
        setupTableColumns();
        // Configurar filtros
        TableManager.setupFilters(customerField,
                taxIdField,
                idField,
                customerTable);
        // Carregar dados iniciais
        loadTableData();
    }

    public static CustomersForm getInstance() {
        return instance;
    }

    /**
     * Adiciona um cliente à tabela.
     *
     * @param customerId O ID do cliente a ser adicionado
     */
    @Override
    public void setObjectToTable(Integer customerId) {
        try {
            // Get customer data
            Customer customer = customerController.findCustomerById(customerId);
            if (customer == null) {
                return;
            }
            Object[] rowData = {
                customer.getId(),
                customer.getName(),
                customer.getTaxId(),
                customer.getEmail(),
                customer.getPhone(),
                "Editar"
            };
            TableManager.addRow((DefaultTableModel) customerTable.getModel(),
                    rowData);
        } catch (Exception e) {
            System.err.println(
                    "Erro ao adicionar cliente à tabela: " + e.getMessage());
        }
    }

    @Override
    public final void loadTableData() {
        try {
            // Clear table
            TableManager.clearTable((DefaultTableModel) customerTable.getModel());
            // Get filter values
            Map<String, Object> filters = new HashMap<>();
            String customerFilter = customerField.getText().trim();
            String taxIdFilter = taxIdField.getText().trim();
            String idFilter = idField.getText().trim();
            // Add filters if they are not empty
            if (!customerFilter.isEmpty()) {
                filters.put("name",
                        customerFilter);
            }
            if (!taxIdFilter.isEmpty()) {
                filters.put("taxId",
                        taxIdFilter);
            }
            if (!idFilter.isEmpty()) {
                try {
                    filters.put("id",
                            Integer.valueOf(idFilter));
                } catch (NumberFormatException e) {
                    // Ignore invalid number format
                }
            }
            // Get customers through controller
            List<Customer> customers;
            if (!filters.isEmpty()) {
                Map<String, String> stringFilters = new HashMap<>();
                for (Map.Entry<String, Object> entry : filters.entrySet()) {
                    stringFilters.put(entry.getKey(),
                            entry.getValue().toString());
                }
                customers = customerController.filterCustomers(stringFilters);
            } else {
                customers = customerController.findAll();
            }
            // Add customers to table
            for (Customer customer : customers) {
                setObjectToTable(customer.getId());
            }
            // Adjust column widths
            TableManager.adjustColumnWidths(customerTable,
                    10);
        } catch (Exception e) {
            System.err.println(
                    "Erro ao carregar dados da tabela: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar clientes: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Configura as colunas da tabela de clientes.
     */
    @Override
    public final void setupTableColumns() {
        // Definir nomes das colunas
        String[] columnNames = {
            "Código", "Cliente", "CPF/CNPJ", "E-Mail", "Telefone", "Ações"
        };
        // Definir classes das colunas
        Class<?>[] columnClasses = {
            Integer.class, String.class, String.class, String.class, String.class, String.class
        };
        // Definir quais colunas são editáveis
        boolean[] editableColumns = {
            false, false, false, false, false, true
        };
        // Configurar o modelo da tabela
        TableManager.setupTableModel(customerTable,
                columnNames,
                columnClasses,
                editableColumns);
        // Configurar o botão de edição na coluna de ações
        setupEditButton();
        // Configurar o comportamento de seleção da tabela
        configureTableSelectionBehavior();
    }

    /**
     * Configura o comportamento de seleção da tabela para melhorar a
     * experiência do usuário
     */
    private void configureTableSelectionBehavior() {
        // Permitir seleção de apenas uma linha por vez
        customerTable.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION);
        // Configurar para que o clique na célula do botão inicie a edição imediatamente
        customerTable.putClientProperty("terminateEditOnFocusLost",
                Boolean.TRUE);
        // Permitir que a célula do botão seja editável com um único clique
        customerTable.setEditingColumn(5);
    }

    /**
     * Configura o botão de edição na coluna de ações da tabela.
     */
    private void setupEditButton() {
        // Obter a coluna de ações (última coluna)
        TableColumn actionColumn = customerTable.getColumnModel().getColumn(5);
        // Configurar o renderizador e o editor para a coluna de ações
        actionColumn.setCellRenderer(new TableEditButtonRenderer("Editar"));
        actionColumn.setCellEditor(new TableEditButtonEditor("Editar",
                row -> editCustomer(row)));
        // Definir largura preferencial para a coluna de ações
        actionColumn.setPreferredWidth(80);
        actionColumn.setMaxWidth(100);
        actionColumn.setMinWidth(80);
    }

    /**
     * Abre o formulário de edição para o cliente selecionado.
     *
     * @param selectedRow O índice da linha selecionada na tabela
     */
    private void editCustomer(int selectedRow) {
        try {
            // Obter o ID do cliente selecionado
            Integer customerId = (Integer) customerTable.getValueAt(selectedRow,
                    0);
            System.out.println("Editando cliente com ID: " + customerId);
            // Redirecionar para o painel de edição de venda com os dados da venda selecionada
            Customer customer = customerController.findCustomerById(customerId);
            MainAppView.redirectToPanel(MainAppView.EDIT_CUSTOMER_PANEL,
                    customer);
        } catch (HeadlessException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao abrir formulário de edição: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método público para atualizar a tabela de clientes. Pode ser chamado de
     * outras classes para atualizar a exibição após alterações.
     */
    @Override
    public void refreshTable() {
        loadTableData();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        filterLbl = new javax.swing.JLabel();
        newCustomerBtn = new javax.swing.JButton();
        deleteCustomerBtn = new javax.swing.JButton();
        filterPanel = new javax.swing.JPanel();
        idField = new javax.swing.JTextField();
        idLbl = new javax.swing.JLabel();
        taxIdLbl = new javax.swing.JLabel();
        taxIdField = new javax.swing.JTextField();
        customerLbl = new javax.swing.JLabel();
        customerField = new javax.swing.JTextField();
        tableScrollPanel = new javax.swing.JScrollPane();
        customerTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        filterLbl.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        filterLbl.setText("Filtar");

        newCustomerBtn.setBackground(new java.awt.Color(76, 175, 80));
        newCustomerBtn.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        newCustomerBtn.setForeground(new java.awt.Color(255, 255, 255));
        newCustomerBtn.setText("Novo Cliente");
        newCustomerBtn.setPreferredSize(new java.awt.Dimension(150, 40));
        ViewComponentStyle.standardCornerRadius(newCustomerBtn);
        newCustomerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newCustomerBtnActionPerformed(evt);
            }
        });

        deleteCustomerBtn.setBackground(new java.awt.Color(175, 76, 78));
        deleteCustomerBtn.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        deleteCustomerBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteCustomerBtn.setText("Excluir Cliente");
        deleteCustomerBtn.setPreferredSize(new java.awt.Dimension(150, 40));
        ViewComponentStyle.standardCornerRadius(deleteCustomerBtn);
        deleteCustomerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteCustomerBtnActionPerformed(evt);
            }
        });

        filterPanel.setPreferredSize(new java.awt.Dimension(917, 50));
        ViewComponentStyle.standardCornerRadius(filterPanel);

        idField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        idField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        idField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewComponentStyle.standardCornerRadius(idField);

        idLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        idLbl.setText("Código:");

        taxIdLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        taxIdLbl.setText("CPF/CNPJ:");

        taxIdField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        taxIdField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        taxIdField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewComponentStyle.standardCornerRadius(taxIdField);

        customerLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        customerLbl.setText("Cliente:");

        customerField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        customerField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        customerField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewComponentStyle.standardCornerRadius(customerField);

        javax.swing.GroupLayout filterPanelLayout = new javax.swing.GroupLayout(filterPanel);
        filterPanel.setLayout(filterPanelLayout);
        filterPanelLayout.setHorizontalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addComponent(customerLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(customerField, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(idLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(taxIdLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(taxIdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(102, Short.MAX_VALUE))
        );
        filterPanelLayout.setVerticalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(idLbl)
                        .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(taxIdLbl)
                        .addComponent(taxIdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(customerLbl)
                        .addComponent(customerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );

        customerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Cliente", "CPF/CNPJ", "E-Mail", "Telefone", "Ações"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableScrollPanel.setViewportView(customerTable);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                            .addComponent(deleteCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(newCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(tableScrollPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 917, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(filterPanel, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 916, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterLbl, javax.swing.GroupLayout.Alignment.CENTER))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(filterLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(tableScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(69, Short.MAX_VALUE))
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

    private void newCustomerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newCustomerBtnActionPerformed
        try {
            MainAppView.redirectToPanel(MainAppView.NEW_CUSTOMER_PANEL);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao abrir formulário de novo cliente: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_newCustomerBtnActionPerformed

    private void deleteCustomerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteCustomerBtnActionPerformed
        // Get selected row
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um cliente para excluir",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Confirmar exclusão
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir este cliente?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Get the customer ID before any operations
            Integer customerId = (Integer) customerTable.getValueAt(selectedRow,
                    0);
            // Store the table model before deletion
            DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
            boolean success = customerController.deleteCustomer(customerId);
            if (!success) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir o cliente",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Log the activity before removing the row
            MainAppView.getInstance().registerUserActivity(
                    "Excluiu o cliente ID:" + customerId);
            // Remove the row directly from the model
            model.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this,
                    "Cliente excluído com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            // Refresh the table if needed
            refreshTable();
        }
    }//GEN-LAST:event_deleteCustomerBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField customerField;
    private javax.swing.JLabel customerLbl;
    private javax.swing.JTable customerTable;
    private javax.swing.JButton deleteCustomerBtn;
    private javax.swing.JLabel filterLbl;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel idLbl;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton newCustomerBtn;
    private javax.swing.JScrollPane tableScrollPanel;
    private javax.swing.JTextField taxIdField;
    private javax.swing.JLabel taxIdLbl;
    // End of variables declaration//GEN-END:variables
}
