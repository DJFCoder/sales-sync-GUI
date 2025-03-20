package br.com.devjf.salessync.view.forms;

import br.com.devjf.salessync.util.ViewUtil;

public class CustomersForm extends javax.swing.JFrame {
    public CustomersForm() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        filterLbl = new javax.swing.JLabel();
        newCustomerButton = new javax.swing.JButton();
        deleteCustomerButton = new javax.swing.JButton();
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

        newCustomerButton.setBackground(new java.awt.Color(76, 175, 80));
        newCustomerButton.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        newCustomerButton.setForeground(new java.awt.Color(255, 255, 255));
        newCustomerButton.setText("Novo Cliente");
        newCustomerButton.setPreferredSize(new java.awt.Dimension(150, 40));
        ViewUtil.standardCornerRadius(newCustomerButton);

        deleteCustomerButton.setBackground(new java.awt.Color(175, 76, 78));
        deleteCustomerButton.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        deleteCustomerButton.setForeground(new java.awt.Color(255, 255, 255));
        deleteCustomerButton.setText("Excluir Cliente");
        deleteCustomerButton.setPreferredSize(new java.awt.Dimension(150, 40));
        ViewUtil.standardCornerRadius(deleteCustomerButton);

        filterPanel.setPreferredSize(new java.awt.Dimension(917, 50));
        ViewUtil.standardCornerRadius(filterPanel);

        idField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        idField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        idField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewUtil.standardCornerRadius(idField);

        idLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        idLbl.setText("Código:");

        taxIdLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        taxIdLbl.setText("CPF/CNPJ:");

        taxIdField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        taxIdField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        taxIdField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewUtil.standardCornerRadius(taxIdField);

        customerLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        customerLbl.setText("Cliente:");

        customerField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        customerField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        customerField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewUtil.standardCornerRadius(customerField);

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
        ViewUtil.standardCornerRadius(customerField);
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
                            .addComponent(deleteCustomerButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(newCustomerButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(newCustomerButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteCustomerButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    
    // new CustomersForm().setVisible(true);

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField customerField;
    private javax.swing.JLabel customerLbl;
    private javax.swing.JTable customerTable;
    private javax.swing.JButton deleteCustomerButton;
    private javax.swing.JLabel filterLbl;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel idLbl;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton newCustomerButton;
    private javax.swing.JScrollPane tableScrollPanel;
    private javax.swing.JTextField taxIdField;
    private javax.swing.JLabel taxIdLbl;
    // End of variables declaration//GEN-END:variables
}
