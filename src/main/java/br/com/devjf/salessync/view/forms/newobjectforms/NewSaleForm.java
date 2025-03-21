package br.com.devjf.salessync.view.forms.newobjectforms;

import java.awt.Component;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import br.com.devjf.salessync.model.Customer;
import br.com.devjf.salessync.model.PaymentMethod;
import br.com.devjf.salessync.model.Sale;
import br.com.devjf.salessync.model.SaleItem;
import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.util.UserSession;
import br.com.devjf.salessync.util.ViewUtil;
import br.com.devjf.salessync.util.TableButtonEditor;
import br.com.devjf.salessync.util.TableButtonRenderer;
import java.awt.HeadlessException;

public class NewSaleForm extends javax.swing.JFrame {
    
    private DefaultTableModel tableModel;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    
    // Sale object attributes
    private Sale saleToCreate;
    private Customer selectedCustomer;
    private final List<SaleItem> saleItems = new ArrayList<>();
    private User currentUser;
    
    public NewSaleForm() {
        initComponents();
        setupTable();
        initializeSale();
        
        // Adicionar listener para o campo de desconto
        discountField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
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
     * Initialize the sale object with default values
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
            currentUser = UserSession.getInstance().getLoggedUser();
            saleToCreate.setUser(currentUser);
        } catch (Exception e) {
            System.err.println("Error getting current user: " + e.getMessage());
        }
    }
    
    /**
     * Prepare the sale object from form data
     * @return Sale object ready to be sent to controller
     */
    private Sale prepareSaleObject() {
        // Make sure we have a sale object
        if (saleToCreate == null) {
            initializeSale();
        }
        
        // Set customer
        if (selectedCustomer != null) {
            saleToCreate.setCustomer(selectedCustomer);
        } else {
            // Handle case where customer is not selected
            throw new IllegalStateException("Cliente deve ser selecionado para criar uma venda");
        }
        
        // Set payment method
        String selectedPaymentMethodStr = paymentMethodCmb.getSelectedItem().toString();
        PaymentMethod paymentMethod;
        
        // Map the combo box values to PaymentMethod enum values
        switch (selectedPaymentMethodStr) {
            case "DINHEIRO":
                paymentMethod = PaymentMethod.CASH;
                break;
            case "CRÉDITO":
                paymentMethod = PaymentMethod.CREDIT_CARD;
                break;
            case "DÉBITO":
                paymentMethod = PaymentMethod.DEBIT_CARD;
                break;
            case "TRANSFERÊNCIA":
                paymentMethod = PaymentMethod.BANK_TRANSFER;
                break;
            case "PIX":
                paymentMethod = PaymentMethod.PIX;
                break;
            case "BOLETO":
                paymentMethod = PaymentMethod.BANK_SLIP;
                break;
            case "CHEQUE":
                paymentMethod = PaymentMethod.PAYCHECK;
                break;
            default:
                throw new IllegalStateException("Forma de pagamento inválida");
        }
        
        saleToCreate.setPaymentMethod(paymentMethod);
        
        // Set payment date if available
        if (paymentMethodField.getValue() != null) {
            try {
                // Convert the date from the field to LocalDateTime
                Date paymentDate = (Date) paymentMethodField.getValue();
                saleToCreate.setPaymentDate(paymentDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime());
            } catch (Exception e) {
                System.err.println("Error converting payment date: " + e.getMessage());
            }
        }
        
        // Create sale items from table data
        saleItems.clear();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String description = (String) tableModel.getValueAt(i, 0);
            
            // Skip empty rows
            if (description == null || description.trim().isEmpty()) {
                continue;
            }
            
            // Get quantity
            Integer quantity = 1;
            Object quantityObj = tableModel.getValueAt(i, 1);
            if (quantityObj instanceof Integer) {
                quantity = (Integer) quantityObj;
            } else if (quantityObj instanceof String) {
                try {
                    quantity = Integer.valueOf(quantityObj.toString().trim());
                } catch (NumberFormatException e) {
                    // Use default quantity of 1
                }
            }
            
            // Get unit price
            Double unitPrice = 0.0;
            Object priceObj = tableModel.getValueAt(i, 2);
            if (priceObj instanceof Double) {
                unitPrice = (Double) priceObj;
            } else if (priceObj instanceof String) {
                try {
                    String priceStr = priceObj.toString().replaceAll("[^\\d,.]", "").replace(",", ".");
                    unitPrice = Double.valueOf(priceStr);
                } catch (NumberFormatException e) {
                    // Use default price of 0.0
                }
            }
            
            // Create and add the sale item
            SaleItem item = new SaleItem();
            item.setDescription(description.trim());
            item.setQuantity(quantity);
            item.setUnitPrice(unitPrice);
            
            saleItems.add(item);
        }
        
        // Add all items to the sale
        saleToCreate.getItems().clear();
        for (SaleItem item : saleItems) {
            saleToCreate.addItem(item);
        }
        
        // Set subtotal, discount, and total amounts
        double subtotal = calculateSubtotal();
        double discount = getDiscount();
        double total = subtotal - discount;
        
        saleToCreate.setSubtotalAmount(subtotal);
        saleToCreate.setDiscountAmount(discount);
        saleToCreate.setTotalAmount(total);
        
        return saleToCreate;
    }

    // Método para calcular e atualizar todos os totais
    private void updateTotals() {
        double subtotal = calculateSubtotal();
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
    }
    
    // Método para calcular o subtotal de todas as linhas
    private double calculateSubtotal() {
        double sum = 0.0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object value = tableModel.getValueAt(i, 3); // Coluna do subtotal
            if (value != null) {
                if (value instanceof Double) {
                    sum += (Double) value;
                } else if (value instanceof String) {
                    try {
                        // Remover símbolos de moeda e converter para double
                        String valueStr = value.toString().replaceAll("[^\\d,.]", "").replace(",", ".");
                        sum += Double.parseDouble(valueStr);
                    } catch (NumberFormatException e) {
                        // Ignorar erros de formato
                    }
                }
            }
        }
        return sum;
    }

    // Método para obter o valor do desconto
    private double getDiscount() {
        try {
            String discountText = discountField.getText().trim();
            if (discountText.isEmpty()) {
                return 0.0;
            }
            // Remover símbolos de moeda e converter para double
            discountText = discountText.replaceAll("[^\\d,.]", "").replace(",", ".");
            return Double.parseDouble(discountText);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    private void setupTable() {
        // Configurar o modelo da tabela
        tableModel = new DefaultTableModel(
            new Object [][] {},
            new String [] {"Descrição", "Quantidade", "Preço Unitário", "Subtotal", "Ações"}
        ) {
            Class[] types = new Class [] {
                String.class, Integer.class, Double.class, Double.class, Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, false, true
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        
        newSaleTable.setModel(tableModel);
        
        // Configurar o renderizador e editor para a coluna de ações (botão remover)
        TableColumn actionColumn = newSaleTable.getColumnModel().getColumn(4);
        actionColumn.setCellRenderer(new TableButtonRenderer("Remover"));
        actionColumn.setCellEditor(new TableButtonEditor("Remover", tableModel, 
                                   () -> updateTotals(), // ação após remover (se tabela não estiver vazia)
                                   () -> addNewRow()     // ação se tabela ficar vazia
                                  ));
        
        // Configurar o renderizador para a coluna de preço unitário e subtotal
        newSaleTable.getColumnModel().getColumn(2).setCellRenderer(new CurrencyRenderer());
        newSaleTable.getColumnModel().getColumn(3).setCellRenderer(new CurrencyRenderer());
        
        // Adicionar listener para atualizar o subtotal quando quantidade ou preço mudar
        tableModel.addTableModelListener(e -> {
            // Verificar se a linha ainda existe antes de tentar atualizar
            int row = e.getFirstRow();
            if (row >= 0 && row < tableModel.getRowCount() && (e.getColumn() == 1 || e.getColumn() == 2)) {
                updateSubtotal(row);
            }
        });
        
        // Ajustar larguras das colunas
        newSaleTable.getColumnModel().getColumn(0).setPreferredWidth(300); // Descrição
        newSaleTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Quantidade
        newSaleTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Preço Unitário
        newSaleTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Subtotal
        newSaleTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Ações
        
        // Adicionar uma linha inicial à tabela
        addNewRow();
        
        // Inicializar os valores
        updateTotals();
    }
    
    private void updateSubtotal(int row) {
        if (row < 0 || row >= tableModel.getRowCount()) return;
        
        try {
            Object quantityObj = tableModel.getValueAt(row, 1);
            Object priceObj = tableModel.getValueAt(row, 2);
            
            if (quantityObj != null && priceObj != null) {
                int quantity = 0;
                double price = 0.0;
                
                if (quantityObj instanceof Integer) {
                    quantity = (Integer) quantityObj;
                } else if (quantityObj instanceof String) {
                    quantity = Integer.parseInt(quantityObj.toString());
                }
                
                if (priceObj instanceof Double) {
                    price = (Double) priceObj;
                } else if (priceObj instanceof String) {
                    // Remover símbolos de moeda e converter para double
                    String priceStr = priceObj.toString().replaceAll("[^\\d,.]", "").replace(",", ".");
                    price = Double.parseDouble(priceStr);
                }
                
                double subtotal = quantity * price;
                tableModel.setValueAt(subtotal, row, 3);
                
                // Atualizar os totais após modificar o subtotal de uma linha
                updateTotals();
            }
        } catch (NumberFormatException e) {
            // Ignorar erros de formato
        }
    }
    
    private void addNewRow() {
        tableModel.addRow(new Object[]{"", 1, 0.0, 0.0, null});
        updateTotals();
    }
    
    // Renderizador para valores monetários
    class CurrencyRenderer extends DefaultTableCellRenderer {
        public CurrencyRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Double) {
                value = currencyFormat.format(value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
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
        cancelSaleButton = new javax.swing.JButton();
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
        newSaleBtn.setText("Nova Venda");
        newSaleBtn.setPreferredSize(new java.awt.Dimension(150, 40));
        ViewUtil.standardCornerRadius(newSaleBtn);
        newSaleBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newSaleBtnActionPerformed(evt);
            }
        });

        sellInformationPnl.setPreferredSize(new java.awt.Dimension(917, 50));
        ViewUtil.standardCornerRadius(sellInformationPnl);

        paymentMethodLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        paymentMethodLbl.setText("Forma de Pagamento:");

        paymentDateLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        paymentDateLbl.setText("Data de Pagamento:");

        nameLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        nameLbl.setText("Nome:");

        nameField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        nameField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nameField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewUtil.standardCornerRadius(nameField);

        findCustomerBtn.setBackground(new java.awt.Color(96, 125, 139));
        findCustomerBtn.setForeground(new java.awt.Color(255, 255, 255));
        findCustomerBtn.setText("Buscar");
        findCustomerBtn.setPreferredSize(new java.awt.Dimension(75, 30));
        ViewUtil.standardCornerRadius(findCustomerBtn);

        paymentMethodCmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "DINHEIRO", "CRÉDITO", "DÉBITO", "TRANSFERÊNCIA", "PIX", "BOLETO", "CHEQUE" }));
        paymentMethodCmb.setPreferredSize(new java.awt.Dimension(140, 30));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Informações da Venda");

        try {
            paymentMethodField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        paymentMethodField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        paymentMethodField.setPreferredSize(new java.awt.Dimension(120, 30));
        ViewUtil.standardCornerRadius(paymentMethodField);

        javax.swing.GroupLayout sellInformationPnlLayout = new javax.swing.GroupLayout(sellInformationPnl);
        sellInformationPnl.setLayout(sellInformationPnlLayout);
        sellInformationPnlLayout.setHorizontalGroup(
            sellInformationPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sellInformationPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sellInformationPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.CENTER, sellInformationPnlLayout.createSequentialGroup()
                        .addComponent(nameLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(findCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paymentMethodLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paymentMethodCmb, 0, 186, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paymentDateLbl)
                        .addGap(1, 1, 1)
                        .addComponent(paymentMethodField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.CENTER))
                .addContainerGap())
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
        ViewUtil.standardCornerRadius(addItemBtn);

        cancelSaleButton.setBackground(new java.awt.Color(175, 76, 78));
        cancelSaleButton.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        cancelSaleButton.setForeground(new java.awt.Color(255, 255, 255));
        cancelSaleButton.setText("Cancelar Venda");
        cancelSaleButton.setPreferredSize(new java.awt.Dimension(150, 40));
        ViewUtil.standardCornerRadius(cancelSaleButton);
        cancelSaleButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelSaleButtonActionPerformed(evt);
            }
        });

        subtotalLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        subtotalLbl.setText("Subtotal: R$");

        discountLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        discountLbl.setText("Desconto: R$");

        discountField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        discountField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        discountField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewUtil.standardCornerRadius(nameField);

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
                        .addComponent(cancelSaleButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(cancelSaleButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void addItemBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemBtnActionPerformed
        addNewRow();
    }//GEN-LAST:event_addItemBtnActionPerformed

    private void newSaleBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newSaleBtnActionPerformed
        try {
            // Validate form data
            if (selectedCustomer == null) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor, selecione um cliente para a venda.", 
                    "Cliente não selecionado", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Check if there are valid items in the table
            boolean hasValidItems = false;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String description = (String) tableModel.getValueAt(i, 0);
                if (description != null && !description.trim().isEmpty()) {
                    hasValidItems = true;
                    break;
                }
            }
            
            if (!hasValidItems) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor, adicione pelo menos um item à venda.", 
                    "Itens não adicionados", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Prepare the sale object
            Sale sale = prepareSaleObject();
            
            // SaleController controller = new SaleController();
            // controller.createSale(sale);
            
            // Show success message
            JOptionPane.showMessageDialog(this, 
                "Venda registrada com sucesso!", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Close the form or reset it for a new sale
            dispose();
            
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao registrar a venda:\n" + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_newSaleBtnActionPerformed

    private void cancelSaleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelSaleButtonActionPerformed
        
    }//GEN-LAST:event_cancelSaleButtonActionPerformed
    
    // new NewSaleForm().setVisible(true);

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addItemBtn;
    private javax.swing.JButton cancelSaleButton;
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
