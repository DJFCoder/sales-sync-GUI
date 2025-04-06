package br.com.devjf.salessync.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.devjf.salessync.dao.ExpenseCategoryDAO;
import br.com.devjf.salessync.dao.ExpenseDAO;
import br.com.devjf.salessync.model.Expense;
import br.com.devjf.salessync.model.ExpenseCategory;
import br.com.devjf.salessync.model.RecurrenceType;

public class ExpenseService {
    private final ExpenseDAO expenseDAO;
    private final ExpenseCategoryDAO categoryDAO;

    public ExpenseService() {
        this.expenseDAO = new ExpenseDAO();
        this.categoryDAO = new ExpenseCategoryDAO();
    }

    public boolean registerExpense(Expense expense) {
        return expenseDAO.save(expense);
    }

    public boolean updateExpense(Expense expense) {
        Expense existingExpense = expenseDAO.findById(expense.getId());
        if (existingExpense == null) {
            return false;
        }
        return expenseDAO.update(expense);
    }

    public boolean deleteExpense(Integer id) {
        return expenseDAO.delete(id);
    }

    public Expense findExpenseById(Integer id) {
        if (id == null) {
            System.err.println("ID da despesa não pode ser nulo");
            return null;
        }
        
        try {
            Expense expense = expenseDAO.findById(id);
            
            if (expense == null) {
                System.err.println("Despesa não encontrada para o ID: " + id);
                return null;
            }
            
            // Eager load related entities to prevent LazyInitializationException
            if (expense.getCategory() != null) {
                expense.getCategory().getName(); // Force load
            }
            
            return expense;
        } catch (Exception e) {
            System.err.println("Erro ao buscar despesa por ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Expense> listExpenses(Map<String, Object> filters) {
        // Extensive logging for debugging
        System.err.println("Filtros recebidos para listagem de despesas:");
        if (filters != null) {
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                System.err.println(entry.getKey() + ": " + entry.getValue());
            }
        }
        
        // If no filters, return all expenses
        if (filters == null || filters.isEmpty()) {
            System.err.println("Nenhum filtro aplicado. Retornando todas as despesas.");
            return expenseDAO.findAll();
        }
        
        try {
            // Handle specific filter types with comprehensive logic
            if (filters.containsKey("categoryName")) {
                String categoryName = (String) filters.get("categoryName");
                System.err.println("Filtrando por nome da categoria: " + categoryName);
                ExpenseCategory category = categoryDAO.findByName(categoryName);
                
                if (category != null) {
                    return expenseDAO.findByCategory(category);
                } else {
                    System.err.println("Categoria não encontrada: " + categoryName);
                    return expenseDAO.findAll();
                }
            }
            
            // Text-based filters with case-insensitive partial matching
            List<Expense> allExpenses = expenseDAO.findAll();
            List<Expense> filteredExpenses = new ArrayList<>(allExpenses);
            
            // Description filter
            if (filters.containsKey("description")) {
                String descriptionFilter = ((String) filters.get("description")).toLowerCase().trim();
                System.err.println("Filtrando por descrição: " + descriptionFilter);
                
                filteredExpenses.removeIf(expense -> 
                    expense.getDescription() == null || 
                    !expense.getDescription().toLowerCase().contains(descriptionFilter)
                );
            }
            
            // Value filter (partial match)
            if (filters.containsKey("value")) {
                String valueFilter = ((String) filters.get("value")).trim();
                System.err.println("Filtrando por valor: " + valueFilter);
                
                filteredExpenses.removeIf(expense -> 
                    expense.getAmount()== null || 
                    !expense.getAmount().toString().contains(valueFilter)
                );
            }
            
            // Date range filter
            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                LocalDate startDate = (LocalDate) filters.get("startDate");
                LocalDate endDate = (LocalDate) filters.get("endDate");
                System.err.println("Filtrando por intervalo de datas: " + startDate + " - " + endDate);
                
                filteredExpenses.removeIf(expense -> {
                    // Null check
                    if (expense.getDate() == null) {
                        return true;
                    }
                    
                    // Use inclusive date range check
                    boolean isOutsideRange = 
                        expense.getDate().toLocalDate().isBefore(startDate) || 
                        expense.getDate().toLocalDate().isAfter(endDate);
                    
                    // Log detailed filtering information
                    System.err.printf(
                        "Filtrando despesa (ID: %d, Data: %s): Fora do intervalo = %b%n", 
                        expense.getId(), 
                        expense.getDate(), 
                        isOutsideRange
                    );
                    
                    return isOutsideRange;
                });
            }
            
            // Log filtering results
            System.err.println("Total de despesas antes da filtragem: " + allExpenses.size());
            System.err.println("Total de despesas após filtragem: " + filteredExpenses.size());
            
            return filteredExpenses;
        } catch (Exception e) {
            // Comprehensive error handling
            System.err.println("Erro ao filtrar despesas: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback to returning all expenses in case of error
            return expenseDAO.findAll();
        }
    }

    public void categorizeExpense(Expense expense) {
        // This method would contain logic to automatically categorize expenses
        // based on description or other attributes
        // For simplicity, just using the provided category
        if (expense.getCategory() == null) {
            // Set a default category if none is provided
            ExpenseCategory defaultCategory = categoryDAO.findByName(
                    "Miscellaneous");
            if (defaultCategory == null) {
                defaultCategory = new ExpenseCategory();
                defaultCategory.setName("Miscellaneous");
                defaultCategory.setDescription(
                        "Default category for uncategorized expenses");
                categoryDAO.save(defaultCategory);
            }
            expense.setCategory(defaultCategory);
        }
    }

    /**
     * Returns all expense categories from the database
     *
     * @return List of all expense categories
     */
    public List<ExpenseCategory> getAllCategories() {
        return categoryDAO.findAll();
    }

    /**
     * Finds an expense category by its ID
     *
     * @param id The ID of the category to find
     * @return The found category or null if not found
     */
    public ExpenseCategory findCategoryById(Integer id) {
        return categoryDAO.findById(id);
    }

    /**
     * Creates a new expense category
     *
     * @param category The category to create
     * @return true if successful, false otherwise
     */
    public boolean createExpenseCategory(ExpenseCategory category) {
        return categoryDAO.save(category);
    }

    /**
     * Updates an existing expense category
     *
     * @param category The category with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateExpenseCategory(ExpenseCategory category) {
        ExpenseCategory existingCategory = categoryDAO.findById(category.getId());
        if (existingCategory == null) {
            return false;
        }
        return categoryDAO.update(category);
    }

    /**
     * Deletes an expense category by its ID
     *
     * @param id The ID of the category to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteExpenseCategory(Integer id) {
        return categoryDAO.delete(id);
    }

    /**
     * Calculates the total expenses amount for a specific period.
     *
     * @param startDateTime The start date and time of the period
     * @param endDateTime The end date and time of the period
     * @return Total expenses amount for the specified period
     */
    public BigDecimal calculateTotalExpensesInPeriod(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // Find expenses within the specified date range
        Map<String, Object> filters = new HashMap<>();
        filters.put("startDate", startDateTime.toLocalDate());
        filters.put("endDate", endDateTime.toLocalDate());
        
        // Get filtered expenses
        List<Expense> expensesInPeriod = listExpenses(filters);
        
        // Calculate total expenses amount
        return expensesInPeriod.stream()
            .map(Expense::getAmount)
            .map(BigDecimal::valueOf)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculates the total expenses amount up to the current moment.
     *
     * @return Total expenses amount for all registered expenses
     */
    public BigDecimal calculateTotalExpenses() {
        // Get all expenses
        List<Expense> allExpenses = expenseDAO.findAll();
        
        // Calculate total expenses amount
        return allExpenses.stream()
            .map(Expense::getAmount)
            .map(BigDecimal::valueOf)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
