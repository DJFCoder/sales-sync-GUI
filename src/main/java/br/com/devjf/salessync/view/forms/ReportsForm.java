package br.com.devjf.salessync.view.forms;

import br.com.devjf.salessync.controller.ReportController;
import br.com.devjf.salessync.controller.SaleController;
import br.com.devjf.salessync.controller.ExpenseController;
import br.com.devjf.salessync.model.Sale;
import br.com.devjf.salessync.model.Expense;
import br.com.devjf.salessync.model.ExpenseCategory;
import br.com.devjf.salessync.view.components.style.ViewComponentStyle;
import br.com.devjf.salessync.view.components.table.TableFormInterface;
import br.com.devjf.salessync.view.components.table.TableManager;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.SwingUtilities;
import java.text.DecimalFormat;
import java.util.List;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

public class ReportsForm extends javax.swing.JFrame implements TableFormInterface {
    private static final Logger LOGGER = Logger.getLogger(
            ReportsForm.class.getName());
    private ReportController reportController;
    private SaleController saleController;
    private ExpenseController expenseController;
    private Timer refreshTimer;
    private static ReportsForm instance;

    public ReportsForm() {
        initComponents();
        instance = this;
        this.reportController = new ReportController();
        this.saleController = new SaleController();
        this.expenseController = new ExpenseController();
        setupTableColumns();
        setupTableFilters();
        setupCategoryComboBox();
        loadTableData();
        setLabelValues();
    }

    public static ReportsForm getInstance() {
        return instance;
    }

//    private void setupPeriodicRefresh() {
//        // Atualiza os valores a cada 5 segundos
//        refreshTimer = new Timer(5000,
//                e -> updateReportValues());
//        refreshTimer.start();
//    }
    public void updateReportValues() {
        SwingUtilities.invokeLater(() -> {
            try {
                setLabelValues();
                loadTableData();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE,
                        "Erro ao atualizar valores do relatório",
                        e);
            }
        });
    }

    private void setLabelValues() {
        // Formatação consistente dos valores monetários
        DecimalFormat df = new DecimalFormat("#,##0.00");
        salesValueLbl.setText(
                "R$ " + df.format(reportController.getTotalSales()));
        expensesValueLbl.setText(
                "R$ " + df.format(reportController.getTotalExpenses()));
        profitValueLbl.setText(
                "R$ " + df.format(reportController.getCurrentGrossProfit()));
        taxesValueLbl.setText(
                "R$ " + df.format(reportController.getTaxes(
                        ReportController.BusinessType.PRODUCT_SALES)));
        amountValueLbl.setText(
                "R$ " + df.format(reportController.getCurrentNetProfit(
                        ReportController.BusinessType.PRODUCT_SALES)));
    }

    @Override
    public void setupTableColumns() {
        String[] columnNames = {
            "Vendas", "Forma de Pagamento", "Despesas", "Recorrência", "Categoria"
        };
        Class<?>[] columnClasses = {
            String.class, String.class, String.class, String.class, String.class
        };
        boolean[] editableColumns = {
            false, false, false, false, false
        };
        DefaultTableModel model = new DefaultTableModel(columnNames,
                0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnClasses[columnIndex];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return editableColumns[column];
            }
        };
        expenseTable.setModel(model);
    }

    private void setupTableFilters() {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(
                (DefaultTableModel) expenseTable.getModel());
        expenseTable.setRowSorter(sorter);
        // Listener for payment method filter
        paymentMethodCmb.addActionListener(e -> {
            String selectedPaymentMethod = (String) paymentMethodCmb.getSelectedItem();
            if (selectedPaymentMethod != null && !selectedPaymentMethod.isEmpty() && !selectedPaymentMethod.equals(
                    "Selecione")) {
                sorter.setRowFilter(RowFilter.regexFilter(
                        "(?i)" + selectedPaymentMethod,
                        1));
            } else {
                sorter.setRowFilter(null);
            }
        });
        // Listener for recurrence filter
        recurrenceCmb.addActionListener(e -> {
            String selectedRecurrence = (String) recurrenceCmb.getSelectedItem();
            if (selectedRecurrence != null && !selectedRecurrence.isEmpty() && !selectedRecurrence.equals(
                    "Selecione")) {
                sorter.setRowFilter(RowFilter.regexFilter(
                        "(?i)" + selectedRecurrence,
                        3));
            } else {
                sorter.setRowFilter(null);
            }
        });
        // Listener for category filter
        categoryCmb.addActionListener(e -> {
            String selectedCategory = (String) categoryCmb.getSelectedItem();
            if (selectedCategory != null && !selectedCategory.isEmpty() && !selectedCategory.equals(
                    "Selecione")) {
                sorter.setRowFilter(RowFilter.regexFilter(
                        "(?i)" + selectedCategory,
                        4));
            } else {
                sorter.setRowFilter(null);
            }
        });
    }

    private void setupCategoryComboBox() {
        // Clear existing items
        categoryCmb.removeAllItems();
        // Add default "Selecione" item
        categoryCmb.addItem("Selecione");
        // Fetch all expense categories
        List<ExpenseCategory> categories = expenseController.listAllCategories();
        // Add each category name to the combo box
        for (ExpenseCategory category : categories) {
            categoryCmb.addItem(category.getName());
        }
    }

    @Override
    public void loadTableData() {
        DefaultTableModel model = (DefaultTableModel) expenseTable.getModel();
        model.setRowCount(0); // Clear existing rows
        // Fetch sales data
        List<Sale> sales = saleController.listAllSales();
        // Fetch expenses data
        List<Expense> expenses = expenseController.listExpenses(new HashMap<>());
        // Combine sales and expenses in the table
        for (int i = 0; i < Math.max(sales.size(),
                expenses.size()); i++) {
            Object[] rowData = new Object[5];
            // Sales data
            if (i < sales.size()) {
                Sale sale = sales.get(i);
                rowData[0] = TableManager.formatCurrency(sale.getTotalAmount()); // Vendas
                rowData[1] = sale.getPaymentMethod().toString(); // Forma de Pagamento
            }
            // Expenses data
            if (i < expenses.size()) {
                Expense expense = expenses.get(i);
                rowData[2] = TableManager.formatCurrency(expense.getAmount()); // Despesas
                rowData[3] = expense.getRecurrence().getDisplayName(); // Recorrência
                rowData[4] = expense.getCategory().getName(); // Categoria
            }
            model.addRow(rowData);
        }
    }

    @Override
    public void refreshTable() {
        loadTableData();
    }

    @Override
    public void setObjectToTable(Integer id) {
        // Not applicable for this view
    }

    @Override
    public void dispose() {
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
        super.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        filterLbl = new javax.swing.JLabel();
        newReportBtn = new javax.swing.JButton();
        filterReportBtn = new javax.swing.JButton();
        filterPanel = new javax.swing.JPanel();
        categoryLbl = new javax.swing.JLabel();
        recurrenceLbl = new javax.swing.JLabel();
        paymentMethodLbl = new javax.swing.JLabel();
        paymentMethodCmb = new javax.swing.JComboBox<>();
        categoryCmb = new javax.swing.JComboBox<>();
        recurrenceCmb = new javax.swing.JComboBox<>();
        tableScrollPanel = new javax.swing.JScrollPane();
        expenseTable = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        resumeTitleLbl = new javax.swing.JLabel();
        totalSalesLbl = new javax.swing.JLabel();
        totalExpensesLbl = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        salesValueLbl = new javax.swing.JLabel();
        expensesValueLbl = new javax.swing.JLabel();
        estimatedTaxesLbl = new javax.swing.JLabel();
        grossProfitLbl = new javax.swing.JLabel();
        profitValueLbl = new javax.swing.JLabel();
        taxesValueLbl = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        clearAmountLbl = new javax.swing.JLabel();
        amountValueLbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        filterLbl.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        filterLbl.setText("Filtar");

        newReportBtn.setBackground(new java.awt.Color(76, 175, 80));
        newReportBtn.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        newReportBtn.setForeground(new java.awt.Color(255, 255, 255));
        newReportBtn.setText("Emitir Relatório");
        newReportBtn.setPreferredSize(new java.awt.Dimension(150, 40));
        ViewComponentStyle.standardCornerRadius(newReportBtn);
        newReportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newReportBtnActionPerformed(evt);
            }
        });

        filterReportBtn.setBackground(new java.awt.Color(255, 178, 0));
        filterReportBtn.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        filterReportBtn.setForeground(new java.awt.Color(255, 255, 255));
        filterReportBtn.setText("Relatório Filtrado");
        filterReportBtn.setToolTipText("");
        filterReportBtn.setPreferredSize(new java.awt.Dimension(170, 40));
        filterReportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterReportBtnActionPerformed(evt);
            }
        });
        ViewComponentStyle.standardCornerRadius(filterReportBtn);

        filterPanel.setPreferredSize(new java.awt.Dimension(917, 50));
        ViewComponentStyle.standardCornerRadius(filterPanel);

        categoryLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        categoryLbl.setText("Categoria:");

        recurrenceLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        recurrenceLbl.setText("Recorência:");

        paymentMethodLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        paymentMethodLbl.setText("Forma de Pagamento:");

        paymentMethodCmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "DINHEIRO", "CRÉDITO", "DÉBITO", "TRANSFERÊNCIA", "PIX", "BOLETO", "CHEQUE" }));
        paymentMethodCmb.setPreferredSize(new java.awt.Dimension(140, 30));

        categoryCmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "DIÁRIA", "SEMANAL", "MENSAL", "ANUAL" }));
        categoryCmb.setPreferredSize(new java.awt.Dimension(140, 30));

        recurrenceCmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "Diária", "Semanal", "Mensal", "Anual" }));
        recurrenceCmb.setPreferredSize(new java.awt.Dimension(140, 30));

        javax.swing.GroupLayout filterPanelLayout = new javax.swing.GroupLayout(filterPanel);
        filterPanel.setLayout(filterPanelLayout);
        filterPanelLayout.setHorizontalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addComponent(paymentMethodLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(paymentMethodCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(recurrenceLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(recurrenceCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(categoryLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(categoryCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );
        filterPanelLayout.setVerticalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(categoryLbl)
                    .addComponent(paymentMethodCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paymentMethodLbl)
                    .addComponent(recurrenceLbl)
                    .addComponent(categoryCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recurrenceCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        tableScrollPanel.setPreferredSize(new java.awt.Dimension(917, 180));

        expenseTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Vendas", "Forma de Pagamento", "Despesas", "Recorrência", "Categoria"
            }
        ));
        tableScrollPanel.setViewportView(expenseTable);

        jPanel1.setPreferredSize(new java.awt.Dimension(575, 270));
        ViewComponentStyle.standardCornerRadius(jPanel1);

        resumeTitleLbl.setFont(new java.awt.Font("Liberation Sans", 0, 16)); // NOI18N
        resumeTitleLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        resumeTitleLbl.setText("Resumo Financeiro");

        totalSalesLbl.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        totalSalesLbl.setText("Total de Vendas:");

        totalExpensesLbl.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        totalExpensesLbl.setText("Total de Despesas:");

        salesValueLbl.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        salesValueLbl.setText("R$ ");

        expensesValueLbl.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        expensesValueLbl.setText("R$ ");

        estimatedTaxesLbl.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        estimatedTaxesLbl.setText("Impostos Estimados:");

        grossProfitLbl.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        grossProfitLbl.setText("Lucro Bruto:");

        profitValueLbl.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        profitValueLbl.setText("R$ ");

        taxesValueLbl.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        taxesValueLbl.setText("R$ ");

        clearAmountLbl.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        clearAmountLbl.setText("Lucro Liquido:");

        amountValueLbl.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        amountValueLbl.setText("R$ ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(135, 135, 135)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(estimatedTaxesLbl)
                            .addComponent(grossProfitLbl)
                            .addComponent(totalExpensesLbl)
                            .addComponent(totalSalesLbl)
                            .addComponent(clearAmountLbl))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(amountValueLbl, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(taxesValueLbl, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(profitValueLbl, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(expensesValueLbl, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(salesValueLbl, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resumeTitleLbl))
                .addGap(140, 140, 140))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(resumeTitleLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalSalesLbl)
                    .addComponent(salesValueLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalExpensesLbl)
                    .addComponent(expensesValueLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(grossProfitLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(estimatedTaxesLbl))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(profitValueLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(taxesValueLbl)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearAmountLbl)
                    .addComponent(amountValueLbl))
                .addContainerGap(54, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                            .addComponent(filterReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(newReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(tableScrollPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 917, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(filterPanel, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 916, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterLbl, javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(tableScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
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

    private void filterReportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterReportBtnActionPerformed
        JOptionPane.showMessageDialog(ReportsForm.this,
                "Função em desenvolvimento",
                "Em breve",
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_filterReportBtnActionPerformed

    private void newReportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newReportBtnActionPerformed
        JOptionPane.showMessageDialog(ReportsForm.this,
                "Função em desenvolvimento",
                "Em breve",
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_newReportBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel amountValueLbl;
    private javax.swing.JComboBox<String> categoryCmb;
    private javax.swing.JLabel categoryLbl;
    private javax.swing.JLabel clearAmountLbl;
    private javax.swing.JLabel estimatedTaxesLbl;
    private javax.swing.JTable expenseTable;
    private javax.swing.JLabel expensesValueLbl;
    private javax.swing.JLabel filterLbl;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JButton filterReportBtn;
    private javax.swing.JLabel grossProfitLbl;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton newReportBtn;
    private javax.swing.JComboBox<String> paymentMethodCmb;
    private javax.swing.JLabel paymentMethodLbl;
    private javax.swing.JLabel profitValueLbl;
    private javax.swing.JComboBox<String> recurrenceCmb;
    private javax.swing.JLabel recurrenceLbl;
    private javax.swing.JLabel resumeTitleLbl;
    private javax.swing.JLabel salesValueLbl;
    private javax.swing.JScrollPane tableScrollPanel;
    private javax.swing.JLabel taxesValueLbl;
    private javax.swing.JLabel totalExpensesLbl;
    private javax.swing.JLabel totalSalesLbl;
    // End of variables declaration//GEN-END:variables
}
