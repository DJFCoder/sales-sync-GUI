package br.com.devjf.salessync.view.forms;

public class ReportsForm extends javax.swing.JFrame {
    public ReportsForm() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        filterLbl = new javax.swing.JLabel();
        newExpenseButton = new javax.swing.JButton();
        manageCategorysButton = new javax.swing.JButton();
        filterPanel = new javax.swing.JPanel();
        descriptionField = new javax.swing.JTextField();
        descriptionLbl = new javax.swing.JLabel();
        valueLbl = new javax.swing.JLabel();
        valueField = new javax.swing.JTextField();
        categoryLbl = new javax.swing.JLabel();
        categoryField = new javax.swing.JTextField();
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

        newExpenseButton.setBackground(new java.awt.Color(76, 175, 80));
        newExpenseButton.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        newExpenseButton.setForeground(new java.awt.Color(255, 255, 255));
        newExpenseButton.setText("Nova Despesa");
        newExpenseButton.setPreferredSize(new java.awt.Dimension(150, 40));

        manageCategorysButton.setBackground(new java.awt.Color(255, 178, 0));
        manageCategorysButton.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        manageCategorysButton.setForeground(new java.awt.Color(255, 255, 255));
        manageCategorysButton.setText("Gerenciar Categorias");
        manageCategorysButton.setPreferredSize(new java.awt.Dimension(170, 40));
        manageCategorysButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageCategorysButtonActionPerformed(evt);
            }
        });

        filterPanel.setPreferredSize(new java.awt.Dimension(917, 50));

        descriptionField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        descriptionField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        descriptionField.setPreferredSize(new java.awt.Dimension(121, 30));

        descriptionLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        descriptionLbl.setText("Descrição:");

        valueLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        valueLbl.setText("Valor:");

        valueField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        valueField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        valueField.setPreferredSize(new java.awt.Dimension(121, 30));

        categoryLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        categoryLbl.setText("Categoria:");

        categoryField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        categoryField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        categoryField.setPreferredSize(new java.awt.Dimension(121, 30));

        javax.swing.GroupLayout filterPanelLayout = new javax.swing.GroupLayout(filterPanel);
        filterPanel.setLayout(filterPanelLayout);
        filterPanelLayout.setHorizontalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addGap(113, 113, 113)
                .addComponent(categoryLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(categoryField, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(descriptionLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(descriptionField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(valueLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(valueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(114, Short.MAX_VALUE))
        );
        filterPanelLayout.setVerticalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(descriptionLbl)
                        .addComponent(descriptionField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(valueLbl)
                        .addComponent(valueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(categoryLbl)
                        .addComponent(categoryField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );

        tableScrollPanel.setPreferredSize(new java.awt.Dimension(917, 180));

        expenseTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Descrição", "Categoria", "Data", "Valor", "Ações"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableScrollPanel.setViewportView(expenseTable);

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));
        jPanel1.setPreferredSize(new java.awt.Dimension(575, 270));

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
                .addContainerGap(50, Short.MAX_VALUE))
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
                            .addComponent(manageCategorysButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(newExpenseButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(tableScrollPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 917, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(filterPanel, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 916, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.CENTER, mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(filterLbl)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addComponent(newExpenseButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(manageCategorysButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void manageCategorysButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageCategorysButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_manageCategorysButtonActionPerformed
    
    // new ReportsForm().setVisible(true);

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel amountValueLbl;
    private javax.swing.JTextField categoryField;
    private javax.swing.JLabel categoryLbl;
    private javax.swing.JLabel clearAmountLbl;
    private javax.swing.JTextField descriptionField;
    private javax.swing.JLabel descriptionLbl;
    private javax.swing.JLabel estimatedTaxesLbl;
    private javax.swing.JTable expenseTable;
    private javax.swing.JLabel expensesValueLbl;
    private javax.swing.JLabel filterLbl;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JLabel grossProfitLbl;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton manageCategorysButton;
    private javax.swing.JButton newExpenseButton;
    private javax.swing.JLabel profitValueLbl;
    private javax.swing.JLabel resumeTitleLbl;
    private javax.swing.JLabel salesValueLbl;
    private javax.swing.JScrollPane tableScrollPanel;
    private javax.swing.JLabel taxesValueLbl;
    private javax.swing.JLabel totalExpensesLbl;
    private javax.swing.JLabel totalSalesLbl;
    private javax.swing.JTextField valueField;
    private javax.swing.JLabel valueLbl;
    // End of variables declaration//GEN-END:variables
}
