package br.com.devjf.salessync.util;

import br.com.devjf.salessync.model.RecurrenceType;

/**
 * Utility class for handling RecurrenceType conversions. Follows Single
 * Responsibility and Dependency Inversion principles.
 */
public class RecurrenceTypeConverter {
    /**
     * Converts RecurrenceType to display text.
     *
     * @param type The recurrence type to convert
     * @return Corresponding display text or empty string if null
     */
    public static String toDisplayText(RecurrenceType type) {
        return type == null ? "" : type.getDisplayName().toUpperCase();
    }

    /**
     * Converts display text to RecurrenceType.
     *
     * @param displayText The display text to convert
     * @return Corresponding RecurrenceType or null if not found
     */
    public static RecurrenceType fromDisplayText(String displayText) {
        if (displayText == null || "Selecione".equals(displayText)) {
            return null;
        }
        try {
            return RecurrenceType.fromDisplayName(displayText);
        } catch (IllegalArgumentException e) {
            // Log the error or handle it appropriately
            System.err.println("Invalid recurrence type: " + displayText);
            return null;
        }
    }
}
