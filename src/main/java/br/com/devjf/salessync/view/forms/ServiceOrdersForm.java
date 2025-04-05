package br.com.devjf.salessync.view.forms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import br.com.devjf.salessync.controller.ServiceOrderController;
import br.com.devjf.salessync.model.ServiceOrder;
import br.com.devjf.salessync.model.ServiceStatus;
import br.com.devjf.salessync.view.MainAppView;
import br.com.devjf.salessync.view.components.style.ViewComponentStyle;
import br.com.devjf.salessync.view.components.table.TableFormInterface;
import br.com.devjf.salessync.view.components.table.TableManager;
import java.awt.HeadlessException;
import javax.swing.table.TableColumn;

public class ServiceOrdersForm extends javax.swing.JFrame implements TableFormInterface {
    private static ServiceOrdersForm instance;
    private final ServiceOrderController serviceOrderController;

    public ServiceOrdersForm() {
        instance = this;
        initComponents();
        this.serviceOrderController = new ServiceOrderController();
        setupTableColumns();
        TableManager.setupFilters(customerField,
                statusField,
                idField,
                serviceOrderTable);
        loadTableData();
    }

    public static ServiceOrdersForm getInstance() {
        return instance;
    }

    @Override
    public final void setupTableColumns() {
        // Definir nomes das colunas
        String[] columnNames = {
            "Código", "Cliente", "Descrição", "Data Criação", "Status", "Ações"
        };
        // Definir classes das colunas - Alterando para LocalDate em vez de java.util.Date
        Class<?>[] columnClasses = {
            Integer.class, String.class, String.class, java.time.LocalDate.class, String.class, String.class
        };
        // Definir quais colunas são editáveis
        boolean[] editableColumns = {
            false, false, false, false, false, true
        };
        // Configurar o modelo da tabela
        TableManager.setupTableModel(serviceOrderTable,
                columnNames,
                columnClasses,
                editableColumns);
        // Configurar o botão de edição na coluna de ações
        setupEditButton();
        // Configurar o comportamento de seleção da tabela
        configureTableSelectionBehavior();
    }

    @Override
    public final void loadTableData() {
        try {
            // Clear table
            TableManager.clearTable(
                    (DefaultTableModel) serviceOrderTable.getModel());
            // Get filter values
            Map<String, Object> filters = new HashMap<>();
            String customerFilter = customerField.getText().trim();
            String statusFilter = statusField.getText().trim();
            String idFilter = idField.getText().trim();
            // Add filters if they are not empty
            if (!customerFilter.isEmpty()) {
                filters.put("customerName",
                        customerFilter);
            }
            if (!statusFilter.isEmpty()) {
                filters.put("status",
                        statusFilter);
            }
            if (!idFilter.isEmpty()) {
                try {
                    filters.put("id",
                            Integer.valueOf(idFilter));
                } catch (NumberFormatException e) {
                    // Ignore invalid number format
                }
            }
            // Get service orders through controller
            List<ServiceOrder> serviceOrders;
            if (!filters.isEmpty()) {
                serviceOrders = serviceOrderController.listServiceOrders(filters);
            } else {
                // Usando listServiceOrders com um mapa vazio para obter todos
                serviceOrders = serviceOrderController.listServiceOrders(
                        new HashMap<>());
            }
            // Add service orders to table
            for (ServiceOrder serviceOrder : serviceOrders) {
                setObjectToTable(serviceOrder.getId());
            }
            // Adjust column widths
            TableManager.adjustColumnWidths(serviceOrderTable,
                    10);
        } catch (Exception e) {
            System.err.println(
                    "Erro ao carregar dados da tabela: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar ordens de serviço: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void refreshTable() {
        loadTableData();
    }

    @Override
    public void setObjectToTable(Integer serviceOrderId) {
        try {
            // Get service order data
            ServiceOrder serviceOrder = serviceOrderController.findServiceOrderById(
                    serviceOrderId);
            if (serviceOrder == null) {
                return;
            }
            // Format the date using TableManager.formatDate like in SalesForm
            Object[] rowData = {
                serviceOrder.getId(),
                serviceOrder.getCustomer().getName(),
                serviceOrder.getDescription(),
                TableManager.formatDate(serviceOrder.getCreationDate()), // Format date like in SalesForm
                serviceOrder.getStatus().getDisplayName(),
                "Editar"
            };
            TableManager.addRow(
                    (DefaultTableModel) serviceOrderTable.getModel(),
                    rowData);
        } catch (Exception e) {
            System.err.println(
                    "Erro ao adicionar ordem de serviço à tabela: " + e.getMessage());
            e.printStackTrace(); // Add stack trace for better debugging
        }
    }

    /**
     * Configura o comportamento de seleção da tabela para melhorar a
     * experiência do usuário
     */
    private void configureTableSelectionBehavior() {
        // Permitir seleção de apenas uma linha por vez
        serviceOrderTable.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION);
        // Configurar para que o clique na célula do botão inicie a edição imediatamente
        serviceOrderTable.putClientProperty("terminateEditOnFocusLost",
                Boolean.TRUE);
        // Permitir que a célula do botão seja editável com um único clique
        serviceOrderTable.setEditingColumn(5);
    }

    /**
     * Configura o botão de edição na coluna de ações da tabela.
     */
    private void setupEditButton() {
        // Obter a coluna de ações (última coluna)
        TableColumn actionColumn = serviceOrderTable.getColumnModel().getColumn(
                5);
        // Configurar o renderizador e o editor para a coluna de ações
        actionColumn.setCellRenderer(
                new br.com.devjf.salessync.view.components.table.TableEditButtonRenderer(
                        "Editar"));
        actionColumn.setCellEditor(
                new br.com.devjf.salessync.view.components.table.TableEditButtonEditor(
                        "Editar",
                        row -> editServiceOrder(row)));
        // Definir largura preferencial para a coluna de ações
        actionColumn.setPreferredWidth(80);
        actionColumn.setMaxWidth(100);
        actionColumn.setMinWidth(80);
    }

    /**
     * Abre o formulário de edição para a ordem de serviço selecionada.
     *
     * @param selectedRow O índice da linha selecionada na tabela
     */
    private void editServiceOrder(int selectedRow) {
        try {
            // Obter o ID da ordem de serviço selecionada
            Integer serviceOrderId = (Integer) serviceOrderTable.getValueAt(
                    selectedRow,
                    0);
            System.out.println(
                    "Editando ordem de serviço com ID: " + serviceOrderId);
            // Redirecionar para o painel de edição de ordem de serviço com os dados da ordem selecionada
            ServiceOrder serviceOrder = serviceOrderController.findServiceOrderById(
                    serviceOrderId);
            MainAppView.redirectToPanel(MainAppView.EDIT_SO_PANEL,
                    serviceOrder);
        } catch (HeadlessException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao abrir formulário de edição: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        filterLbl = new javax.swing.JLabel();
        newServiceOrderButton = new javax.swing.JButton();
        cancelServiceOrderButton = new javax.swing.JButton();
        filterPanel = new javax.swing.JPanel();
        statusField = new javax.swing.JTextField();
        statusLbl = new javax.swing.JLabel();
        idLbl = new javax.swing.JLabel();
        idField = new javax.swing.JTextField();
        customerLbl = new javax.swing.JLabel();
        customerField = new javax.swing.JTextField();
        tableScrollPanel = new javax.swing.JScrollPane();
        serviceOrderTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        filterLbl.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        filterLbl.setText("Filtar");

        newServiceOrderButton.setBackground(new java.awt.Color(76, 175, 80));
        newServiceOrderButton.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        newServiceOrderButton.setForeground(new java.awt.Color(255, 255, 255));
        newServiceOrderButton.setText("Nova Ordem");
        newServiceOrderButton.setPreferredSize(new java.awt.Dimension(150, 40));
        ViewComponentStyle.standardCornerRadius(newServiceOrderButton);
        newServiceOrderButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newServiceOrderButtonActionPerformed(evt);
            }
        });

        cancelServiceOrderButton.setBackground(new java.awt.Color(175, 76, 78));
        cancelServiceOrderButton.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        cancelServiceOrderButton.setForeground(new java.awt.Color(255, 255, 255));
        cancelServiceOrderButton.setText("Cancelar Ordem");
        cancelServiceOrderButton.setPreferredSize(new java.awt.Dimension(150, 40));
        ViewComponentStyle.standardCornerRadius(cancelServiceOrderButton);
        cancelServiceOrderButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelServiceOrderButtonActionPerformed(evt);
            }
        });

        filterPanel.setPreferredSize(new java.awt.Dimension(917, 50));
        ViewComponentStyle.standardCornerRadius(filterPanel);

        statusField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        statusField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        statusField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewComponentStyle.standardCornerRadius(statusField);

        statusLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        statusLbl.setText("Status:");

        idLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        idLbl.setText("Código:");

        idField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        idField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        idField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewComponentStyle.standardCornerRadius(idField);

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
                .addGap(128, 128, 128)
                .addComponent(customerLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(customerField, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(statusLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(statusField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(idLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(128, Short.MAX_VALUE))
        );
        filterPanelLayout.setVerticalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(statusLbl)
                        .addComponent(statusField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(idLbl)
                        .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(customerLbl)
                        .addComponent(customerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );

        serviceOrderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Cliente", "Descrição", "Data Criação", "Status", "Ações"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableScrollPanel.setViewportView(serviceOrderTable);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                            .addComponent(cancelServiceOrderButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(newServiceOrderButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(newServiceOrderButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelServiceOrderButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void newServiceOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newServiceOrderButtonActionPerformed
        MainAppView.redirectToPanel(MainAppView.NEW_SO_PANEL);
    }//GEN-LAST:event_newServiceOrderButtonActionPerformed

    private void cancelServiceOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelServiceOrderButtonActionPerformed
        int selectedRow = serviceOrderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma ordem de serviço para cancelar",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        Integer serviceOrderId = (Integer) serviceOrderTable.getValueAt(
                selectedRow,
                0);
        String customerName = (String) serviceOrderTable.getValueAt(selectedRow,
                1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja cancelar a ordem de serviço para o cliente " + customerName + "?",
                "Confirmar Cancelamento",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = serviceOrderController.updateStatus(
                        serviceOrderId,
                        ServiceStatus.CANCELED);
                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Ordem de serviço cancelada com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                    // Registrar atividade
                    if (MainAppView.getInstance() != null) {
                        MainAppView.getInstance().registerUserActivity(
                                "Cancelou a ordem de serviço para o cliente: " + customerName);
                    }
                    // Atualizar a tabela
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Não foi possível cancelar a ordem de serviço",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (HeadlessException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Erro ao cancelar ordem de serviço: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_cancelServiceOrderButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelServiceOrderButton;
    private javax.swing.JTextField customerField;
    private javax.swing.JLabel customerLbl;
    private javax.swing.JLabel filterLbl;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel idLbl;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton newServiceOrderButton;
    private javax.swing.JTable serviceOrderTable;
    private javax.swing.JTextField statusField;
    private javax.swing.JLabel statusLbl;
    private javax.swing.JScrollPane tableScrollPanel;
    // End of variables declaration//GEN-END:variables
}
