package br.com.devjf.salessync.view.forms;

import br.com.devjf.salessync.util.ViewUtil;

public class ExpensesForm extends javax.swing.JFrame {
    public ExpensesForm() {
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
        ViewUtil.standardCornerRadius(newExpenseButton);

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
        ViewUtil.standardCornerRadius(manageCategorysButton);

        filterPanel.setPreferredSize(new java.awt.Dimension(917, 50));
        ViewUtil.standardCornerRadius(filterPanel);

        descriptionField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        descriptionField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        descriptionField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewUtil.standardCornerRadius(descriptionField);

        descriptionLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        descriptionLbl.setText("Descrição:");

        valueLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        valueLbl.setText("Valor:");

        valueField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        valueField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        valueField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewUtil.standardCornerRadius(valueField);

        categoryLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        categoryLbl.setText("Categoria:");

        categoryField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        categoryField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        categoryField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewUtil.standardCornerRadius(categoryField);

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
                    .addComponent(newExpenseButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(manageCategorysButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void manageCategorysButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageCategorysButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_manageCategorysButtonActionPerformed
    
    // new ExpensesForm().setVisible(true);

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField categoryField;
    private javax.swing.JLabel categoryLbl;
    private javax.swing.JTextField descriptionField;
    private javax.swing.JLabel descriptionLbl;
    private javax.swing.JTable expenseTable;
    private javax.swing.JLabel filterLbl;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton manageCategorysButton;
    private javax.swing.JButton newExpenseButton;
    private javax.swing.JScrollPane tableScrollPanel;
    private javax.swing.JTextField valueField;
    private javax.swing.JLabel valueLbl;
    // End of variables declaration//GEN-END:variables
}
