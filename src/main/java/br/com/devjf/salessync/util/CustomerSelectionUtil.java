package br.com.devjf.salessync.util;

import br.com.devjf.salessync.dao.CustomerDAO;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.com.devjf.salessync.model.Customer;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/**
 * Utility class for customer selection using JOptionPane
 */
public class CustomerSelectionUtil {

    /**
     * Shows a dialog to select a customer from the database
     * 
     * @param parent The parent component for the dialog
     * @return The selected customer or null if canceled
     */
    public static Customer selectCustomer(Component parent) {
        // Create a dialog for customer selection
        JDialog dialog = new JDialog((Dialog) SwingUtilities.getWindowAncestor(parent), "Selecionar Cliente", true);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(parent);
        
        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Buscar");
        searchPanel.add(new JLabel("Buscar por nome: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        // Create table with pagination
        String[] columnNames = {"ID", "Nome", "CPF/CNPJ", "Telefone", "Email"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Pagination controls
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prevButton = new JButton("Anterior");
        JButton nextButton = new JButton("Próximo");
        JLabel pageLabel = new JLabel("Página 1");
        paginationPanel.add(prevButton);
        paginationPanel.add(pageLabel);
        paginationPanel.add(nextButton);
        
        // Add components to dialog
        dialog.setLayout(new BorderLayout());
        dialog.add(searchPanel, BorderLayout.NORTH);
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);
        dialog.add(paginationPanel, BorderLayout.SOUTH);
        
        // Variables for pagination
        final int[] currentPage = {0};
        final int pageSize = 20;
        final String[] currentSearchTerm = {""};
        
        // Function to load data
        Runnable loadData = () -> {
            // Show loading indicator
            table.setEnabled(false);
            prevButton.setEnabled(false);
            nextButton.setEnabled(false);
            searchButton.setEnabled(false);
            
            SwingWorker<List<Customer>, Void> worker = new SwingWorker<List<Customer>, Void>() {
                @Override
                protected List<Customer> doInBackground() throws Exception {
                    CustomerDAO dao = new CustomerDAO();
                    if (currentSearchTerm[0].isEmpty()) {
                        return dao.findWithPagination(currentPage[0], pageSize);
                    } else {
                        return dao.findByNameWithPagination(currentSearchTerm[0], currentPage[0], pageSize);
                    }
                }
                
                @Override
                protected void done() {
                    try {
                        List<Customer> customers = get();
                        // Clear existing data
                        model.setRowCount(0);
                        
                        // Add new data
                        for (Customer customer : customers) {
                            model.addRow(new Object[]{
                                customer.getId(),
                                customer.getName(),
                                customer.getTaxId(),
                                customer.getPhone(),
                                customer.getEmail()
                            });
                        }
                        
                        // Update pagination controls
                        pageLabel.setText("Página " + (currentPage[0] + 1));
                        prevButton.setEnabled(currentPage[0] > 0);
                        nextButton.setEnabled(customers.size() == pageSize);
                        
                    } catch (InterruptedException | ExecutionException e) {
                        JOptionPane.showMessageDialog(dialog, 
                                "Erro ao carregar clientes: " + e.getMessage(),
                                "Erro", 
                                JOptionPane.ERROR_MESSAGE);
                    } finally {
                        // Re-enable controls
                        table.setEnabled(true);
                        prevButton.setEnabled(currentPage[0] > 0);
                        nextButton.setEnabled(model.getRowCount() == pageSize);
                        searchButton.setEnabled(true);
                    }
                }
            };
            
            worker.execute();
        };
        
        // Initial data load
        loadData.run();
        
        // Set up event handlers
        prevButton.addActionListener(e -> {
            if (currentPage[0] > 0) {
                currentPage[0]--;
                loadData.run();
            }
        });
        
        nextButton.addActionListener(e -> {
            currentPage[0]++;
            loadData.run();
        });
        
        searchButton.addActionListener(e -> {
            currentSearchTerm[0] = searchField.getText().trim();
            currentPage[0] = 0;
            loadData.run();
        });
        
        // Allow pressing Enter in search field
        searchField.addActionListener(e -> searchButton.doClick());
        
        // Result variable
        final Customer[] selectedCustomer = {null};
        
        // Add selection button
        JButton selectButton = new JButton("Selecionar");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        selectButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int customerId = (int) table.getValueAt(selectedRow, 0);
                CustomerDAO dao = new CustomerDAO();
                selectedCustomer[0] = dao.findById(customerId);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                        "Por favor, selecione um cliente da lista.",
                        "Nenhum cliente selecionado", 
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // Double-click to select
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    selectButton.doClick();
                }
            }
        });
        
        // Show dialog
        dialog.setVisible(true);
        
        // Return selected customer
        return selectedCustomer[0];
    }
    
    /**
     * Filter customers in the combo box based on search text
     * 
     * @param searchText Text to search for
     * @param comboBox ComboBox to update
     * @param allCustomers List of all customers
     */
    private static void filterCustomers(String searchText, JComboBox<Customer> comboBox, List<Customer> allCustomers) {
        if (searchText == null || searchText.trim().isEmpty()) {
            // If search text is empty, show all customers
            comboBox.removeAllItems();
            for (Customer customer : allCustomers) {
                comboBox.addItem(customer);
            }
            return;
        }
        
        // Convert search text to lowercase for case-insensitive search
        String lowerSearchText = searchText.toLowerCase();
        
        // Remove all items and add only matching ones
        comboBox.removeAllItems();
        for (Customer customer : allCustomers) {
            if (customer.getName().toLowerCase().contains(lowerSearchText) || 
                (customer.getTaxId() != null && customer.getTaxId().toLowerCase().contains(lowerSearchText))) {
                comboBox.addItem(customer);
            }
        }
        
        // Select first item if available
        if (comboBox.getItemCount() > 0) {
            comboBox.setSelectedIndex(0);
        }
    }
}