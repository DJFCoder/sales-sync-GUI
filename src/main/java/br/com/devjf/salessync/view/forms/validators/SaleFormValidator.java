package br.com.devjf.salessync.view.forms.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFormattedTextField;
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
        if (paymentMethod == null || paymentMethod.equals("Selecione")) {
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
}
