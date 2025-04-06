package br.com.devjf.salessync.model;

/**
 * Represents the recurrence types for recurring events or expenses.
 */
public enum RecurrenceType {
    DAILY("Diária"),
    WEEKLY("Semanal"),
    MONTHLY("Mensal"),
    ANNUAL("Anual");

    /**
     * The display name of the recurrence type in Portuguese.
     */
    private final String displayName;

    /**
     * Constructor for RecurrenceType.
     * 
     * @param displayName The human-readable name of the recurrence type
     */
    RecurrenceType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Retrieves the display name of the recurrence type.
     * 
     * @return The display name in Portuguese
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Converts a display name to its corresponding RecurrenceType.
     * 
     * This method follows the Open/Closed Principle by providing
     * an extensible way to convert display names to enum values.
     * 
     * @param displayName The display name to convert
     * @return The corresponding RecurrenceType
     * @throws IllegalArgumentException If no matching recurrence type is found
     */
    public static RecurrenceType fromDisplayName(String displayName) {
        // Validate input to prevent null or empty strings
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("Display name cannot be null or empty");
        }

        for (RecurrenceType type : values()) {
            if (type.displayName.equalsIgnoreCase(displayName.trim())) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("No matching recurrence type for display name: " + displayName);
    }

    /**
     * Provides a default recurrence type if no specific type is selected.
     * 
     * @return The default recurrence type (MONTHLY)
     */
    public static RecurrenceType getDefaultType() {
        return MONTHLY;
    }
}