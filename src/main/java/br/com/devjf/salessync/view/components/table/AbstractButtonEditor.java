package br.com.devjf.salessync.view.components.table;

import java.awt.Component;
import java.awt.Color;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * Base class for button editors in table cells.
 * This abstract class provides common functionality for button editors.
 */
public abstract class AbstractButtonEditor extends AbstractCellEditor implements TableCellEditor {
    protected JButton button;
    protected int clickedRow;
    
    /**
     * Creates a new button editor with the specified text and colors.
     * 
     * @param text The text to display on the button
     * @param backgroundColor The background color of the button
     * @param foregroundColor The foreground (text) color of the button
     */
    public AbstractButtonEditor(String text, Color backgroundColor, Color foregroundColor) {
        button = new JButton(text);
        button.setOpaque(true);
        button.setBackground(backgroundColor);
        button.setForeground(foregroundColor);
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        clickedRow = row;
        return button;
    }
    
    @Override
    public Object getCellEditorValue() {
        return "";
    }
}