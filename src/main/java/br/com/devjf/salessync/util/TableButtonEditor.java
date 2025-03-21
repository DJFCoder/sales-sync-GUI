package br.com.devjf.salessync.util;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.DefaultTableModel;

/**
 * A reusable button editor for JTable cells.
 * This class provides a button that can be used in a table cell with customizable
 * appearance and behavior.
 */
public class TableButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private final JButton button;
    private int clickedRow;
    private final DefaultTableModel tableModel;
    private final Runnable afterRemoveAction;
    private final Runnable emptyTableAction;
    
    /**
     * Creates a new button editor for table cells.
     * 
     * @param buttonText The text to display on the button
     * @param tableModel The table model that contains the data
     * @param afterRemoveAction Action to run after removing a row (if table is not empty)
     * @param emptyTableAction Action to run if the table becomes empty after removal
     */
    public TableButtonEditor(String buttonText, DefaultTableModel tableModel, 
                            Runnable afterRemoveAction, Runnable emptyTableAction) {
        this.tableModel = tableModel;
        this.afterRemoveAction = afterRemoveAction;
        this.emptyTableAction = emptyTableAction;
        
        button = new JButton(buttonText);
        button.setOpaque(true);
        button.setBackground(new java.awt.Color(175, 76, 78));
        button.setForeground(new java.awt.Color(255, 255, 255));
        ViewUtil.standardCornerRadius(button);
        
        button.addActionListener((ActionEvent e) -> {
            // Verificar se a linha existe antes de remover
            if (clickedRow >= 0 && clickedRow < tableModel.getRowCount()) {
                // Remover a linha
                tableModel.removeRow(clickedRow);
                
                // Se a tabela ficou vazia, executar a ação para tabela vazia
                if (tableModel.getRowCount() == 0) {
                    if (emptyTableAction != null) {
                        emptyTableAction.run();
                    }
                } else {
                    // Executar a ação após remoção
                    if (afterRemoveAction != null) {
                        afterRemoveAction.run();
                    }
                }
            }
            // Encerrar a edição
            cancelCellEditing();
        });
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