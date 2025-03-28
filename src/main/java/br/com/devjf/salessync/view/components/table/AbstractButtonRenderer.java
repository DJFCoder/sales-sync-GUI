package br.com.devjf.salessync.view.components.table;

import java.awt.Component;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Base class for button renderers in table cells.
 * This abstract class provides common functionality for button renderers.
 */
public class AbstractButtonRenderer extends JButton implements TableCellRenderer {
    
    /**
     * Creates a new button renderer with the specified text and colors.
     * 
     * @param text The text to display on the button
     * @param backgroundColor The background color of the button
     * @param foregroundColor The foreground (text) color of the button
     */
    public AbstractButtonRenderer(String text, Color backgroundColor, Color foregroundColor) {
        setText(text);
        setOpaque(true);
        setBackground(backgroundColor);
        setForeground(foregroundColor);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}