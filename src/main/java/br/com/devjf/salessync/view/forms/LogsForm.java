package br.com.devjf.salessync.view.forms;

import br.com.devjf.salessync.util.ViewUtil;

public class LogsForm extends javax.swing.JFrame {
    public LogsForm() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        filterLbl = new javax.swing.JLabel();
        filterPanel = new javax.swing.JPanel();
        statusField = new javax.swing.JTextField();
        statusLbl = new javax.swing.JLabel();
        userTypeLbl = new javax.swing.JLabel();
        userTypeField = new javax.swing.JTextField();
        nameLbl = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        tableScrollPanel = new javax.swing.JScrollPane();
        logTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        filterLbl.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        filterLbl.setText("Filtar");

        filterPanel.setPreferredSize(new java.awt.Dimension(917, 50));
        ViewUtil.standardCornerRadius(filterPanel);

        statusField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        statusField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        statusField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewUtil.standardCornerRadius(statusField);

        statusLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        statusLbl.setText("Data/Hora:");

        userTypeLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        userTypeLbl.setText("Ação:");

        userTypeField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        userTypeField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        userTypeField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewUtil.standardCornerRadius(userTypeField);

        nameLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        nameLbl.setText("Usuário:");

        nameField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        nameField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nameField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewUtil.standardCornerRadius(nameField);

        javax.swing.GroupLayout filterPanelLayout = new javax.swing.GroupLayout(filterPanel);
        filterPanel.setLayout(filterPanelLayout);
        filterPanelLayout.setHorizontalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addGap(120, 120, 120)
                .addComponent(nameLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(statusLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(statusField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(userTypeLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(userTypeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(121, Short.MAX_VALUE))
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
                        .addComponent(userTypeLbl)
                        .addComponent(userTypeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLbl)
                        .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );

        logTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Data/Hora", "Usuário", "Ação", "Detalhes"
            }
        ));
        tableScrollPanel.setViewportView(logTable);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 917, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(tableScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
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
    
    // new LogsForm().setVisible(true);

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel filterLbl;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JTable logTable;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLbl;
    private javax.swing.JTextField statusField;
    private javax.swing.JLabel statusLbl;
    private javax.swing.JScrollPane tableScrollPanel;
    private javax.swing.JTextField userTypeField;
    private javax.swing.JLabel userTypeLbl;
    // End of variables declaration//GEN-END:variables
}
