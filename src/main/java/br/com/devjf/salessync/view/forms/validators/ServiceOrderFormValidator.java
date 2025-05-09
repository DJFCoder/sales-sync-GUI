package br.com.devjf.salessync.view.forms.validators;

import javax.swing.JComboBox;
import javax.swing.JTextArea;
import br.com.devjf.salessync.model.Customer;

/**
 * Validator for service order form inputs. Validates customer, status, and
 * other required fields.
 */
public class ServiceOrderFormValidator {
    
    /**
     * Validates the customer selection.
     * 
     * @param customer The selected customer
     * @throws IllegalStateException if no customer is selected
     */
    public static void validateCustomer(Customer customer) throws IllegalStateException {
        if (customer == null) {
            throw new IllegalStateException("É necessário selecionar um cliente.");
        }
    }
    
    /**
     * Validates the status selection.
     * 
     * @param saleCombo The status combo box
     * @throws IllegalStateException if no status is selected
     */
    public static void validateSale(JComboBox<String> saleCombo) throws IllegalStateException {
        // Check if there's a selected item
        if (saleCombo.getSelectedItem() == null) {
            throw new IllegalStateException("É necessário selecionar uma venda. 'null'");
        }
        
        // Check if the selected index is valid and not the default "Selecione" option
        if (saleCombo.getSelectedIndex() <= 0 || "Selecione".equals(saleCombo.getSelectedItem().toString())) {
            throw new IllegalStateException("É necessário selecionar uma venda. 'selecione'");
        }
    }
    
    /**
     * Validates the description field.
     * 
     * @param descriptionField The description text area
     * @throws IllegalStateException if the description is too long
     */
    public static void validateDescription(JTextArea descriptionField) throws IllegalStateException {
        String description = descriptionField.getText();
        // Description is optional, but if provided, check length
        if (description != null && description.length() > 600) {
            throw new IllegalStateException("A descrição não pode exceder 600 caracteres.");
        }
    }
    
    /**
     * Validates all service order form fields at once.
     * 
     * @param customer The selected customer
     * @param saleCombo The status combo box
     * @param descriptionField The description text area
     * @throws IllegalStateException if any validation fails
     */
    public static void validateAllFields(
            Customer customer,
            JComboBox<String> saleCombo,
            JTextArea descriptionField) throws IllegalStateException {
        validateCustomer(customer);
        validateSale(saleCombo);
        validateDescription(descriptionField);
    }
}