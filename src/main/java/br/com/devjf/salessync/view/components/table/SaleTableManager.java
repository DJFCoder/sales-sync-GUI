package br.com.devjf.salessync.view.components.table;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import br.com.devjf.salessync.model.SaleItem;
import br.com.devjf.salessync.view.components.renderers.CurrencyRenderer;

/**
 * Manages the sale items table operations for the NewSaleForm. Handles table
 * setup, row operations, and calculations.
 */
public class SaleTableManager {
    private final JTable table;
    private DefaultTableModel tableModel;
    private final NumberFormat currencyFormat;
    private final Runnable updateTotalsCallback;

    /**
     * Creates a new SaleTableManager for the specified table.
     *
     * @param table The JTable to manage
     * @param updateTotalsCallback Callback to update totals when table changes
     */
    public SaleTableManager(JTable table, Runnable updateTotalsCallback) {
        this.table = table;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt",
                "BR"));
        this.updateTotalsCallback = updateTotalsCallback;
        setupTable();
    }

    /**
     * Sets up the sale items table with appropriate columns, renderers, and
     * listeners.
     */
    private void setupTable() {
        // Configurar o modelo da tabela
        tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Descrição", "Quantidade", "Preço Unitário", "Subtotal", "Ações"}) {
            Class<?>[] types = new Class<?>[]{
                String.class, Integer.class, Double.class, Double.class, Object.class
            };
            boolean[] canEdit = new boolean[]{
                true, true, true, false, true
            };

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        table.setModel(tableModel);
        // Configurar o renderizador e editor para a coluna de ações (botão remover)
        TableColumn actionColumn = table.getColumnModel().getColumn(4);
        actionColumn.setCellRenderer(new TableDeleteButtonRenderer("Remover"));
        actionColumn.setCellEditor(new TableDeleteButtonEditor("Remover",
                tableModel,
                table, // Pass the JTable reference
                () -> updateTotalsCallback.run(), // action after remove row (if table isn't empty)
                () -> addNewRow() // action if table is empty
        ));
        // Configurar o renderizador para a coluna de preço unitário e subtotal
        table.getColumnModel().getColumn(2).setCellRenderer(
                new CurrencyRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(
                new CurrencyRenderer());
        // Adicionar listener para atualizar o subtotal quando quantidade ou preço mudar
        tableModel.addTableModelListener(e -> {
            // Verificar se a linha ainda existe antes de tentar atualizar
            int row = e.getFirstRow();
            if (row >= 0 && row < tableModel.getRowCount() && (e.getColumn() == 1 || e.getColumn() == 2)) {
                updateSubtotal(row);
            }
        });
        // Ajustar larguras das colunas
        table.getColumnModel().getColumn(0).setPreferredWidth(300); // Descrição
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Quantidade
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // Preço Unitário
        table.getColumnModel().getColumn(3).setPreferredWidth(150); // Subtotal
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Ações
        // Adicionar uma linha inicial à tabela
        addNewRow();
    }

    /**
     * Updates the subtotal for a specific row in the table.
     *
     * @param row The row index to update
     */
    public void updateSubtotal(int row) {
        if (row < 0 || row >= tableModel.getRowCount()) {
            return;
        }
        try {
            Object quantityObj = tableModel.getValueAt(row,
                    1);
            Object priceObj = tableModel.getValueAt(row,
                    2);
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
                    String priceStr = priceObj.toString().replaceAll("[^\\d,.]",
                            "").replace(",",
                                    ".");
                    price = Double.parseDouble(priceStr);
                }
                double subtotal = quantity * price;
                tableModel.setValueAt(subtotal,
                        row,
                        3);
                // Atualizar os totais após modificar o subtotal de uma linha
                updateTotalsCallback.run();
            }
        } catch (NumberFormatException e) {
            // Ignorar erros de formato
        }
    }

    /**
     * Adds a new empty row to the items table.
     */
    public void addNewRow() {
        tableModel.addRow(new Object[]{"", 1, 0.0, 0.0, null});
        updateTotalsCallback.run();
    }

    /**
     * Loads the sale items from a sale into the items table.
     *
     * @param saleItems The list of sale items to load
     */
    public void loadSaleItems(List<SaleItem> saleItems) {
        clearTable();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (SaleItem item : saleItems) {
            Object[] rowData = {
                item.getDescription(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getQuantity() * item.getUnitPrice(), // Subtotal
                "Excluir Item" // Delete button
            };
            model.addRow(rowData);
        }
    }

    /**
     * Calculates the subtotal by summing all item subtotals from the table.
     *
     * @return The calculated subtotal amount
     */
    public double calculateSubtotal() {
        double sum = 0.0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object value = tableModel.getValueAt(i,
                    3); // Coluna do subtotal
            if (value != null) {
                if (value instanceof Double) {
                    sum += (Double) value;
                } else if (value instanceof String) {
                    try {
                        // Remover símbolos de moeda e converter para double
                        String valueStr = value.toString().replaceAll("[^\\d,.]",
                                "").replace(",",
                                        ".");
                        sum += Double.parseDouble(valueStr);
                    } catch (NumberFormatException e) {
                        // Ignorar erros de formato
                    }
                }
            }
        }
        return sum;
    }

    /**
     * Clears all rows from the table.
     */
    public void clearTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
    }

    /**
     * Gets the table model.
     *
     * @return The DefaultTableModel
     */
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
