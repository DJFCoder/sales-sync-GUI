package br.com.devjf.salessync.view.forms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import br.com.devjf.salessync.controller.SaleController;
import br.com.devjf.salessync.model.Sale;
import br.com.devjf.salessync.view.MainAppView;
import br.com.devjf.salessync.view.components.style.ViewComponentStyle;
import br.com.devjf.salessync.view.components.table.TableEditButtonEditor;
import br.com.devjf.salessync.view.components.table.TableEditButtonRenderer;
import br.com.devjf.salessync.view.components.table.TableFormInterface;
import br.com.devjf.salessync.view.components.table.TableManager;
import java.awt.HeadlessException;
import javax.swing.JTable;

public class SalesForm extends javax.swing.JFrame implements TableFormInterface {
    private static SalesForm instance;
    private final SaleController saleController;

    public SalesForm() {
        initComponents();
        instance = this;
        this.saleController = new SaleController();
        // Configurar a tabela
        setupTableColumns();
        // Configurar filtros
        TableManager.setupFilters(customerField,
                dateField,
                paymentMethodField,
                salesTable);
        // Carregar dados iniciais
        loadTableData();
    }

    public static SalesForm getInstance() {
        return instance;
    }

    /**
     * Adiciona uma venda à tabela.
     *
     * @param saleId O ID da venda a ser adicionada
     */
    @Override
    public void setObjectToTable(Integer saleId) {
        try {
            // Get sale data
            Object[] rowData = {
                saleId,
                saleController.getCustomerName(saleId),
                TableManager.formatDate(saleController.getSaleDate(saleId)),
                saleController.getPaymentMethodSafe(saleId),
                saleController.getPaymentDate(saleId) != null
                ? TableManager.formatDate(saleController.getPaymentDate(saleId)) : "",
                TableManager.formatCurrency(
                saleController.getTotalAmount(saleId)),
                "Editar"
            };
            TableManager.addRow((DefaultTableModel) salesTable.getModel(),
                    rowData);
        } catch (Exception e) {
            System.err.println(
                    "Erro ao adicionar venda à tabela: " + e.getMessage());
        }
    }

    @Override
    public void loadTableData() {
        try {
            // Clear table
            TableManager.clearTable((DefaultTableModel) salesTable.getModel());
            // Get filter values
            Map<String, Object> filters = new HashMap<>();
            String customerFilter = customerField.getText().trim();
            String dateFilter = dateField.getText().trim();
            String paymentMethodFilter = paymentMethodField.getText().trim();
            // Add filters if they are not empty
            if (!customerFilter.isEmpty()) {
                filters.put("customerName",
                        customerFilter);
            }
            if (!dateFilter.isEmpty()) {
                filters.put("saleDate",
                        dateFilter);
            }
            if (!paymentMethodFilter.isEmpty()) {
                filters.put("paymentMethod",
                        paymentMethodFilter);
            }
            // Get sales through controller
            List<Sale> sales = saleController.listSales(filters);
            // Add sales to table
            for (Sale sale : sales) {
                setObjectToTable(sale.getId());
            }
            // Adjust column widths
            TableManager.adjustColumnWidths(salesTable,
                    10);
        } catch (Exception e) {
            System.err.println(
                    "Erro ao carregar dados da tabela: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar vendas: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Configura as colunas da tabela de vendas.
     */
    @Override
    public void setupTableColumns() {
        // Definir nomes das colunas
        String[] columnNames = {
            "Código", "Cliente", "Data Venda", "Forma de Pagamento", "Data Pagamento", "Valor Total", "Ações"
        };
        // Definir classes das colunas
        Class<?>[] columnClasses = {
            Integer.class, String.class, String.class, String.class, String.class, String.class, String.class
        };
        // Definir quais colunas são editáveis
        boolean[] editableColumns = {
            false, false, false, false, false, false, true
        };
        // Configurar o modelo da tabela
        TableManager.setupTableModel(salesTable,
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
        salesTable.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION);
        // Configurar para que o clique na célula do botão inicie a edição imediatamente
        salesTable.putClientProperty("terminateEditOnFocusLost",
                Boolean.TRUE);
        // Permitir que a célula do botão seja editável com um único clique
        salesTable.setEditingColumn(6);
    }

    /**
     * Configura o botão de edição na coluna de ações da tabela.
     */
    private void setupEditButton() {
        // Obter a coluna de ações (última coluna)
        TableColumn actionColumn = salesTable.getColumnModel().getColumn(6);
        // Configurar o renderizador e o editor para a coluna de ações
        actionColumn.setCellRenderer(new TableEditButtonRenderer("Editar"));
        actionColumn.setCellEditor(new TableEditButtonEditor("Editar",
                row -> editSale(row)));
        // Definir largura preferencial para a coluna de ações
        actionColumn.setPreferredWidth(80);
        actionColumn.setMaxWidth(100);
        actionColumn.setMinWidth(80);
    }

    /**
     * Abre o formulário de edição para a venda selecionada.
     *
     * @param selectedRow O índice da linha selecionada na tabela
     */
    private void editSale(int selectedRow) {
        try {
            // Obter o ID da venda selecionada
            Integer saleId = (Integer) salesTable.getValueAt(selectedRow,
                    0);
            System.out.println("Editando venda com ID: " + saleId);
            // Redirecionar para o painel de edição de venda com os dados da venda selecionada
            MainAppView.redirectToPanel(MainAppView.EDIT_SALE_PANEL,
                    saleId);
            // Não atualizar a tabela aqui, pois a venda ainda não foi salva
        } catch (HeadlessException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao abrir formulário de edição: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método público para atualizar a tabela de vendas. Pode ser chamado de
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
        newSaleButton = new javax.swing.JButton();
        deleteSaleButton = new javax.swing.JButton();
        filterPanel = new javax.swing.JPanel();
        dateField = new javax.swing.JTextField();
        dateLbl = new javax.swing.JLabel();
        paymentMethodLbl = new javax.swing.JLabel();
        paymentMethodField = new javax.swing.JTextField();
        customerLbl = new javax.swing.JLabel();
        customerField = new javax.swing.JTextField();
        tableScrollPanel = new javax.swing.JScrollPane();
        salesTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        filterLbl.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        filterLbl.setText("Filtar");

        newSaleButton.setBackground(new java.awt.Color(76, 175, 80));
        newSaleButton.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        newSaleButton.setForeground(new java.awt.Color(255, 255, 255));
        newSaleButton.setText("Nova Venda");
        newSaleButton.setPreferredSize(new java.awt.Dimension(150, 40));
        ViewComponentStyle.standardCornerRadius(newSaleButton);
        newSaleButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newSaleButtonActionPerformed(evt);
            }
        });

        deleteSaleButton.setBackground(new java.awt.Color(175, 76, 78));
        deleteSaleButton.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        deleteSaleButton.setForeground(new java.awt.Color(255, 255, 255));
        deleteSaleButton.setText("Excluir Venda");
        deleteSaleButton.setPreferredSize(new java.awt.Dimension(150, 40));
        ViewComponentStyle.standardCornerRadius(deleteSaleButton);
        deleteSaleButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSaleButtonActionPerformed(evt);
            }
        });

        filterPanel.setPreferredSize(new java.awt.Dimension(917, 50));
        ViewComponentStyle.standardCornerRadius(filterPanel);

        dateField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        dateField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        dateField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewComponentStyle.standardCornerRadius(dateField);

        dateLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        dateLbl.setText("Data:");

        paymentMethodLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        paymentMethodLbl.setText("Forma de Pagamento:");

        paymentMethodField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        paymentMethodField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        paymentMethodField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewComponentStyle.standardCornerRadius(paymentMethodField);

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
                .addGap(83, 83, 83)
                .addComponent(customerLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(customerField, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(dateLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(paymentMethodLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(paymentMethodField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );
        filterPanelLayout.setVerticalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(paymentMethodLbl)
                        .addComponent(paymentMethodField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(dateLbl)
                        .addComponent(dateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(customerLbl)
                        .addComponent(customerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );

        salesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Cliente", "Data Venda", "Forma de Pagamento", "Data Pagamento", "Valor Total", "Ações"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableScrollPanel.setViewportView(salesTable);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                            .addComponent(deleteSaleButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(newSaleButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(newSaleButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteSaleButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void newSaleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newSaleButtonActionPerformed
        try {
            MainAppView.redirectToPanel(MainAppView.NEW_SALE_PANEL);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao abrir formulário de nova venda: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_newSaleButtonActionPerformed

    private void deleteSaleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSaleButtonActionPerformed
        // Get selected row
        int selectedRow = salesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma venda para excluir",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Confirmar exclusão
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir esta venda?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Get the sale ID before any operations
            Integer saleId = (Integer) salesTable.getValueAt(selectedRow,
                    0);
            // Store the table model before deletion
            DefaultTableModel model = (DefaultTableModel) salesTable.getModel();
            boolean success = saleController.deleteSale(saleId);
            if (!success) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir a venda",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
            // Log the activity before removing the row
            MainAppView.getInstance().registerUserActivity(
                    "Excluiu a venda ID:" + saleId);
            // Remove the row directly from the model
            model.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this,
                    "Venda excluída com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            // Refresh the table if needed
            refreshTable();
        }
    }//GEN-LAST:event_deleteSaleButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField customerField;
    private javax.swing.JLabel customerLbl;
    private javax.swing.JTextField dateField;
    private javax.swing.JLabel dateLbl;
    private javax.swing.JButton deleteSaleButton;
    private javax.swing.JLabel filterLbl;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton newSaleButton;
    private javax.swing.JTextField paymentMethodField;
    private javax.swing.JLabel paymentMethodLbl;
    private javax.swing.JTable salesTable;
    private javax.swing.JScrollPane tableScrollPanel;
    // End of variables declaration//GEN-END:variables
}
