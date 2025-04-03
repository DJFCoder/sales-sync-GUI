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
            throw new IllegalStateException("Por favor, selecione um cliente.");
        }
    }
    
    /**
     * Validates the status selection.
     * 
     * @param statusCombo The status combo box
     * @throws IllegalStateException if no status is selected
     */
    public static void validateStatus(JComboBox<String> statusCombo) throws IllegalStateException {
        if (statusCombo.getSelectedIndex() == 0) {
            throw new IllegalStateException("Por favor, selecione um status.");
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
        if (description != null && description.length() > 1000) {
            throw new IllegalStateException("A descrição não pode exceder 1000 caracteres.");
        }
    }
    
    /**
     * Validates all service order form fields at once.
     * 
     * @param customer The selected customer
     * @param statusCombo The status combo box
     * @param descriptionField The description text area
     * @throws IllegalStateException if any validation fails
     */
    public static void validateAllFields(
            Customer customer,
            JComboBox<String> statusCombo,
            JTextArea descriptionField) throws IllegalStateException {
        validateCustomer(customer);
        validateStatus(statusCombo);
        validateDescription(descriptionField);
    }
}