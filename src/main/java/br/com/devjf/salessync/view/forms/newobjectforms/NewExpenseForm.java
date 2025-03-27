package br.com.devjf.salessync.view.forms.newobjectforms;

import java.text.SimpleDateFormat;
import java.util.List;
import br.com.devjf.salessync.controller.ExpenseController;
import br.com.devjf.salessync.model.Expense;
import br.com.devjf.salessync.model.ExpenseCategory;
import br.com.devjf.salessync.model.RecurrenceType;
import static br.com.devjf.salessync.model.RecurrenceType.*;
import br.com.devjf.salessync.view.components.style.ViewComponentStyle;
import br.com.devjf.salessync.view.MainAppView;

public class NewExpenseForm extends javax.swing.JFrame {
    private Expense expenseToEdit;
    private ExpenseController expenseController;
    private boolean isEditMode = false;

    public NewExpenseForm() {
        initComponents();
    }

    /**
     * Construtor para editar uma despesa existente.
     *
     * @param expense A despesa a ser editada
     */
    public NewExpenseForm(Expense expense) {
        initComponents();
        this.expenseToEdit = expense;
        this.expenseController = new ExpenseController();
        this.isEditMode = true;
        // Carregar categorias no combobox
        loadCategories();
        // Preencher os campos com os dados da despesa
        loadExpenseData();
        // Alterar o título do formulário
        titleField.setText("Editar Despesa");
        // Alterar o texto do botão para "Atualizar"
        saveBtn.setText("Atualizar");
    }

    /**
     * Carrega as categorias de despesa no combobox
     */
    private void loadCategories() {
        // Limpar o combobox
        categoryCmb.removeAllItems();
        categoryCmb.addItem("Selecione");
        // Buscar todas as categorias
        List<ExpenseCategory> categories = expenseController.listAllCategories();
        // Adicionar as categorias ao combobox
        for (ExpenseCategory category : categories) {
            categoryCmb.addItem(category.getName());
        }
    }

    /**
     * Carrega os dados da despesa nos campos do formulário
     */
    private void loadExpenseData() {
        if (expenseToEdit != null) {
            // Preencher descrição
            descriptionField.setText(expenseToEdit.getDescription());
            // Preencher valor
            valueField.setValue(expenseToEdit.getAmount());
            // Preencher data
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateField.setText(dateFormat.format(java.sql.Date.valueOf(
                    expenseToEdit.getDate())));
            // Selecionar categoria
            if (expenseToEdit.getCategory() != null) {
                for (int i = 0; i < categoryCmb.getItemCount(); i++) {
                    if (categoryCmb.getItemAt(i).equals(
                            expenseToEdit.getCategory().getName())) {
                        categoryCmb.setSelectedIndex(i);
                        break;
                    }
                }
            }
            // Selecionar recorrência
            if (expenseToEdit.getRecurrence() != null) {
                String recurrenceStr = mapRecurrenceTypeToDisplay(
                        expenseToEdit.getRecurrence());
                for (int i = 0; i < recurrenceCmb.getItemCount(); i++) {
                    if (recurrenceCmb.getItemAt(i).equals(recurrenceStr)) {
                        recurrenceCmb.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Mapeia o enum RecurrenceType para o texto exibido no ComboBox
     *
     * @param type O tipo de recorrência
     * @return O texto a ser exibido no ComboBox
     */
    private String mapRecurrenceTypeToDisplay(RecurrenceType type) {
        switch (type) {
            case DAILY:
                return "DIÁRIA";
            case WEEKLY:
                return "SEMANAL";
            case MONTHLY:
                return "MENSAL";
            case ANNUAL:
                return "ANUAL";
            default:
                return "";
        }
    }

    /**
     * Mapeia o texto do ComboBox para o enum RecurrenceType
     *
     * @param displayText O texto exibido no ComboBox
     * @return O enum RecurrenceType correspondente
     */
    private RecurrenceType mapDisplayToRecurrenceType(String displayText) {
        switch (displayText) {
            case "DIÁRIA":
                return RecurrenceType.DAILY;
            case "SEMANAL":
                return RecurrenceType.WEEKLY;
            case "MENSAL":
                return RecurrenceType.MONTHLY;
            case "ANUAL":
                return RecurrenceType.ANNUAL;
            default:
                return null;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        expensePnl = new javax.swing.JPanel();
        cancelBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        descriptionField = new javax.swing.JTextField();
        descriptionLbl = new javax.swing.JLabel();
        valueLbl = new javax.swing.JLabel();
        recurrenceLbl = new javax.swing.JLabel();
        categoryLbl = new javax.swing.JLabel();
        dateLbl = new javax.swing.JLabel();
        recurrenceCmb = new javax.swing.JComboBox<>();
        categoryCmb = new javax.swing.JComboBox<>();
        valueField = new javax.swing.JFormattedTextField();
        dateField = new javax.swing.JFormattedTextField();
        titleField = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        expensePnl.setPreferredSize(new java.awt.Dimension(813, 459));
        ViewComponentStyle.standardCornerRadius(expensePnl);

        cancelBtn.setBackground(new java.awt.Color(175, 76, 78));
        cancelBtn.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        cancelBtn.setForeground(new java.awt.Color(255, 255, 255));
        cancelBtn.setText("Cancelar");
        cancelBtn.setPreferredSize(new java.awt.Dimension(120, 40));
        ViewComponentStyle.standardCornerRadius(cancelBtn);
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
        ViewComponentStyle.standardCornerRadius(saveBtn);
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        descriptionField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        descriptionField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        descriptionField.setPreferredSize(new java.awt.Dimension(376, 40));
        ViewComponentStyle.standardCornerRadius(descriptionField);

        descriptionLbl.setText("Descrição:");

        valueLbl.setText("Valor (R$):");

        recurrenceLbl.setText("Recorrência:");

        categoryLbl.setText("Categoria:");

        dateLbl.setText("Data:");

        recurrenceCmb.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        recurrenceCmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "DIÁRIA", "SEMANAL", "MENSAL", "ANUAL" }));
        recurrenceCmb.setPreferredSize(new java.awt.Dimension(178, 40));

        categoryCmb.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        categoryCmb.setPreferredSize(new java.awt.Dimension(178, 40));

        valueField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.##"))));
        valueField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        valueField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        valueField.setPreferredSize(new java.awt.Dimension(179, 40));
        ViewComponentStyle.standardCornerRadius(valueField);

        try {
            dateField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dateField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        dateField.setToolTipText("");
        dateField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dateField.setPreferredSize(new java.awt.Dimension(179, 40));
        ViewComponentStyle.standardCornerRadius(dateField);

        javax.swing.GroupLayout expensePnlLayout = new javax.swing.GroupLayout(expensePnl);
        expensePnl.setLayout(expensePnlLayout);
        expensePnlLayout.setHorizontalGroup(
            expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, expensePnlLayout.createSequentialGroup()
                .addGroup(expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(expensePnlLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(descriptionLbl)
                            .addComponent(descriptionField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, expensePnlLayout.createSequentialGroup()
                                .addGroup(expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(recurrenceCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(recurrenceLbl))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(categoryLbl)
                                    .addComponent(categoryCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(expensePnlLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(valueLbl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, expensePnlLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                                .addComponent(valueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateLbl)
                            .addComponent(dateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(expensePnlLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        expensePnlLayout.setVerticalGroup(
            expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, expensePnlLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(descriptionLbl)
                    .addComponent(valueLbl)
                    .addComponent(dateLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(descriptionField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(valueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(recurrenceLbl)
                    .addComponent(categoryLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(recurrenceCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(categoryCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 195, Short.MAX_VALUE)
                .addGroup(expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        titleField.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        titleField.setText("Informações da Despesa");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(titleField)
                    .addComponent(expensePnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(71, 71, 71))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(titleField)
                .addGap(18, 18, 18)
                .addComponent(expensePnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(154, Short.MAX_VALUE))
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

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        MainAppView.redirectToPanel(MainAppView.EXPENSES_PANEL);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed

    }//GEN-LAST:event_saveBtnActionPerformed
    // new NewExpenseForm().setVisible(true);

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JComboBox<String> categoryCmb;
    private javax.swing.JLabel categoryLbl;
    private javax.swing.JFormattedTextField dateField;
    private javax.swing.JLabel dateLbl;
    private javax.swing.JTextField descriptionField;
    private javax.swing.JLabel descriptionLbl;
    private javax.swing.JPanel expensePnl;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JComboBox<String> recurrenceCmb;
    private javax.swing.JLabel recurrenceLbl;
    private javax.swing.JButton saveBtn;
    private javax.swing.JLabel titleField;
    private javax.swing.JFormattedTextField valueField;
    private javax.swing.JLabel valueLbl;
    // End of variables declaration//GEN-END:variables
}
