package br.com.devjf.salessync.view.forms.newobjectforms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import br.com.devjf.salessync.controller.ExpenseController;
import br.com.devjf.salessync.model.Expense;
import br.com.devjf.salessync.model.ExpenseCategory;
import br.com.devjf.salessync.model.RecurrenceType;
import br.com.devjf.salessync.util.RecurrenceTypeConverter;
import br.com.devjf.salessync.view.MainAppView;
import br.com.devjf.salessync.view.components.style.ViewComponentStyle;
import br.com.devjf.salessync.view.forms.ExpensesForm;
import br.com.devjf.salessync.view.forms.ReportsForm;
import br.com.devjf.salessync.view.forms.validators.ExpenseFormValidator;
import java.awt.Cursor;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class NewExpenseForm extends javax.swing.JFrame {
    private Expense expenseToEdit;
    private final ExpenseController expenseController;
    private boolean isEditMode = false;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(
            new Locale("pt",
                    "BR"));
    private ExpenseCategory selectedCategory;

    /**
     * Creates a new expense form for adding an expense.
     */
    public NewExpenseForm() {
        initComponents();
        initializeValueField();
        this.expenseController = new ExpenseController();
        initForNewExpense();
    }

    /**
     * Constructor for editing an existing expense.
     *
     * @param expenseId The expense to be edited
     */
    public NewExpenseForm(Integer expenseId) {
        initComponents();
        initializeValueField();
        this.expenseController = new ExpenseController();
        isEditMode = true;
        loadExpenseFromDb(expenseId);
    }

    /**
     * Initializes the form for a new expense
     */
    private void initForNewExpense() {
        setupCategoryComboBox();
        setupRecurrenceComboBox();
        saveBtn.setText("Salvar");
    }

    /**
     * Loads an expense from the database for editing
     *
     * @param expenseId ID of the expense to be loaded
     */
    private void loadExpenseFromDb(Integer expenseId) {
        if (expenseId == null) {
            expenseToEdit = null;
            return;
        }
        try {
            expenseToEdit = expenseController.findExpenseById(expenseId);
            if (expenseToEdit == null) {
                JOptionPane.showMessageDialog(this,
                        "Despesa não encontrada",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }
            saveBtn.setText("Atualizar");
            setupCategoryComboBox();
            setupRecurrenceComboBox();
            loadExpenseData();
        } catch (Exception e) {
            System.err.println("Erro ao carregar despesa: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar despesa: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    /**
     * Sets up the category ComboBox
     */
    private void setupCategoryComboBox() {
        categoryCmb.removeAllItems();
        categoryCmb.addItem("Selecione");
        try {
            List<ExpenseCategory> categories = expenseController.listAllCategories();
            for (ExpenseCategory category : categories) {
                categoryCmb.addItem(category.getName());
            }
            // Se estiver em modo de edição, selecionar a categoria da despesa
            if (isEditMode && expenseToEdit != null && expenseToEdit.getCategory() != null) {
                String categoryName = expenseToEdit.getCategory().getName();
                for (int i = 0; i < categoryCmb.getItemCount(); i++) {
                    if (categoryCmb.getItemAt(i).equals(categoryName)) {
                        categoryCmb.setSelectedIndex(i);
                        selectedCategory = expenseToEdit.getCategory();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar categorias: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar categorias: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sets up the recurrence ComboBox
     */
    private void setupRecurrenceComboBox() {
        final String DEFAULT_SELECTION = "Selecione";
        recurrenceCmb.setModel(new DefaultComboBoxModel<>(new String[]{
            DEFAULT_SELECTION,
            RecurrenceTypeConverter.toDisplayText(RecurrenceType.DAILY),
            RecurrenceTypeConverter.toDisplayText(RecurrenceType.WEEKLY),
            RecurrenceTypeConverter.toDisplayText(RecurrenceType.MONTHLY),
            RecurrenceTypeConverter.toDisplayText(RecurrenceType.ANNUAL)
        }));
        // If in edit mode, select the expense's recurrence
        if (isEditMode && expenseToEdit != null && expenseToEdit.getRecurrence() != null) {
            String recurrenceStr = RecurrenceTypeConverter.toDisplayText(
                    expenseToEdit.getRecurrence());
            // More efficient way to find and set the selected index
            int index = Arrays.asList(recurrenceCmb.getModel().getSize())
                    .indexOf(recurrenceStr);
            if (index != -1) {
                recurrenceCmb.setSelectedIndex(index);
            }
        }
    }

    private Double parseAmount(String amountText) {
        try {
            // Handle currency parsing with Brazilian Real format
            String cleanedAmount = amountText
                    .trim()
                    .replace("R$ ",
                            "")
                    .replace("R$",
                            "")
                    .replaceAll("[\\u200B\\u200C\\u200D\\u202F]",
                            "")
                    .replaceAll("\\p{C}+",
                            "")
                    .replaceAll("\\s+",
                            "")
                    .replace(".",
                            "")
                    .replace(",",
                            ".")
                    .trim();
            // Debugging output
            System.out.println("Cleaned amount: " + cleanedAmount);
            // Ensure proper decimal format
            if (!cleanedAmount.contains(".")) {
                cleanedAmount += ".00";
            } else {
                String[] parts = cleanedAmount.split("\\.");
                if (parts.length > 1) {
                    if (parts[1].length() > 2) {
                        parts[1] = parts[1].substring(0,
                                2);
                    } else if (parts[1].length() == 1) {
                        parts[1] += "0";
                    }
                    cleanedAmount = parts[0] + "." + parts[1];
                }
            }
            // Debugging output
            System.out.println("Formatted amount: " + cleanedAmount);
            // Validate and parse amount
            Double amount = Double.parseDouble(cleanedAmount);
            // Debugging output
            System.out.println("Parsed amount: " + amount);
            // Validate positive amount
            if (amount <= 0) {
                throw new IllegalArgumentException(
                        "Valor deve ser maior que zero");
            }
            return amount;
        } catch (Exception e) {
            System.err.println("Error parsing amount: " + e.getMessage());
            e.printStackTrace();
            throw new IllegalArgumentException(
                    "Formato de valor inválido: " + amountText);
        }
    }

    /**
     * Prepares an Expense object from form inputs
     *
     * @return Prepared Expense object
     * @throws ParseException if date parsing fails
     */
    private Expense prepareExpense() throws ParseException {
        // Use existing expense in edit mode, otherwise create new
        Expense expense = isEditMode ? expenseToEdit : new Expense();
        // Set description
        expense.setDescription(descriptionField.getText().trim());
        try {
            double amount = parseAmount(valueField.getText());
            expense.setAmount(amount);
            // Set date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            LocalDateTime expenseDate = dateFormat.parse(dateField.getText()).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            expense.setDate(expenseDate);
            // Set category
            String selectedCategoryName = categoryCmb.getSelectedItem().toString();
            List<ExpenseCategory> categories = expenseController.listAllCategories();
            ExpenseCategory category = categories.stream()
                    .filter(cat -> cat.getName().equals(selectedCategoryName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                    "Categoria não encontrada"));
            expense.setCategory(category);
            // Set recurrence
            RecurrenceType recurrence = RecurrenceTypeConverter.fromDisplayText(
                    recurrenceCmb.getSelectedItem().toString());
            expense.setRecurrence(recurrence);
            return expense;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Valor inválido: " + valueField.getText());
        }
    }

    /**
     * Loads expense data into form fields
     */
    private void loadExpenseData() {
        if (expenseToEdit != null) {
            // Preencher descrição
            descriptionField.setText(expenseToEdit.getDescription());
            // Preencher valor formatado como moeda
            valueField.setText(currencyFormat.format(expenseToEdit.getAmount()));
            // Preencher data
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateField.setText(dateFormat.format(java.sql.Date.valueOf(
                    expenseToEdit.getDate().toLocalDate())));
            // Selecionar categoria
            if (expenseToEdit.getCategory() != null) {
                String categoryName = expenseToEdit.getCategory().getName();
                for (int i = 0; i < categoryCmb.getItemCount(); i++) {
                    if (categoryCmb.getItemAt(i).equals(categoryName)) {
                        categoryCmb.setSelectedIndex(i);
                        break;
                    }
                }
            }
            // Update recurrence selection
            if (expenseToEdit.getRecurrence() != null) {
                recurrenceCmb.setSelectedItem(
                        RecurrenceTypeConverter.toDisplayText(
                                expenseToEdit.getRecurrence()));
            }
        }
    }

    private void initializeValueField() {
        valueField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                formatCurrencyValue();
            }
        });
    }

    // Currency formatting method
    private void formatCurrencyValue() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Clean the input value from non-numeric characters
                String text = valueField.getText().replaceAll("[^0-9]",
                        "");
                // Prevent empty input
                if (text.isEmpty()) {
                    valueField.setText("");
                    return;
                }
                // Pad with zeros if needed (to ensure we have at least two digits after the
                // decimal point)
                while (text.length() < 3) {
                    text = "0" + text;
                }
                // Parse cents to double
                long cents = Long.parseLong(text);
                double value = cents / 100.0;
                // Use DecimalFormat to format with comma as thousands separator
                DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                String formattedValue = "R$ " + decimalFormat.format(value);
                // Set formatted value to the text field
                valueField.setText(formattedValue);
            } catch (NumberFormatException ex) {
                System.err.println(
                        "Currency formatting error: " + ex.getMessage());
                valueField.setText("");
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
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
        dateField = new javax.swing.JFormattedTextField();
        valueField = new javax.swing.JTextField();
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
        recurrenceCmb.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "Selecione", "DIÁRIA", "SEMANAL", "MENSAL", "ANUAL" }));
        recurrenceCmb.setPreferredSize(new java.awt.Dimension(178, 40));

        categoryCmb.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        categoryCmb.setPreferredSize(new java.awt.Dimension(178, 40));

        try {
            dateField.setFormatterFactory(
                    new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dateField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        dateField.setToolTipText("");
        dateField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dateField.setPreferredSize(new java.awt.Dimension(179, 40));
        ViewComponentStyle.standardCornerRadius(dateField);

        valueField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        valueField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        valueField.setPreferredSize(new java.awt.Dimension(179, 40));
        ViewComponentStyle.standardCornerRadius(valueField);

        javax.swing.GroupLayout expensePnlLayout = new javax.swing.GroupLayout(expensePnl);
        expensePnl.setLayout(expensePnlLayout);
        expensePnlLayout.setHorizontalGroup(
                expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, expensePnlLayout.createSequentialGroup()
                                .addGroup(expensePnlLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(expensePnlLayout.createSequentialGroup()
                                                .addGap(20, 20, 20)
                                                .addGroup(expensePnlLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(descriptionLbl)
                                                        .addComponent(descriptionField,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                expensePnlLayout.createSequentialGroup()
                                                                        .addGroup(expensePnlLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(recurrenceCmb,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(recurrenceLbl))
                                                                        .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addGroup(expensePnlLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(categoryLbl)
                                                                                .addComponent(categoryCmb,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                .addGroup(expensePnlLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(expensePnlLayout.createSequentialGroup()
                                                                .addGap(20, 20, 20)
                                                                .addComponent(valueLbl))
                                                        .addGroup(expensePnlLayout.createSequentialGroup()
                                                                .addGap(18, 18, 18)
                                                                .addComponent(valueField,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21,
                                                        Short.MAX_VALUE)
                                                .addGroup(expensePnlLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(dateLbl)
                                                        .addComponent(dateField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(expensePnlLayout.createSequentialGroup()
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(20, 20, 20)));
        expensePnlLayout.setVerticalGroup(
                expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, expensePnlLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(
                                        expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(descriptionLbl)
                                                .addComponent(valueLbl)
                                                .addComponent(dateLbl))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(
                                        expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(descriptionField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(dateField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(valueField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(
                                        expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(recurrenceLbl)
                                                .addComponent(categoryLbl))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(
                                        expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(recurrenceCmb, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(categoryCmb, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 191,
                                        Short.MAX_VALUE)
                                .addGroup(
                                        expensePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)));

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
                                        .addComponent(expensePnl, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(71, 71, 71)));
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(titleField)
                                .addGap(18, 18, 18)
                                .addComponent(expensePnl, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(154, Short.MAX_VALUE)));

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

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelBtnActionPerformed
        initForNewExpense();
        MainAppView.redirectToPanel(MainAppView.EXPENSES_PANEL);
    }// GEN-LAST:event_cancelBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveBtnActionPerformed
        // Show a wait cursor while processing
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        saveBtn.setEnabled(false);
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            private Expense preparedExpense;

            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    // Validate all fields
                    ExpenseFormValidator.validateAllFields(
                            descriptionField,
                            valueField,
                            dateField,
                            categoryCmb,
                            recurrenceCmb);
                    // Prepare expense object
                    preparedExpense = prepareExpense();
                    // Save or update based on mode
                    if (isEditMode) {
                        expenseController.updateExpense(preparedExpense);
                    } else {
                        expenseController.registerExpense(preparedExpense);
                    }
                    return true;
                } catch (IllegalStateException validationEx) {
                    // Show validation error
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(
                                NewExpenseForm.this,
                                validationEx.getMessage(),
                                "Erro de Validação",
                                JOptionPane.ERROR_MESSAGE);
                    });
                    return false;
                } catch (ParseException | IllegalArgumentException ex) {
                    // Log and show unexpected errors
                    SwingUtilities.invokeLater(() -> {
                        System.err.println(
                                "Erro ao salvar/atualizar despesa: " + ex.getMessage());
                        JOptionPane.showMessageDialog(
                                NewExpenseForm.this,
                                "Erro ao salvar/atualizar despesa: " + ex.getMessage(),
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                    });
                    return false;
                }
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        // Log user activity
                        MainAppView.getInstance().registerUserActivity(
                                (isEditMode ? "Atualizou" : "Registrou")
                                + " a despesa: " + preparedExpense.getDescription());
                        JOptionPane.showMessageDialog(NewExpenseForm.this,
                                "Despesa " + (isEditMode ? "atualizada" : "registrada") + " com sucesso!",
                                "Sucesso",
                                JOptionPane.INFORMATION_MESSAGE);
                        // Redirect to Expenses panel
                        MainAppView.redirectToPanel(MainAppView.EXPENSES_PANEL);
                        ExpensesForm.getInstance().refreshTable();
                    } else {
                        // Re-enable save button if operation failed
                        saveBtn.setEnabled(true);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    // Extract the root cause
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    String errorMessage = cause.getMessage() != null
                            ? cause.getMessage()
                            : "Erro desconhecido ao processar despesa";
                    System.err.println(
                            "Erro ao processar resultado: " + errorMessage);
                    JOptionPane.showMessageDialog(
                            NewExpenseForm.this,
                            "Erro ao processar despesa: " + errorMessage,
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    // Always re-enable save button
                    saveBtn.setEnabled(true);
                } finally {
                    // Reset cursor
                    setCursor(Cursor.getDefaultCursor());
                    initForNewExpense();
                    ReportsForm.getInstance().updateReportValues();
                }
            }
        };
        // Execute the worker
        worker.execute();
    }// GEN-LAST:event_saveBtnActionPerformed

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
    private javax.swing.JTextField valueField;
    private javax.swing.JLabel valueLbl;
    // End of variables declaration//GEN-END:variables
}
