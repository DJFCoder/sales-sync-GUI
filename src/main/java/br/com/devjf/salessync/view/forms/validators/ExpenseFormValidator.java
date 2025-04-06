package br.com.devjf.salessync.view.forms.validators;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Validator for Expense Form fields
 */
public class ExpenseFormValidator {
    /**
     * Validates the description field
     *
     * @param descriptionField Description text field
     * @throws IllegalStateException if description is invalid
     */
    public static void validateDescription(JTextField descriptionField) throws IllegalStateException {
        String description = descriptionField.getText();
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalStateException("Descrição é obrigatória.");
        }
        if (description.length() > 200) {
            throw new IllegalStateException(
                    "A descrição não pode exceder 200 caracteres.");
        }
    }

    /**
     * Validates the value field
     *
     * @param valueField Value formatted text field
     * @throws IllegalStateException if value is invalid
     */
    public static void validateValue(JTextField valueField) throws IllegalStateException {
        String valueText = valueField.getText();

        try {
            // Handle null or empty input
            if (valueText == null || valueText.trim().isEmpty()) {
                throw new IllegalStateException("Valor não pode ser vazio");
            }

            // Handle currency parsing with Brazilian Real format
            String cleanedAmount = valueText
                    .trim()
                    .replace("R$ ", "")
                    .replace("R$", "")
                    .replaceAll("[\\u200B\\u200C\\u200D\\u202F]", "")
                    .replaceAll("\\p{C}+", "")
                    .replaceAll("\\s+", "")
                    .replace(".", "")
                    .replace(",", ".")
                    .trim();

            // Prevent empty input after cleaning
            if (cleanedAmount.isEmpty()) {
                throw new IllegalStateException("Valor não pode ser vazio");
            }

            // Ensure proper decimal format
            if (!cleanedAmount.contains(".")) {
                cleanedAmount += ".00";
            } else {
                String[] parts = cleanedAmount.split("\\.");
                if (parts.length > 1) {
                    if (parts[1].length() > 2) {
                        parts[1] = parts[1].substring(0, 2);
                    } else if (parts[1].length() == 1) {
                        parts[1] += "0";
                    }
                    cleanedAmount = parts[0] + "." + parts[1];
                }
            }

            // Parse and validate amount
            Double value = Double.parseDouble(cleanedAmount);

            if (value <= 0) {
                throw new IllegalStateException("Valor deve ser maior que zero");
            }
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Formato de valor inválido: " + valueText);
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao processar valor: " + valueText);
        }
    }

    /**
     * Validates the date field
     *
     * @param dateField Date formatted text field
     * @throws IllegalStateException if date is invalid
     */
    public static void validateDate(JFormattedTextField dateField) throws IllegalStateException {
        try {
            String dateText = dateField.getText();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat.setLenient(false);
            dateFormat.parse(dateText);
        } catch (ParseException e) {
            throw new IllegalStateException(
                    "Data inválida. Use o formato dd/MM/yyyy.");
        }
    }

    /**
     * Validates the category combo box
     *
     * @param categoryCmb Category combo box
     * @throws IllegalStateException if no category is selected
     */
    public static void validateCategory(JComboBox<String> categoryCmb) throws IllegalStateException {
        if (categoryCmb.getSelectedIndex() <= 0) {
            throw new IllegalStateException("Selecione uma categoria.");
        }
    }

    /**
     * Validates the recurrence combo box
     *
     * @param recurrenceCmb Recurrence combo box
     * @throws IllegalStateException if no recurrence is selected
     */
    public static void validateRecurrence(JComboBox<String> recurrenceCmb) throws IllegalStateException {
        if (recurrenceCmb.getSelectedIndex() <= 0) {
            throw new IllegalStateException("Selecione uma recorrência.");
        }
    }

    /**
     * Validates all expense form fields at once
     *
     * @param descriptionField Description text field
     * @param valueField       Value formatted text field
     * @param dateField        Date formatted text field
     * @param categoryCmb      Category combo box
     * @param recurrenceCmb    Recurrence combo box
     * @throws IllegalStateException if any validation fails
     */
    public static void validateAllFields(
            JTextField descriptionField,
            JTextField valueField,
            JFormattedTextField dateField,
            JComboBox<String> categoryCmb,
            JComboBox<String> recurrenceCmb) throws IllegalStateException {
        validateDescription(descriptionField);
        validateValue(valueField);
        validateDate(dateField);
        validateCategory(categoryCmb);
        validateRecurrence(recurrenceCmb);
    }
}
