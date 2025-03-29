package br.com.devjf.salessync.view.components.table;

import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * A reusable button editor for JTable cells. This class provides a button that
 * can be used in a table cell with customizable appearance and behavior.
 */
public class TableDeleteButtonEditor extends AbstractButtonEditor {
    private final DefaultTableModel tableModel;
    private final Runnable afterRemoveAction;
    private final Runnable emptyTableAction;
    private final JTable table;

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
            JTable table, Runnable afterRemoveAction, Runnable emptyTableAction) {
        super(buttonText,
                new Color(175,
                        76,
                        78),
                Color.WHITE);
        this.tableModel = tableModel;
        this.table = table;
        this.afterRemoveAction = afterRemoveAction;
        this.emptyTableAction = emptyTableAction;
        button.addActionListener(e -> {
            try {
                // Get the current row
                int row = table.getEditingRow();
                // First stop editing to prevent the table from trying to update a removed row
                fireEditingStopped();
                // Then check if the row is valid and remove it
                if (row >= 0 && row < tableModel.getRowCount()) {
                    // Remove the row
                    tableModel.removeRow(row);
                    // Check if table is empty after removal
                    if (tableModel.getRowCount() == 0 && emptyTableAction != null) {
                        emptyTableAction.run();
                    } else if (afterRemoveAction != null) {
                        // Run the after remove action if table is not empty
                        afterRemoveAction.run();
                    }
                }
            } catch (Exception ex) {
                // Log the error but don't crash
                System.err.println(
                        "Error in delete button action: " + ex.getMessage());
                ex.printStackTrace();
                fireEditingStopped();
            }
        });
    }
}
