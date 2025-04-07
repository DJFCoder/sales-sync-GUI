package br.com.devjf.salessync.view.forms.validators;

import br.com.devjf.salessync.model.UserType;

/**
 * Validator class for user form inputs to ensure data integrity and validation.
 */
public class UserFormValidator {
    /**
     * Validates the user name.
     *
     * @param name The name to validate
     * @throws IllegalStateException if the name is invalid
     */
    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalStateException(
                    "O nome do usuário não pode estar em branco.");
        }
        // Optional: Add more name validation rules if needed
        if (name.trim().length() < 2) {
            throw new IllegalStateException(
                    "O nome deve ter pelo menos 2 caracteres.");
        }
    }

    /**
     * Validates the user login.
     *
     * @param login The login to validate
     * @throws IllegalStateException if the login is invalid
     */
    public static void validateLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            throw new IllegalStateException("O login não pode estar em branco.");
        }
        // Optional: Add more login validation rules
        if (login.trim().length() < 3) {
            throw new IllegalStateException(
                    "O login deve ter pelo menos 3 caracteres.");
        }
        // Optional: Check for valid characters
        if (!login.matches("^[a-zA-Z0-9_-]+$")) {
            throw new IllegalStateException(
                    "O login deve conter apenas letras, números, underlines e hífens.");
        }
    }

    /**
     * Validates the user type selection.
     *
     * @param userType The user type to validate
     * @throws IllegalStateException if the user type is invalid
     */
    public static void validateUserType(UserType userType) {
        if (userType == null) {
            throw new IllegalStateException("Selecione um tipo de usuário.");
        }
    }

    /**
     * Validates the password.
     *
     * @param password The password to validate
     * @param isEditMode Whether the form is in edit mode
     * @throws IllegalStateException if the password is invalid
     */
    public static void validatePassword(String password, boolean isEditMode) {
        // For new users, password is mandatory
        if (!isEditMode && (password == null || password.trim().isEmpty())) {
            throw new IllegalStateException("A senha não pode estar em branco.");
        }
        // If password is provided, check its complexity
        if (password != null && !password.isEmpty()) {
            // Password complexity requirements
            if (password.length() < 8) {
                throw new IllegalStateException(
                        "A senha deve ter pelo menos 8 caracteres.");
            }
            if (!password.matches(".*[A-Z].*")) {
                throw new IllegalStateException(
                        "A senha deve conter pelo menos uma letra maiúscula.");
            }
            if (!password.matches(".*[a-z].*")) {
                throw new IllegalStateException(
                        "A senha deve conter pelo menos uma letra minúscula.");
            }
            if (!password.matches(".*\\d.*")) {
                throw new IllegalStateException(
                        "A senha deve conter pelo menos um número.");
            }
        }
    }

    /**
     * Validates password confirmation.
     *
     * @param password The original password
     * @param confirmPassword The password confirmation
     * @throws IllegalStateException if passwords do not match
     */
    public static void validatePasswordConfirmation(String password, String confirmPassword) {
        if (password != null && !password.equals(confirmPassword)) {
            throw new IllegalStateException("As senhas não coincidem.");
        }
    }

    /**
     * Validates all user form fields.
     *
     * @param name User's name
     * @param login User's login
     * @param userType User's type
     * @param password User's password
     * @param confirmPassword Password confirmation
     * @param isEditMode Whether the form is in edit mode
     * @throws IllegalStateException if any validation fails
     */
    public static void validateUserForm(
            String name,
            String login,
            UserType userType,
            String password,
            String confirmPassword,
            boolean isEditMode) {
        validateName(name);
        // Login validation only for new users
        if (!isEditMode) {
            validateLogin(login);
        }
        validateUserType(userType);
        validatePassword(password,
                isEditMode);
        validatePasswordConfirmation(password,
                confirmPassword);
    }
}
