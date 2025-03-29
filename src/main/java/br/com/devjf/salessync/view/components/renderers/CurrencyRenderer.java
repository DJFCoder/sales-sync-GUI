package br.com.devjf.salessync.view.components.renderers;

import java.awt.Component;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Custom renderer for displaying currency values in tables.
 * Formats double values as currency using the Brazilian locale.
 * 
 * @author DevJF
 */
public class CurrencyRenderer extends DefaultTableCellRenderer {
    
    private final NumberFormat currencyFormat;
    
    /**
     * Creates a new currency renderer with right alignment using the default locale.
     */
    public CurrencyRenderer() {
        this(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")));
    }
    
    /**
     * Creates a new currency renderer with right alignment using the specified format.
     * 
     * @param currencyFormat The number format to use for currency formatting
     */
    public CurrencyRenderer(NumberFormat currencyFormat) {
        setHorizontalAlignment(SwingConstants.RIGHT);
        this.currencyFormat = currencyFormat;
    }

    /**
     * Formats the cell value as currency if it's a Double.
     *
     * @param table The JTable
     * @param value The value to be formatted
     * @param isSelected Whether the cell is selected
     * @param hasFocus Whether the cell has focus
     * @param row The row index
     * @param column The column index
     * @return The formatted component
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Double) {
            value = currencyFormat.format(value);
        }
        return super.getTableCellRendererComponent(table,
                value,
                isSelected,
                hasFocus,
                row,
                column);
    }
}