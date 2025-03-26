package br.com.devjf.salessync.controller;

import java.util.List;
import java.util.Map;
import br.com.devjf.salessync.model.Expense;
import br.com.devjf.salessync.model.ExpenseCategory;
import br.com.devjf.salessync.service.ExpenseService;

/**
 * Controller class for managing expense-related operations.
 * Provides methods for creating, updating, deleting, and retrieving expenses and expense categories.
 */
public class ExpenseController {
    private final ExpenseService expenseService;
    
    /**
     * Constructs a new ExpenseController with an ExpenseService instance.
     */
    public ExpenseController() {
        this.expenseService = new ExpenseService();
    }
    
    /**
     * Registers a new expense in the system.
     *
     * @param expense The expense object to register
     * @return true if the expense was successfully registered, false otherwise
     */
    public boolean registerExpense(Expense expense) {
        return expenseService.registerExpense(expense);
    }
    
    /**
     * Updates an existing expense's information.
     *
     * @param expense The expense object with updated information
     * @return true if the expense was successfully updated, false otherwise
     */
    public boolean updateExpense(Expense expense) {
        return expenseService.updateExpense(expense);
    }
    
    /**
     * Deletes an expense from the system by ID.
     *
     * @param id The ID of the expense to delete
     * @return true if the expense was successfully deleted, false otherwise
     */
    public boolean deleteExpense(Integer id) {
        return expenseService.deleteExpense(id);
    }
    
    /**
     * Finds an expense by its ID.
     *
     * @param id The ID of the expense to find
     * @return The expense object if found, null otherwise
     */
    public Expense findExpenseById(Integer id) {
        return expenseService.findExpenseById(id);
    }
    
    /**
     * Lists expenses based on specified filters.
     *
     * @param filters Map of filter criteria to apply
     * @return A list of expenses matching the filter criteria
     */
    public List<Expense> listExpenses(Map<String, Object> filters) {
        return expenseService.listExpenses(filters);
    }
    
    /**
     * Lists all expense categories in the system.
     *
     * @return A list of all expense categories
     */
    public List<ExpenseCategory> listAllCategories() {
        return expenseService.getAllCategories();
    }
    
    /**
     * Creates a new expense category.
     *
     * @param category The expense category to create
     * @return true if the category was successfully created, false otherwise
     */
    public boolean createExpenseCategory(ExpenseCategory category) {
        return expenseService.createExpenseCategory(category);
    }
    
    /**
     * Updates an existing expense category.
     *
     * @param category The expense category with updated information
     * @return true if the category was successfully updated, false otherwise
     */
    public boolean updateExpenseCategory(ExpenseCategory category) {
        return expenseService.updateExpenseCategory(category);
    }
    
    /**
     * Deletes an expense category by ID.
     *
     * @param id The ID of the expense category to delete
     * @return true if the category was successfully deleted, false otherwise
     */
    public boolean deleteExpenseCategory(Integer id) {
        return expenseService.deleteExpenseCategory(id);
    }
}