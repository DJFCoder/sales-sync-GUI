package br.com.devjf.salessync.view.forms.validators;

import javax.swing.JTextField;
import br.com.devjf.salessync.controller.CustomerController;
import br.com.devjf.salessync.model.Customer;

/**
 * Validator for customer form inputs. Validates name, tax ID, email, phone, and
 * other required fields.
 */
public class CustomerFormValidator {
    /**
     * Validates the customer name.
     *
     * @param nameField The name field
     * @throws IllegalStateException if the name is empty or invalid
     */
    public static void validateName(JTextField nameField) throws IllegalStateException {
        String name = nameField.getText();
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalStateException(
                    "É necessário informar o nome do cliente.");
        }
        if (name.trim().length() < 3) {
            throw new IllegalStateException(
                    "O nome do cliente deve ter pelo menos 3 caracteres.");
        }
    }

    /**
     * Validates the tax ID (CPF/CNPJ).
     *
     * @param taxIdField The tax ID field
     * @throws IllegalStateException if the tax ID is empty or invalid
     */
    public static void validateTaxId(JTextField taxIdField) throws IllegalStateException {
        String taxId = taxIdField.getText();
        if (taxId == null || taxId.trim().isEmpty()) {
            throw new IllegalStateException(
                    "É necessário informar o CPF/CNPJ do cliente.");
        }
        // Remove non-numeric characters for validation
        String digits = taxId.replaceAll("[^0-9]",
                "");
        // Check if it's a valid CPF (11 digits) or CNPJ (14 digits)
        if (digits.length() != 11 && digits.length() != 14) {
            throw new IllegalStateException(
                    "O CPF/CNPJ informado é inválido. CPF deve ter 11 dígitos e CNPJ 14 dígitos.");
        }
    }

    /**
     * Validates that the tax ID is not already in use by another customer.
     *
     * @param taxIdField The tax ID field to check
     * @throws IllegalStateException if the tax ID is already in use
     */
    public static void validateTaxIdUniqueness(JTextField taxIdField) throws IllegalStateException {
        // Format the tax ID before checking
        CustomerController customerController = new CustomerController();
        String formattedTaxId = customerController.formatTaxId(
                taxIdField.getText());
        // Check if tax ID already exists in the database
        Customer existingCustomer = customerController.findCustomerByTaxId(
                formattedTaxId);
        // If a customer with this tax ID exists
        if (existingCustomer != null) {
            throw new IllegalStateException(
                    "CPF/CNPJ já cadastrado no sistema. Não é possível cadastrar clientes duplicados.");
        }
    }

    /**
     * Validates the email address.
     *
     * @param emailField The email field
     * @throws IllegalStateException if the email is invalid
     */
    public static void validateEmail(JTextField emailField) throws IllegalStateException {
        String email = emailField.getText();
        // Email is optional, but if provided, must be valid
        if (email != null && !email.trim().isEmpty()) {
            // Simple email validation
            String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (!email.matches(emailRegex)) {
                throw new IllegalStateException(
                        "O formato do e-mail informado é inválido.");
            }
        }
    }

    /**
     * Validates the phone number.
     *
     * @param phoneField The phone field
     * @throws IllegalStateException if the phone is invalid
     */
    public static void validatePhone(JTextField phoneField) throws IllegalStateException {
        String phone = phoneField.getText();
        // Phone is optional, but if provided, must be valid
        if (phone != null && !phone.trim().isEmpty()) {
            // Remove non-numeric characters for validation
            String phoneDigits = phone.replaceAll("[^0-9]",
                    "");
            // Check if phone has a reasonable number of digits
            if (phoneDigits.length() < 8 || phoneDigits.length() > 15) {
                throw new IllegalStateException(
                        "O formato do telefone informado é inválido.");
            }
        }
    }

    /**
     * Validates all customer form fields at once.
     *
     * @param nameField The name field
     * @param taxIdField The tax ID field
     * @param emailField The email field
     * @param phoneField The phone field
     * @throws IllegalStateException if any validation fails
     */
    public static void validateAllFields(
            JTextField nameField,
            JTextField taxIdField,
            JTextField emailField,
            JTextField phoneField) throws IllegalStateException {
        validateName(nameField);
        validateTaxId(taxIdField);
        validateEmail(emailField);
        validatePhone(phoneField);
        validateTaxIdUniqueness(taxIdField);
    }
}
