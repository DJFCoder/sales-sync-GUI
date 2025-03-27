package br.com.devjf.salessync.view.components.table;

import br.com.devjf.salessync.view.components.style.ViewComponentStyle;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * A reusable button renderer for JTable cells.
 * This class provides a button that can be displayed in a table cell with
 * customizable appearance.
 */
public class TableDeleteButtonRenderer extends JButton implements TableCellRenderer {
    
    /**
     * Creates a new button renderer for table cells.
     * 
     * @param text The text to display on the button
     */
    public TableDeleteButtonRenderer(String text) {
        setText(text);
        setOpaque(true);
        setBackground(new java.awt.Color(175, 76, 78));
        setForeground(new java.awt.Color(255, 255, 255));
        ViewComponentStyle.standardCornerRadius(this);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}