package br.com.devjf.salessync.view.forms.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import br.com.devjf.salessync.dto.SaleItemDTO;
import br.com.devjf.salessync.model.Customer;

/**
 * Validator for sale form inputs. Validates customer, payment method, payment
 * date, and other required fields.
 */
public class SaleFormValidator {
    /**
     * Validates the customer selection.
     *
     * @param customer The selected customer
     * @throws IllegalStateException if no customer is selected
     */
    public static void validateCustomer(Customer customer) throws IllegalStateException {
        if (customer == null) {
            throw new IllegalStateException(
                    "É necessário selecionar um cliente para a venda.");
        }
    }

    /**
     * Validates the payment method selection.
     *
     * @param paymentMethod The selected payment method
     * @throws IllegalStateException if no payment method is selected
     */
    public static void validatePaymentMethod(String paymentMethod) throws IllegalStateException {
        if (paymentMethod == null || "Selecione".equals(paymentMethod)) {
            throw new IllegalStateException(
                    "É necessário selecionar uma forma de pagamento.");
        }
    }

    /**
     * Validates the payment date.
     *
     * @param paymentDateField The payment date field
     * @return The validated payment date string
     * @throws IllegalStateException if the date is invalid
     * @throws ParseException if the date format is invalid
     */
    public static String validatePaymentDate(JFormattedTextField paymentDateField)
            throws IllegalStateException, ParseException {
        String paymentDateStr = paymentDateField.getText();
        if (paymentDateStr == null || paymentDateStr.trim().isEmpty()
                || paymentDateStr.contains("_")) {
            throw new IllegalStateException(
                    "É necessário informar uma data de pagamento válida.");
        }
        // Validate date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        Date date = dateFormat.parse(paymentDateStr);
        return paymentDateStr;
    }
    
    /**
     * Validates the sale items from a table.
     *
     * @param table The table containing sale items
     * @return true if the items are valid, false otherwise
     * @throws IllegalStateException if no valid items are found
     */
    public static boolean validateSaleItems(JTable table) throws IllegalStateException {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        boolean hasValidItems = false;
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String description = (String) model.getValueAt(i, 0);
            Object quantityObj = model.getValueAt(i, 1);
            Object priceObj = model.getValueAt(i, 2);
            
            // Skip empty rows
            if (description == null || description.trim().isEmpty()) {
                continue;
            }
            
            // Check if quantity and price are valid
            try {
                double quantity = quantityObj != null ? Double.parseDouble(quantityObj.toString()) : 0;
                double price = priceObj != null ? Double.parseDouble(priceObj.toString()) : 0;
                
                if (quantity > 0 && price > 0) {
                    hasValidItems = true;
                }
            } catch (NumberFormatException e) {
                // Invalid number format, skip this row
                continue;
            }
        }
        
        if (!hasValidItems) {
            throw new IllegalStateException(
                    "É necessário adicionar pelo menos um item válido à venda.");
        }
        
        return true;
    }
    
    /**
     * Validates a list of sale items.
     *
     * @param items The list of sale items to validate
     * @throws IllegalStateException if the list is empty or contains invalid items
     */
    public static void validateSaleItemsList(List<SaleItemDTO> items) throws IllegalStateException {
        if (items == null || items.isEmpty()) {
            throw new IllegalStateException(
                    "É necessário adicionar pelo menos um item à venda.");
        }
        
        boolean hasValidItems = false;
        for (SaleItemDTO item : items) {
            if (item.getDescription() != null && !item.getDescription().trim().isEmpty() &&
                item.getQuantity() > 0 && 
                item.getPrice() > 0) {
                hasValidItems = true;
                break;
            }
        }
        
        if (!hasValidItems) {
            throw new IllegalStateException(
                    "É necessário informar o valor do item.");
        }
    }
    
    /**
     * Validates the discount amount.
     *
     * @param discountStr The discount amount as a string
     * @param subtotal The subtotal amount
     * @return The validated discount amount
     * @throws IllegalStateException if the discount is invalid
     */
    public static double validateDiscount(String discountStr, double subtotal) throws IllegalStateException {
        double discount = 0.0;
        
        if (discountStr != null && !discountStr.trim().isEmpty()) {
            try {
                // Replace comma with dot for proper parsing
                String normalizedDiscount = discountStr.replace(',', '.');
                discount = Double.parseDouble(normalizedDiscount);
                
                if (discount < 0) {
                    throw new IllegalStateException("O desconto não pode ser negativo.");
                }
                
                if (discount > subtotal) {
                    throw new IllegalStateException("O desconto não pode ser maior que o subtotal.");
                }
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Formato de desconto inválido.");
            }
        }
        
        return discount;
    }
    
    /**
     * Validates all sale form inputs at once.
     *
     * @param customer The selected customer
     * @param paymentMethod The selected payment method
     * @param paymentDateField The payment date field
     * @param table The table containing sale items
     * @param discountStr The discount amount as a string
     * @param subtotal The subtotal amount
     * @throws IllegalStateException if any validation fails
     * @throws ParseException if the date format is invalid
     */
    public static void validateSaleForm(
            Customer customer,
            String paymentMethod,
            JFormattedTextField paymentDateField,
            JTable table,
            String discountStr,
            double subtotal) throws IllegalStateException, ParseException {
        
        validateCustomer(customer);
        validatePaymentMethod(paymentMethod);
        validatePaymentDate(paymentDateField);
        validateSaleItems(table);
        validateDiscount(discountStr, subtotal);
    }
}
