package br.com.devjf.salessync.view.forms.newobjectforms;

import java.awt.Cursor;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import br.com.devjf.salessync.view.forms.ReportsForm;
import br.com.devjf.salessync.view.forms.SalesForm;
import br.com.devjf.salessync.view.forms.validators.SaleFormValidator;
import java.util.HashMap;
import java.util.Map;

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
    private int currentUserId;
    // Controllers
    private final SaleController saleController;
    private final CustomerController customerController;
    // Table manager
    private SaleTableManager tableManager;
    // Flag to track if we're editing an existing sale
    private boolean isEditMode;

    /**
     * Creates a new form for registering a sale. Initializes controllers, sets
     * up the table, and prepares the form for a new sale.
     */
    public NewSaleForm() {
        // Initialize controllers
        this.saleController = new SaleController();
        this.customerController = new CustomerController();
        isEditMode = false;
        initComponents();
        // Initialize table manager
        initTableManager();
        // Configurar o formulário para uma nova venda
        this.tableManager = new SaleTableManager(newSaleTable,
                () -> this.updateTotals());
        initNewSale();
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
     * @param saleId a sale to edit
     */
    public NewSaleForm(Integer saleId) {
        // Initialize components
        initComponents();
        // Initialize controllers first
        this.saleController = new SaleController();
        this.customerController = new CustomerController();
        // Set edit mode flag
        this.isEditMode = true;
        try {
            // Debug output
            System.out.println("Fetching sale with ID: " + saleId);
            // Check if the controller is properly initialized
            if (saleController == null) {
                throw new IllegalStateException(
                        "Sale controller is not initialized");
            }
            // Try multiple approaches to fetch the sale
            boolean saleFound = false;
            // Approach 1: Try findSaleByIdWithItems
            try {
                saleToCreate = saleController.findSaleByIdWithItems(saleId);
                if (saleToCreate != null) {
                    saleFound = true;
                    System.out.println(
                            "Sale found using findSaleByIdWithItems");
                }
            } catch (Exception e) {
                System.err.println(
                        "Error with findSaleByIdWithItems: " + e.getMessage());
            }
            // Approach 2: Try findSaleByIdForEdit if first approach failed
            if (!saleFound) {
                try {
                    saleToCreate = saleController.findSaleByIdForEdit(saleId);
                    if (saleToCreate != null) {
                        saleFound = true;
                        System.out.println(
                                "Sale found using findSaleByIdForEdit");
                    }
                } catch (Exception e) {
                    System.err.println(
                            "Error with findSaleByIdForEdit: " + e.getMessage());
                }
            }
            // Approach 3: Try a direct database query as last resort
            if (!saleFound) {
                try {
                    // This assumes you have a method to find a sale by ID directly
                    saleToCreate = saleController.findSaleById(saleId);
                    if (saleToCreate != null) {
                        saleFound = true;
                        System.out.println("Sale found using findSaleById");
                        // Since we used the simple find method, we need to manually load items
                        // This should be implemented in your SaleController
                        try {
                            List<SaleItem> items = saleController.findItemsBySaleId(
                                    saleId);
                            if (items != null) {
                                saleToCreate.setItems(items);
                                System.out.println(
                                        "Items loaded separately: " + items.size());
                            }
                        } catch (Exception e) {
                            System.err.println(
                                    "Error loading items separately: " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    System.err.println(
                            "Error with findSaleById: " + e.getMessage());
                }
            }
            // Check if sale was found
            if (saleToCreate == null) {
                System.out.println("Sale not found with ID: " + saleId);
                throw new IllegalArgumentException(
                        "Venda com ID " + saleId + " não encontrada");
            }
            // Debug output
            System.out.println(
                    "Sale found successfully: " + saleToCreate.getId());
            // Initialize table manager before loading data
            initTableManager();
            // Set selected customer and user ID
            selectedCustomer = saleToCreate.getCustomer();
            currentUserId = UserSessionManager.getInstance().getLoggedUser().getId();
            // Load sale data into the form
            loadSaleData();
            // Add listener for the discount field
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
            // Change button text to "Update"
            newSaleBtn.setText("Atualizar");
        } catch (IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar venda: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            // Return to sales panel if there's an error
            MainAppView.redirectToPanel(MainAppView.SALES_PANEL);
        }
    }

    /**
     * Ensures the table manager is initialized. This method can be called
     * multiple times safely.
     */
    private void initTableManager() {
        if (this.tableManager == null && newSaleTable != null) {
            // Use updateTotalsWithoutCheck to avoid circular dependency
            this.tableManager = new SaleTableManager(newSaleTable,
                    () -> this.updateTotals());
        }
    }

    /**
     * Initializes the form for a new sale. Clears all fields, resets the sale
     * object, and prepares the form for data entry.
     */
    private void initNewSale() {
        // Set edit mode to false
        isEditMode = false;
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
        paymentDateField.setText(formattedDate);
    }

    /**
     * Calculates and updates all total values displayed in the form.
     */
    private void updateTotals() {
        try {
            if (tableManager == null) {
                return; // Skip if tableManager is not initialized yet
            }
            // Calculate subtotal
            double subtotal = tableManager.calculateSubtotal();
            double discount = getDiscount();
            double total = subtotal - discount; // Consistent calculation
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
     * Updates the sale items in the saleToCreate object from the current table
     * data before saving or updating the sale.
     */
    private void updateSaleItems() {
        if (saleToCreate == null || tableManager == null) {
            return;
        }
        // Get the current items from the table
        List<SaleItemDTO> tableItems = tableManager.getSaleItemsFromTable();
        // If we're in edit mode, update existing items with new quantities
        if (isEditMode && saleToCreate.getItems() != null) {
            // Create a map of existing items by description for quick lookup
            Map<String, SaleItem> existingItemsMap = new HashMap<>();
            for (SaleItem item : saleToCreate.getItems()) {
                existingItemsMap.put(item.getDescription(),
                        item);
            }
            // Update quantities for existing items
            for (SaleItemDTO dto : tableItems) {
                SaleItem existingItem = existingItemsMap.get(
                        dto.getDescription());
                if (existingItem != null) {
                    // Update the quantity and unit price
                    existingItem.setQuantity(dto.getQuantity());
                    existingItem.setUnitPrice(dto.getPrice());
                    System.out.println(
                            "Updated item: " + existingItem.getDescription()
                            + " - Quantity: " + existingItem.getQuantity());
                } else {
                    // This is a new item, add it to the sale
                    SaleItem newItem = new SaleItem();
                    newItem.setDescription(dto.getDescription());
                    newItem.setQuantity(dto.getQuantity());
                    newItem.setUnitPrice(dto.getPrice());
                    newItem.setSale(saleToCreate);
                    saleToCreate.getItems().add(newItem);
                    System.out.println(
                            "Added new item: " + newItem.getDescription());
                }
            }
            // Remove items that are no longer in the table
            saleToCreate.getItems().removeIf(item
                    -> tableItems.stream().noneMatch(dto
                            -> dto.getDescription().equals(item.getDescription())
                    )
            );
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
     * Loads sale data into the form fields when in edit mode
     */
    private void loadSaleData() {
        try {
            // Load customer data
            if (saleToCreate.getCustomer() != null) {
                nameField.setText(saleToCreate.getCustomer().getName());
                System.out.println("carregou cliente...");
            }
            // Load payment method
            if (saleToCreate.getPaymentMethod() != null) {
                // Use mapPaymentMethodToDisplay to get the display string
                String displayValue = mapPaymentMethodToDisplay(
                        saleToCreate.getPaymentMethod());
                paymentMethodCmb.setSelectedItem(displayValue);
                System.out.println("carregou a forma de pagamento...");
            }
            // Load payment date
            if (saleToCreate.getPaymentDate() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date paymentDate = Date.from(saleToCreate.getPaymentDate()
                        .atZone(ZoneId.systemDefault()).toInstant());
                paymentDateField.setText(dateFormat.format(paymentDate));
                System.out.println("inseriu a data de pagamento...");
            }
            // Load discount - Make sure to format it properly
            if (saleToCreate.getDiscountAmount() != null) {
                // Format the discount to match your locale settings
                NumberFormat formatter = NumberFormat.getNumberInstance();
                formatter.setMinimumFractionDigits(2);
                formatter.setMaximumFractionDigits(2);
                discountField.setText(formatter.format(
                        saleToCreate.getDiscountAmount()));
                System.out.println("carregou o desconto...");
            }
            // Load items into the table
            if (saleToCreate.getItems() != null && !saleToCreate.getItems().isEmpty()) {
                loadSaleItems(saleToCreate);
                System.out.println(
                        "carregou " + saleToCreate.getItems().size() + " itens na tabela...");
            }
            // Update totals after loading all data
            updateTotals();
            System.out.println("atualizou os totais... TUDO OK!");
        } catch (Exception e) {
            System.err.println(
                    "Erro ao carregar dados da venda: " + e.getMessage());
            e.printStackTrace();
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
        if (sale == null || sale.getItems() == null) {
            return;
        }
        tableManager.clearTable();
        saleItems.clear(); // Clear existing items first
        tableManager.loadSaleItems(sale.getItems());
        // Add items to our tracking list
        for (SaleItem item : sale.getItems()) {
            saleItems.add(item);
        }
        // Update totals after loading items
        updateTotals();
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
        paymentDateField = new javax.swing.JFormattedTextField();
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
            paymentDateField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        paymentDateField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        paymentDateField.setPreferredSize(new java.awt.Dimension(114, 30));
        ViewComponentStyle.standardCornerRadius(paymentDateField);

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
                        .addComponent(paymentDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(paymentDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        ViewComponentStyle.standardCornerRadius(valuesPnl);

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
        // Disable the button and show wait cursor to indicate processing
        newSaleBtn.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        // Use SwingWorker to perform database operation in background
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            private String errorMessage = null;

            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    // Get current user ID
                    currentUserId = UserSessionManager.getInstance().getLoggedUser().getId();
                    // Get items from table
                    List<SaleItemDTO> items = tableManager.getSaleItemsFromTable();
                    // Calculate amounts
                    double subtotal = tableManager.calculateSubtotal();
                    Double discount = getDiscount();
                    // Validate form inputs using SaleFormValidator
                    SaleFormValidator.validateSaleForm(selectedCustomer,
                            paymentMethodCmb.getSelectedItem().toString(),
                            paymentDateField,
                            newSaleTable,
                            discount.toString(),
                            subtotal);
                    if (isEditMode) {
                        // Update the sale items from the table before saving
                        updateSaleItems();
                        // Update payment method
                        String selectedPaymentMethod = paymentMethodCmb.getSelectedItem().toString();
                        if (!selectedPaymentMethod.equals("Selecione")) {
                            saleToCreate.setPaymentMethod(PaymentMethod.valueOf(
                                    selectedPaymentMethod));
                            System.out.println(
                                    "Updated payment method to: " + selectedPaymentMethod);
                        }
                        // Update existing sale
                        saleToCreate = saleController.updateSale(saleToCreate);
                    } else {
                        // Create new sale using the controller's prepareNewSale method
                        saleToCreate = saleController.prepareNewSale(
                                selectedCustomer,
                                paymentMethodCmb.getSelectedItem().toString(),
                                paymentDateField.getText(),
                                items,
                                subtotal,
                                discount,
                                currentUserId
                        );
                        // Register the prepared sale
                        saleToCreate = saleController.registerSale(saleToCreate);
                    }
                    return saleToCreate != null;
                } catch (IllegalStateException | ParseException e) {
                    errorMessage = e.getMessage();
                    return false;
                }
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        // Show success message
                        JOptionPane.showMessageDialog(NewSaleForm.this,
                                isEditMode ? "Venda atualizada com sucesso!" : "Venda registrada com sucesso!",
                                "Sucesso",
                                JOptionPane.INFORMATION_MESSAGE);
                        // Refresh the sales table in SalesForm
                        refreshSalesTable();
                        // Return to the SalesForm panel
                        MainAppView.redirectToPanel(MainAppView.SALES_PANEL);
                    } else {
                        // Show error message
                        JOptionPane.showMessageDialog(NewSaleForm.this,
                                errorMessage != null ? errorMessage : "Erro ao " + (isEditMode ? "atualizar" : "registrar") + " venda.",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    JOptionPane.showMessageDialog(NewSaleForm.this,
                            "Erro ao processar operação: " + e.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    // Restore cursor and enable button regardless of outcome
                    setCursor(Cursor.getDefaultCursor());
                    newSaleBtn.setEnabled(true);
                    ReportsForm.getInstance().updateReportValues();
                    initNewSale();
                }
            }
        };
        // Start the background task
        worker.execute();
    }//GEN-LAST:event_newSaleBtnActionPerformed

    /**
     * Handles the action when the Cancel button is clicked. Returns to the
     * SalesForm panel without saving changes.
     *
     * @param evt The action event
     */
    private void cancelSaleBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelSaleBtnActionPerformed
        // Return to the SalesForm panel in MainAppView
        initNewSale();
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
    private javax.swing.JFormattedTextField paymentDateField;
    private javax.swing.JLabel paymentDateLbl;
    private javax.swing.JComboBox<String> paymentMethodCmb;
    private javax.swing.JLabel paymentMethodLbl;
    private javax.swing.JPanel sellInformationPnl;
    private javax.swing.JLabel subtotalLbl;
    private javax.swing.JScrollPane tableScrollPanel;
    private javax.swing.JLabel totalLbl;
    private javax.swing.JPanel valuesPnl;
    // End of variables declaration//GEN-END:variables
}
