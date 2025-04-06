package br.com.devjf.salessync.view.forms;

import br.com.devjf.salessync.view.components.style.ViewComponentStyle;
import br.com.devjf.salessync.controller.ExpenseController;
import br.com.devjf.salessync.model.Expense;
import br.com.devjf.salessync.view.MainAppView;
import br.com.devjf.salessync.view.components.table.TableEditButtonEditor;
import br.com.devjf.salessync.view.components.table.TableEditButtonRenderer;
import br.com.devjf.salessync.view.components.table.TableFormInterface;
import br.com.devjf.salessync.view.components.table.TableManager;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class ExpensesForm extends javax.swing.JFrame implements TableFormInterface {
    private static ExpensesForm instance;
    private final ExpenseController expenseController;

    public ExpensesForm() {
        initComponents();
        instance = this;
        this.expenseController = new ExpenseController();
        setupTableColumns();
        TableManager.setupFilters(categoryField,
                descriptionField,
                valueField,
                expenseTable);
        loadTableData();
    }

    public static ExpensesForm getInstance() {
        return instance;
    }

    private void configureTableSelectionBehavior() {
        expenseTable.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION);
        expenseTable.putClientProperty("terminateEditOnFocusLost",
                Boolean.TRUE);
        // Use the last column index (5) instead of 6
        int actionColumnIndex = expenseTable.getColumnCount() - 1;
        System.err.println(
                "Configurando coluna de ações no índice: " + actionColumnIndex);
        try {
            expenseTable.setEditingColumn(actionColumnIndex);
        } catch (Exception e) {
            System.err.println("Erro ao configurar coluna de edição:");
            e.printStackTrace();
            // Log column details for debugging
            System.err.println("Detalhes das Colunas:");
            for (int i = 0; i < expenseTable.getColumnCount(); i++) {
                System.err.printf("Coluna %d: Nome='%s'%n",
                        i,
                        expenseTable.getColumnName(i));
            }
        }
    }

    private void setupEditButton() {
        TableColumn actionColumn = expenseTable.getColumnModel().getColumn(5);
        actionColumn.setCellRenderer(new TableEditButtonRenderer("Editar"));
        actionColumn.setCellEditor(new TableEditButtonEditor("Editar",
                row -> editExpense(row)));
        actionColumn.setPreferredWidth(80);
        actionColumn.setMaxWidth(100);
        actionColumn.setMinWidth(80);
    }

    private void editExpense(int selectedRow) {
        try {
            Integer expenseId = (Integer) expenseTable.getValueAt(selectedRow,
                    0);
            System.out.println("Editando despesa com ID: " + expenseId);
            MainAppView.redirectToPanel(MainAppView.EDIT_EXPENSE_PANEL,
                    expenseId);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao abrir formulário de edição: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void setupTableColumns() {
        String[] columnNames = {
            "Código", "Descrição", "Categoria", "Data", "Valor", "Ações"
        };
        Class<?>[] columnClasses = {
            Integer.class, String.class, String.class, String.class, String.class, String.class
        };
        boolean[] editableColumns = {
            false, false, false, false, false, true
        };
        try {
            DefaultTableModel model = TableManager.setupTableModel(expenseTable,
                    columnNames,
                    columnClasses,
                    editableColumns);
        } catch (Exception e) {
            System.err.println("ERRO FATAL ao configurar modelo de tabela:");
            e.printStackTrace();
        }
        setupEditButton();
        configureTableSelectionBehavior();
    }

    @Override
    public void loadTableData() {
        try {
            DefaultTableModel model = (DefaultTableModel) expenseTable.getModel();
            TableManager.clearTable(model);
            // Detailed logging for debugging
            System.err.println(
                    "Iniciando carregamento de dados da tabela de despesas");
            Map<String, Object> filters = new HashMap<>();
            String categoryFilter = categoryField.getText().trim();
            String descriptionFilter = descriptionField.getText().trim();
            String valueFilter = valueField.getText().trim();
            if (!categoryFilter.isEmpty()) {
                filters.put("categoryName",
                        categoryFilter);
            }
            if (!descriptionFilter.isEmpty()) {
                filters.put("description",
                        descriptionFilter);
            }
            if (!valueFilter.isEmpty()) {
                filters.put("value",
                        valueFilter);
            }
            List<Expense> expenses = expenseController.listExpenses(filters);
            for (Expense expense : expenses) {
                model.addRow(new Object[]{
                    expense.getId(),
                    expense.getDescription(),
                    expense.getCategory() != null ? expense.getCategory().getName() : "Sem Categoria",
                    expense.getDate() != null ? TableManager.formatDate(expense.getDate()) : "Data Inválida",
                    expense.getAmount() != null ? TableManager.formatCurrency(expense.getAmount()) : "R$ 0,00",
                    "Editar" // Action column
                });
            }
            TableManager.adjustColumnWidths(expenseTable,
                    10);
        } catch (Exception e) {
            System.err.println(
                    "Erro ao carregar dados da tabela: " + e.getMessage());
            e.printStackTrace();  // Print full stack trace for more details
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar despesas: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void refreshTable() {
        loadTableData();
    }

    @Override
    public void setObjectToTable(Integer id) {
        try {
            Expense expense = expenseController.findExpenseById(id);
            if (expense == null) {
                System.err.println("Despesa não encontrada para ID: " + id);
                return;
            }
            // Extensive null checks
            if (expense.getCategory() == null) {
                System.err.println(
                        "Categoria da despesa não pode ser nula: " + id);
                return;
            }
            Object[] rowData = {
                expense.getId(),
                expense.getDescription(),
                expense.getCategory().getName(),
                TableManager.formatDate(expense.getDate()),
                TableManager.formatCurrency(expense.getAmount()),
                "Editar"
            };
            // Validate row data with detailed logging
            for (int i = 0; i < rowData.length; i++) {
                if (rowData[i] == null) {
                    System.err.println("Dado nulo na coluna " + i);
                }
            }
            // Get current table model for logging
            DefaultTableModel model = (DefaultTableModel) expenseTable.getModel();
            System.out.println("Modelo da tabela antes de adicionar:");
            System.out.println("Número de colunas: " + model.getColumnCount());
            TableManager.addRow(model,
                    rowData);
        } catch (Exception e) {
            System.err.println(
                    "Erro ao adicionar despesa à tabela: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="GeneratedCode">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        filterLbl = new javax.swing.JLabel();
        newExpenseBtn = new javax.swing.JButton();
        manageCategorysBtn = new javax.swing.JButton();
        filterPanel = new javax.swing.JPanel();
        descriptionField = new javax.swing.JTextField();
        descriptionLbl = new javax.swing.JLabel();
        valueLbl = new javax.swing.JLabel();
        valueField = new javax.swing.JTextField();
        categoryLbl = new javax.swing.JLabel();
        categoryField = new javax.swing.JTextField();
        tableScrollPanel = new javax.swing.JScrollPane();
        expenseTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        filterLbl.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        filterLbl.setText("Filtar");

        newExpenseBtn.setBackground(new java.awt.Color(76, 175, 80));
        newExpenseBtn.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        newExpenseBtn.setForeground(new java.awt.Color(255, 255, 255));
        newExpenseBtn.setText("Nova Despesa");
        newExpenseBtn.setPreferredSize(new java.awt.Dimension(150, 40));
        ViewComponentStyle.standardCornerRadius(newExpenseBtn);
        newExpenseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newExpenseBtnActionPerformed(evt);
            }
        });

        manageCategorysBtn.setBackground(new java.awt.Color(255, 178, 0));
        manageCategorysBtn.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        manageCategorysBtn.setForeground(new java.awt.Color(255, 255, 255));
        manageCategorysBtn.setText("Gerenciar Categorias");
        manageCategorysBtn.setPreferredSize(new java.awt.Dimension(170, 40));
        manageCategorysBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageCategorysBtnActionPerformed(evt);
            }
        });
        ViewComponentStyle.standardCornerRadius(manageCategorysBtn);

        filterPanel.setPreferredSize(new java.awt.Dimension(917, 50));
        ViewComponentStyle.standardCornerRadius(filterPanel);

        descriptionField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        descriptionField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        descriptionField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewComponentStyle.standardCornerRadius(descriptionField);

        descriptionLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        descriptionLbl.setText("Descrição:");

        valueLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        valueLbl.setText("Valor:");

        valueField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        valueField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        valueField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewComponentStyle.standardCornerRadius(valueField);

        categoryLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        categoryLbl.setText("Categoria:");

        categoryField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        categoryField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        categoryField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewComponentStyle.standardCornerRadius(categoryField);

        javax.swing.GroupLayout filterPanelLayout = new javax.swing.GroupLayout(filterPanel);
        filterPanel.setLayout(filterPanelLayout);
        filterPanelLayout.setHorizontalGroup(
                filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(filterPanelLayout.createSequentialGroup()
                                .addGap(113, 113, 113)
                                .addComponent(categoryLbl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(categoryField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(descriptionLbl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(descriptionField, javax.swing.GroupLayout.PREFERRED_SIZE, 205,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(valueLbl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(valueField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(114, Short.MAX_VALUE)));
        filterPanelLayout.setVerticalGroup(
                filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(filterPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(filterPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(filterPanelLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(valueLbl)
                                                .addComponent(valueField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(filterPanelLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(categoryLbl)
                                                .addComponent(categoryField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(filterPanelLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(descriptionLbl)
                                                        .addComponent(descriptionField,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(10, 10, 10)));

        expenseTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "Código", "Descrição", "Categoria", "Data", "Valor", "Ações"
                }) {
            Class[] types = new Class[] {
                    java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                    java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        tableScrollPanel.setViewportView(expenseTable);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(mainPanelLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                        mainPanelLayout.createSequentialGroup()
                                                                .addComponent(manageCategorysBtn,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(newExpenseBtn,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(tableScrollPanel,
                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 917,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(filterPanel, javax.swing.GroupLayout.Alignment.CENTER,
                                                javax.swing.GroupLayout.PREFERRED_SIZE, 916,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(filterLbl, javax.swing.GroupLayout.Alignment.CENTER))
                                .addContainerGap(19, Short.MAX_VALUE)));
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(filterLbl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(filterPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(tableScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 441,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(mainPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(newExpenseBtn, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(manageCategorysBtn, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(69, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap()));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void manageCategorysBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_manageCategorysBtnActionPerformed
        JOptionPane.showMessageDialog(ExpensesForm.this,
                "Função em desenvolvimento",
                "Em breve",
                JOptionPane.INFORMATION_MESSAGE);
    }// GEN-LAST:event_manageCategorysBtnActionPerformed

    private void newExpenseBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_newExpenseBtnActionPerformed
        try {
            MainAppView.redirectToPanel(MainAppView.NEW_EXPENSE_PANEL);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ExpensesForm.this,
                    "Erro ao abrir formulário de novo cliente: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_newExpenseBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField categoryField;
    private javax.swing.JLabel categoryLbl;
    private javax.swing.JTextField descriptionField;
    private javax.swing.JLabel descriptionLbl;
    private javax.swing.JTable expenseTable;
    private javax.swing.JLabel filterLbl;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton manageCategorysBtn;
    private javax.swing.JButton newExpenseBtn;
    private javax.swing.JScrollPane tableScrollPanel;
    private javax.swing.JTextField valueField;
    private javax.swing.JLabel valueLbl;
    // End of variables declaration//GEN-END:variables
}
