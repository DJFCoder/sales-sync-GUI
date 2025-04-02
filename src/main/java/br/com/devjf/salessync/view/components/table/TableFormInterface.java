package br.com.devjf.salessync.view.components.table;

import javax.swing.JTable;

/**
 * Interface for standardizing table operations in form classes.
 * Provides a consistent approach to setting up, loading, and refreshing table data.
 */
public interface TableFormInterface {
    
    /**
     * Sets up the table columns, including renderers, editors, and column properties.
     */
    void setupTableColumns();
    
    /**
     * Loads data into the table from the data source.
     */
    void loadTableData();
    
    /**
     * Refreshes the table data, typically called after changes to the underlying data.
     */
    void refreshTable();
    
    /**
     * Gets the main table component.
     * 
     * @param id
     */
    void setObjectToTable(Integer id);
}