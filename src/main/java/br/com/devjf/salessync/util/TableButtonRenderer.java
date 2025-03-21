package br.com.devjf.salessync.util;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * A reusable button renderer for JTable cells.
 * This class provides a button that can be displayed in a table cell with
 * customizable appearance.
 */
public class TableButtonRenderer extends JButton implements TableCellRenderer {
    
    /**
     * Creates a new button renderer for table cells.
     * 
     * @param text The text to display on the button
     */
    public TableButtonRenderer(String text) {
        setText(text);
        setOpaque(true);
        setBackground(new java.awt.Color(175, 76, 78));
        setForeground(new java.awt.Color(255, 255, 255));
        ViewUtil.standardCornerRadius(this);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}