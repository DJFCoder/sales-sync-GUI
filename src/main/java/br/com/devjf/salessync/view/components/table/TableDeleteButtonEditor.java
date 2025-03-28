package br.com.devjf.salessync.view.components.table;

import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.table.DefaultTableModel;

/**
 * A reusable button editor for JTable cells. This class provides a button that
 * can be used in a table cell with customizable appearance and behavior.
 */
public class TableDeleteButtonEditor extends AbstractButtonEditor {
    private final DefaultTableModel tableModel;
    private final Runnable afterRemoveAction;
    private final Runnable emptyTableAction;

    /**
     * Creates a new button editor for table cells.
     *
     * @param buttonText The text to display on the button
     * @param tableModel The table model that contains the data
     * @param afterRemoveAction Action to run after removing a row (if table is
     * not empty)
     * @param emptyTableAction Action to run if the table becomes empty after
     * removal
     */
    public TableDeleteButtonEditor(String buttonText, DefaultTableModel tableModel,
            Runnable afterRemoveAction, Runnable emptyTableAction) {
        super(buttonText, new Color(175, 76, 78), Color.WHITE);
        this.tableModel = tableModel;
        this.afterRemoveAction = afterRemoveAction;
        this.emptyTableAction = emptyTableAction;
        
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
            fireEditingStopped();
        });
    }
}
