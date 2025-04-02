package br.com.devjf.salessync.view.forms.newobjectforms;

import java.awt.Cursor;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import br.com.devjf.salessync.controller.CustomerController;
import br.com.devjf.salessync.controller.SaleController;
import br.com.devjf.salessync.controller.UserController;
import br.com.devjf.salessync.dto.SaleItemDTO;
import br.com.devjf.salessync.model.Customer;
import br.com.devjf.salessync.model.PaymentMethod;
import br.com.devjf.salessync.model.Sale;
import br.com.devjf.salessync.model.SaleItem;
import br.com.devjf.salessync.service.auth.UserSessionManager;
import br.com.devjf.salessync.view.MainAppView;
import br.com.devjf.salessync.view.components.CustomerSelectionDialog;
import br.com.devjf.salessync.view.components.style.ViewComponentStyle;
import br.com.devjf.salessync.view.components.table.SaleTableManager;
import br.com.devjf.salessync.view.forms.SalesForm;

/**
 * Form for creating and editing sales in the Sales Sync application. This form
 * allows users to select customers, add sale items, set payment methods, and
 * calculate totals for new sales or edit existing ones.
 *
 */
public class NewSaleForm extends javax.swing.JFrame {
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(
            new Locale("pt",
                    "BR"));
    private Sale saleToCreate;
    private Customer selectedCustomer;
    private final List<SaleItem> saleItems = new ArrayList<>();
    private int currentUser;
    // Controllers
    private final SaleController saleController;
    private final CustomerController customerController;
    private final UserController userController;
    // Table manager
    private SaleTableManager tableManager;

    /**
     * Creates a new form for registering a sale. Initializes controllers, sets
     * up the table, and prepares the form for a new sale.
     */
    public NewSaleForm() {
        // Initialize controllers
        this.saleController = new SaleController();
        this.customerController = new CustomerController();
        this.userController = new UserController();
        initComponents();
        // Initialize table manager
        ensureTableManagerInitialized();
        // Configurar o formulário para uma nova venda
        this.tableManager = new SaleTableManager(newSaleTable,
                this::updateTotalsWithoutCheck);
        initializeSale();
        setCurrentDateInPaymentField();
        // Adicionar listener para o campo de desconto
        discountField.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateTotals();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateTotals();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateTotals();
            }
        });
        updateTotals();
    }

    /**
     * Creates a new form for editing a sale. Initializes controllers, sets up
     * the table, and prepares the form for a existing sale.
     *
     * @param sale a sale to edit
     */
    public NewSaleForm(Sale sale) {
        // Initialize controllers
        this.saleController = new SaleController();
        this.customerController = new CustomerController();
        this.userController = new UserController();
        initComponents();
        // Initialize table manager - only once
        this.tableManager = new SaleTableManager(newSaleTable,
                this::updateTotalsWithoutCheck);
        // Configurar o formulário para edição
        this.saleToCreate = sale;
        this.selectedCustomer = sale.getCustomer();
        currentUser = UserSessionManager.getInstance().getLoggedUser().getId();
        // Preencher os campos do formulário com os dados da venda
        loadSaleData(sale);
        // Adicionar listener para o campo de desconto
        discountField.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateTotals();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateTotals();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateTotals();
            }
        });
        // Alterar o texto do botão para "Atualizar Venda"
        newSaleBtn.setText("Atualizar Venda");
    }

    /**
     * Updates totals without checking if tableManager is initialized. This
     * method is used to break the circular dependency.
     */
    private void updateTotalsWithoutCheck() {
        try {
            if (tableManager == null) {
                return; // Skip if tableManager is not initialized yet
            }
            // Calculate subtotal
            double subtotal = tableManager.calculateSubtotal();
            double discount = getDiscount();
            double total = subtotal - discount;
            // Atualizar os labels
            subtotalLbl.setText("Subtotal: " + currencyFormat.format(subtotal));
            totalLbl.setText("Total: " + currencyFormat.format(total));
            // Update sale object if it exists
            if (saleToCreate != null) {
                saleToCreate.setSubtotalAmount(subtotal);
                saleToCreate.setDiscountAmount(discount);
                saleToCreate.setTotalAmount(total);
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar totais: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Ensures the table manager is initialized. This method can be called
     * multiple times safely.
     */
    private void ensureTableManagerInitialized() {
        if (this.tableManager == null && newSaleTable != null) {
            // Use updateTotalsWithoutCheck to avoid circular dependency
            this.tableManager = new SaleTableManager(newSaleTable,
                    this::updateTotalsWithoutCheck);
        }
    }

    /**
     * Initializes the form for a new sale. Clears all fields, resets the sale
     * object, and prepares the form for data entry.
     */
    public void initForNewSale() {
        // Limpar o objeto saleToCreate para garantir que estamos criando uma nova venda
        saleToCreate = new Sale();
        // Limpar campos do formulário
        nameField.setText("");
        selectedCustomer = null;
        paymentMethodCmb.setSelectedIndex(0);
        setCurrentDateInPaymentField();
        discountField.setText("");
        // Limpar tabela de itens
        clearTable();
        // Adicionar uma linha vazia para o primeiro item
        tableManager.addNewRow();
        // Atualizar os totais
        updateTotals();
        // Atualizar o título do botão para "Registrar Venda"
        newSaleBtn.setText("Salvar");
    }

    /**
     * Clears all items from the sale table and the items list.
     */
    private void clearTable() {
        // Use the table manager to clear the table
        DefaultTableModel tableModel = tableManager.getTableModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        // Limpar a lista de itens
        saleItems.clear();
    }

    /**
     * Sets the current date in the payment date field using the format
     * dd/MM/yyyy.
     */
    private void setCurrentDateInPaymentField() {
        // Format current date as dd/MM/yyyy
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        String formattedDate = dateFormat.format(currentDate);
        // Set the formatted date in the payment date field
        paymentMethodField.setText(formattedDate);
    }

    /**
     * Initializes a new Sale object with default values. Sets the current date,
     * initializes amounts to zero, and associates the current user.
     */
    private void initializeSale() {
        // Reset the sale object
        saleToCreate = new Sale();
        // Set default values
        saleToCreate.setDate(LocalDateTime.now());
        saleToCreate.setSubtotalAmount(0.0);
        saleToCreate.setDiscountAmount(0.0);
        saleToCreate.setTotalAmount(0.0);
        // Get current logged-in user
        try {
            currentUser = UserSessionManager.getInstance().getLoggedUser().getId();
            saleToCreate.setUser(userController.findUserById(currentUser));
        } catch (Exception e) {
            System.err.println("Error getting current user: " + e.getMessage());
        }
    }

    /**
     * Prepares a Sale object from the form data. Validates all inputs and
     * creates a new Sale object or updates an existing one.
     *
     * @return The prepared Sale object, or null if validation fails
     */
    private Sale prepareSaleObject() {
        ensureTableManagerInitialized();
        try {
            // Collect form data
            Customer customer = selectedCustomer;
            String selectedPaymentMethodStr = (String) paymentMethodCmb.getSelectedItem();
            String paymentDateStr = paymentMethodField.getText();
            // Calculate values directly here to ensure they're accurate
            double subtotal = tableManager.calculateSubtotal();
            double discount = getDiscount();
            double total = subtotal - discount; // Calculate total correctly
            // Log the values to verify they're correct
            System.out.println("Preparing sale with subtotal: " + subtotal
                    + ", discount: " + discount
                    + ", total: " + total);
            // Collect items data from the table
            List<SaleItemDTO> itemsData = new ArrayList<>();
            DefaultTableModel tableModel = tableManager.getTableModel();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String description = (String) tableModel.getValueAt(i,
                        0);
                // Skip empty rows
                if (description == null || description.trim().isEmpty()) {
                    continue;
                }
                Object quantityObj = tableModel.getValueAt(i,
                        1);
                Object priceObj = tableModel.getValueAt(i,
                        2);
                // Create a simple DTO to transfer the data
                SaleItemDTO itemData = new SaleItemDTO(description,
                        quantityObj,
                        priceObj);
                itemsData.add(itemData);
            }
            // Determine if we're creating or updating
            boolean isNewSale = (saleToCreate == null || saleToCreate.getId() == null);
            // Ensure the current sale object has the correct values
            if (saleToCreate != null) {
                saleToCreate.setSubtotalAmount(subtotal);
                saleToCreate.setDiscountAmount(discount);
                saleToCreate.setTotalAmount(total);
            }
            // Delegate validation and creation to controller
            Sale preparedSale;
            if (!isNewSale) {
                // For updates, use the controller
                preparedSale = saleController.prepareSaleUpdate(
                        saleToCreate.getId(),
                        saleToCreate.getCreatedAt(),
                        saleToCreate.getDate(),
                        customer,
                        selectedPaymentMethodStr,
                        paymentDateStr,
                        itemsData,
                        subtotal,
                        discount,
                        currentUser);
            } else {
                // For new sales, use the controller
                preparedSale = saleController.prepareNewSale(
                        customer,
                        selectedPaymentMethodStr,
                        paymentDateStr,
                        itemsData,
                        subtotal,
                        discount,
                        currentUser);
            }
            return preparedSale;
        } catch (IllegalStateException | IllegalArgumentException | ParseException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Erro de validação",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    /**
     * Calculates and updates all total values displayed in the form.
     */
    private void updateTotals() {
        ensureTableManagerInitialized();
        try {
            // Calculate subtotal
            double subtotal = tableManager.calculateSubtotal();
            double discount = getDiscount();
            double total = subtotal - discount; // Consistent calculation
            // Log the values for debugging
            System.out.println("Updating totals - Subtotal: " + subtotal
                    + ", Discount: " + discount
                    + ", Total: " + total);
            // Update the labels
            subtotalLbl.setText("Subtotal: " + currencyFormat.format(subtotal));
            totalLbl.setText("Total: " + currencyFormat.format(total));
            // Update sale object if it exists
            if (saleToCreate != null) {
                saleToCreate.setSubtotalAmount(subtotal);
                saleToCreate.setDiscountAmount(discount);
                saleToCreate.setTotalAmount(total);
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar totais: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets the discount amount from the discount field.
     */
    private double getDiscount() {
        try {
            String discountText = discountField.getText().trim();
            if (discountText.isEmpty()) {
                return 0.0;
            }
            // Remover símbolos de moeda e converter para double
            discountText = discountText.replaceAll("[^\\d,.]",
                    "").replace(",",
                            ".");
            return Double.parseDouble(discountText);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * Refreshes the sales table in the SalesForm to reflect changes. Called
     * after a successful save or update operation.
     */
    private void refreshSalesTable() {
        // Obter a instância do SalesForm
        SalesForm salesForm = SalesForm.getInstance();
        if (salesForm != null) {
            // Chamar o método refreshTable do SalesForm
            salesForm.refreshTable();
        }
    }

    /**
     * Loads an existing sale's data into the form for editing. Populates all
     * fields with the sale's information including customer, payment details,
     * and items.
     *
     * @param sale The sale to be edited
     */
    private void loadSaleData(Sale sale) {
        ensureTableManagerInitialized();
        try {
            // Preencher os campos com os dados da venda
            if (sale.getCustomer() != null) {
                nameField.setText(sale.getCustomer().getName());
                selectedCustomer = sale.getCustomer();
            }
            // Selecionar o método de pagamento
            if (sale.getPaymentMethod() != null) {
                paymentMethodCmb.setSelectedItem(mapPaymentMethodToDisplay(
                        sale.getPaymentMethod()));
            }
            // Definir a data de pagamento
            if (sale.getPaymentDate() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                // Convert LocalDateTime to Date
                Date paymentDate = Date.from(sale.getPaymentDate().atZone(
                        ZoneId.systemDefault()).toInstant());
                paymentMethodField.setText(dateFormat.format(paymentDate));
            } else {
                // Set current date if payment date is null
                setCurrentDateInPaymentField();
            }
            // Carregar os itens da venda na tabela
            loadSaleItems(sale);
            // Atualizar o desconto - Format it properly to ensure it's displayed correctly
            if (sale.getDiscountAmount() != null) {
                // Format the discount value with 2 decimal places
                discountField.setText(String.format("%.2f",
                        sale.getDiscountAmount()));
            }
            updateTotals();
        } catch (Exception e) {
            System.err.println(
                    "Erro ao carregar dados da venda: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar dados da venda: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Maps a PaymentMethod enum value to its display string for the combo box.
     *
     * @param method The PaymentMethod enum value
     * @return The string representation for display
     */
    private String mapPaymentMethodToDisplay(PaymentMethod method) {
        return method.name();
    }

    /**
     * Loads the sale items from a sale into the items table.
     */
    private void loadSaleItems(Sale sale) {
        ensureTableManagerInitialized();
        tableManager.clearTable();
        tableManager.loadSaleItems(sale.getItems());
        // Add items to our tracking list
        for (SaleItem item : sale.getItems()) {
            saleItems.add(item);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        newSaleBtn = new javax.swing.JButton();
        sellInformationPnl = new javax.swing.JPanel();
        paymentMethodLbl = new javax.swing.JLabel();
        paymentDateLbl = new javax.swing.JLabel();
        nameLbl = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        findCustomerBtn = new javax.swing.JButton();
        paymentMethodCmb = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        paymentMethodField = new javax.swing.JFormattedTextField();
        tableScrollPanel = new javax.swing.JScrollPane();
        newSaleTable = new javax.swing.JTable();
        addItemBtn = new javax.swing.JButton();
        cancelSaleBtn = new javax.swing.JButton();
        valuesPnl = new javax.swing.JPanel();
        subtotalLbl = new javax.swing.JLabel();
        discountLbl = new javax.swing.JLabel();
        discountField = new javax.swing.JTextField();
        totalLbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        newSaleBtn.setBackground(new java.awt.Color(76, 175, 80));
        newSaleBtn.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        newSaleBtn.setForeground(new java.awt.Color(255, 255, 255));
        newSaleBtn.setText("Salvar");
        newSaleBtn.setPreferredSize(new java.awt.Dimension(150, 40));
        ViewComponentStyle.standardCornerRadius(newSaleBtn);
        newSaleBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newSaleBtnActionPerformed(evt);
            }
        });

        sellInformationPnl.setPreferredSize(new java.awt.Dimension(917, 50));
        ViewComponentStyle.standardCornerRadius(sellInformationPnl);

        paymentMethodLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        paymentMethodLbl.setText("Forma de Pagamento:");

        paymentDateLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        paymentDateLbl.setText("Data de Pagamento:");

        nameLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        nameLbl.setText("Cliente:");

        nameField.setEditable(false);
        nameField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        nameField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nameField.setFocusable(false);
        nameField.setPreferredSize(new java.awt.Dimension(181, 30));
        ViewComponentStyle.standardCornerRadius(nameField);

        findCustomerBtn.setBackground(new java.awt.Color(96, 125, 139));
        findCustomerBtn.setForeground(new java.awt.Color(255, 255, 255));
        findCustomerBtn.setText("Buscar");
        findCustomerBtn.setPreferredSize(new java.awt.Dimension(85, 30));
        ViewComponentStyle.standardCornerRadius(findCustomerBtn);
        findCustomerBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findCustomerBtnActionPerformed(evt);
            }
        });

        paymentMethodCmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "DINHEIRO", "CRÉDITO", "DÉBITO", "TRANSFERÊNCIA", "PIX", "BOLETO", "CHEQUE" }));
        paymentMethodCmb.setPreferredSize(new java.awt.Dimension(164, 30));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Informações da Venda");

        try {
            paymentMethodField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        paymentMethodField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        paymentMethodField.setPreferredSize(new java.awt.Dimension(114, 30));
        ViewComponentStyle.standardCornerRadius(paymentMethodField);

        javax.swing.GroupLayout sellInformationPnlLayout = new javax.swing.GroupLayout(sellInformationPnl);
        sellInformationPnl.setLayout(sellInformationPnlLayout);
        sellInformationPnlLayout.setHorizontalGroup(
            sellInformationPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sellInformationPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sellInformationPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(sellInformationPnlLayout.createSequentialGroup()
                        .addComponent(nameLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(findCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(paymentMethodLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paymentMethodCmb, 0, 156, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(paymentDateLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paymentMethodField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1))
                .addGap(7, 7, 7))
        );
        sellInformationPnlLayout.setVerticalGroup(
            sellInformationPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sellInformationPnlLayout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(sellInformationPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLbl)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(findCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paymentMethodLbl)
                    .addComponent(paymentMethodCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paymentDateLbl)
                    .addComponent(paymentMethodField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        newSaleTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Descrição", "Quantidade", "Preço Unitário", "Subtotal", "Ações"
            }
        ));
        tableScrollPanel.setViewportView(newSaleTable);

        addItemBtn.setBackground(new java.awt.Color(255, 178, 0));
        addItemBtn.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        addItemBtn.setForeground(new java.awt.Color(255, 255, 255));
        addItemBtn.setText("Adicionar Item");
        addItemBtn.setToolTipText("");
        addItemBtn.setPreferredSize(new java.awt.Dimension(170, 40));
        addItemBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addItemBtnActionPerformed(evt);
            }
        });
        ViewComponentStyle.standardCornerRadius(addItemBtn);

        cancelSaleBtn.setBackground(new java.awt.Color(175, 76, 78));
        cancelSaleBtn.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        cancelSaleBtn.setForeground(new java.awt.Color(255, 255, 255));
        cancelSaleBtn.setText("Cancelar");
        cancelSaleBtn.setPreferredSize(new java.awt.Dimension(150, 40));
        ViewComponentStyle.standardCornerRadius(cancelSaleBtn);
        cancelSaleBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelSaleBtnActionPerformed(evt);
            }
        });

        subtotalLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        subtotalLbl.setText("Subtotal: R$");

        discountLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        discountLbl.setText("Desconto: R$");

        discountField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        discountField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        discountField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewComponentStyle.standardCornerRadius(nameField);

        totalLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        totalLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalLbl.setText("Total: R$");

        javax.swing.GroupLayout valuesPnlLayout = new javax.swing.GroupLayout(valuesPnl);
        valuesPnl.setLayout(valuesPnlLayout);
        valuesPnlLayout.setHorizontalGroup(
            valuesPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(valuesPnlLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(subtotalLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(discountLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(discountField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        valuesPnlLayout.setVerticalGroup(
            valuesPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(valuesPnlLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(valuesPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subtotalLbl)
                    .addComponent(discountLbl)
                    .addComponent(discountField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalLbl))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addComponent(addItemBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelSaleBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(newSaleBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tableScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 917, Short.MAX_VALUE)
                    .addComponent(sellInformationPnl, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 916, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(valuesPnl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(sellInformationPnl, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(tableScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(valuesPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newSaleBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addItemBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelSaleBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(55, Short.MAX_VALUE))
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

    // Event handlers
    /**
     * Handles the action when the Add Item button is clicked. Adds a new empty
     * row to the items table.
     *
     * @param evt The action event
     */
    private void addItemBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemBtnActionPerformed
        tableManager.addNewRow();
    }//GEN-LAST:event_addItemBtnActionPerformed

    /**
     * Handles the action when the Save/Update Sale button is clicked. Validates
     * the form, creates or updates the sale, and shows appropriate messages.
     * Uses SwingWorker to perform the operation in the background.
     *
     * @param evt The action event
     */
    private void newSaleBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newSaleBtnActionPerformed
        // Show a wait cursor while processing
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        newSaleBtn.setEnabled(false);
        // Add a small delay to ensure any previous transactions are complete
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Prepare the sale object on the EDT before starting the background task
        final Sale preparedSale = prepareSaleObject();
        if (preparedSale == null) {
            setCursor(Cursor.getDefaultCursor());
            return;
        }
        // Debug information
        System.out.println("Prepared sale ID: " + preparedSale.getId());
        System.out.println("Number of items: " + preparedSale.getItems().size());
        // Use SwingWorker to avoid freezing the UI
        new SwingWorker<Sale, Void>() {
            private String errorMessage = null;
            private boolean isNewSale = false; // Flag para controlar se é nova venda ou atualização

            @Override
            protected Sale doInBackground() throws Exception {
                try {
                    // Determinar se é uma nova venda ou atualização
                    isNewSale = (preparedSale.getId() == null);
                    System.out.println("Is new sale: " + isNewSale);
                    Sale result;
                    if (isNewSale) {
                        // Create new sale
                        result = saleController.registerSale(preparedSale);
                        if (result == null) {
                            errorMessage = "Não foi possível registrar a venda. Verifique os dados e tente novamente.";
                            System.err.println("Failed to register sale");
                        }
                    } else {
                        // Update existing sale
                        System.out.println(
                                "Updating sale with ID: " + preparedSale.getId());
                        result = saleController.updateSale(preparedSale);
                        if (result == null) {
                            errorMessage = "Não foi possível atualizar a venda. Verifique os dados e tente novamente.";
                            System.err.println("Failed to update sale");
                        }
                    }
                    return result;
                } catch (Exception e) {
                    errorMessage = "Erro ao processar venda: " + e.getMessage();
                    System.err.println(
                            "Exception during sale processing: " + e.getMessage());
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void done() {
                try {
                    Sale result = get();
                    if (result != null) {
                        // Guardar temporariamente o ID para o log
                        Integer saleId = result.getId();
                        System.out.println(
                                "Operation successful. Sale ID: " + saleId);
                        // Atualizar o objeto saleToCreate com o resultado da operação
                        saleToCreate = result;
                        // Usar a flag isNewSale em vez de verificar o ID novamente
                        String operationType = isNewSale ? "registrada" : "atualizada";
                        JOptionPane.showMessageDialog(NewSaleForm.this,
                                "Venda " + operationType + " com sucesso!",
                                "Sucesso",
                                JOptionPane.INFORMATION_MESSAGE);
                        // Refresh the sales table in SalesForm
                        refreshSalesTable();
                        // Log the activity
                        MainAppView.getInstance().registerUserActivity(
                                (isNewSale ? "Registrou" : "Atualizou") + " a venda ID: " + saleId);
                        // Limpar o objeto saleToCreate para a próxima operação
                        saleToCreate = null;
                        System.out.println("apagou a venda.");
                        // Close this form
                        dispose();
                        // Return to the SalesForm panel in MainAppView
                        SalesForm.getInstance().refreshTable();
                        MainAppView.redirectToPanel(MainAppView.SALES_PANEL);
                    } else if (errorMessage != null) {
                        System.err.println("Error message: " + errorMessage);
                        JOptionPane.showMessageDialog(NewSaleForm.this,
                                errorMessage,
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println("Exception in done(): " + e.getMessage());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(NewSaleForm.this,
                            "Erro ao processar venda: " + e.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    // Restore the default cursor
                    setCursor(Cursor.getDefaultCursor());
                    newSaleBtn.setEnabled(true);
                    initForNewSale();
                }
            }
        }.execute();
    }//GEN-LAST:event_newSaleBtnActionPerformed

    /**
     * Handles the action when the Cancel button is clicked. Returns to the
     * SalesForm panel without saving changes.
     *
     * @param evt The action event
     */
    private void cancelSaleBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelSaleBtnActionPerformed
        // Return to the SalesForm panel in MainAppView
        MainAppView.redirectToPanel(MainAppView.SALES_PANEL);
    }//GEN-LAST:event_cancelSaleBtnActionPerformed

    /**
     * Handles the action when the Find Customer button is clicked. Opens a
     * customer selection dialog and updates the selected customer. Uses
     * SwingWorker to perform the operation in the background.
     *
     * @param evt The action event
     */
    private void findCustomerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findCustomerBtnActionPerformed
        // Show loading cursor
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        // Using SwingWorker to perform customer selection in background
        SwingWorker<Customer, Void> worker = new SwingWorker<Customer, Void>() {
            @Override
            protected Customer doInBackground() throws Exception {
                // Use CustomerSelectionDialog to select a customer
                Customer selected = CustomerSelectionDialog.selectCustomer(
                        NewSaleForm.this);
                // If a customer was selected, use the controller to get the full customer data
                if (selected != null && selected.getId() != null) {
                    return customerController.findCustomerById(selected.getId());
                }
                return selected;
            }

            @Override
            protected void done() {
                try {
                    // Get the selected customer
                    Customer customer = get();
                    if (customer != null) {
                        // Set the selected customer
                        selectedCustomer = customer;
                        // Update the name field with formatted customer info
                        nameField.setText(
                                customerController.getCustomerDisplayString(
                                        customer));
                    }
                } catch (InterruptedException | ExecutionException e) {
                    JOptionPane.showMessageDialog(NewSaleForm.this,
                            "Erro ao selecionar cliente: " + e.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    // Restore cursor
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };
        // Execute the worker
        worker.execute();
    }//GEN-LAST:event_findCustomerBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addItemBtn;
    private javax.swing.JButton cancelSaleBtn;
    private javax.swing.JTextField discountField;
    private javax.swing.JLabel discountLbl;
    private javax.swing.JButton findCustomerBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLbl;
    private javax.swing.JButton newSaleBtn;
    private javax.swing.JTable newSaleTable;
    private javax.swing.JLabel paymentDateLbl;
    private javax.swing.JComboBox<String> paymentMethodCmb;
    private javax.swing.JFormattedTextField paymentMethodField;
    private javax.swing.JLabel paymentMethodLbl;
    private javax.swing.JPanel sellInformationPnl;
    private javax.swing.JLabel subtotalLbl;
    private javax.swing.JScrollPane tableScrollPanel;
    private javax.swing.JLabel totalLbl;
    private javax.swing.JPanel valuesPnl;
    // End of variables declaration//GEN-END:variables
}
