package br.com.devjf.salessync.controller;

import java.util.List;
import java.util.Map;

import br.com.devjf.salessync.model.Expense;
import br.com.devjf.salessync.model.ExpenseCategory;
import br.com.devjf.salessync.service.ExpenseService;

public class ExpenseController {
    private final ExpenseService expenseService;
    
    public ExpenseController() {
        this.expenseService = new ExpenseService();
    }
    
    public boolean registerExpense(Expense expense) {
        return expenseService.registerExpense(expense);
    }
    
    public boolean updateExpense(Expense expense) {
        return expenseService.updateExpense(expense);
    }
    
    public boolean deleteExpense(Integer id) {
        return expenseService.deleteExpense(id);
    }
    
    public Expense findExpenseById(Integer id) {
        return expenseService.findExpenseById(id);
    }
    
    public List<Expense> listExpenses(Map<String, Object> filters) {
        return expenseService.listExpenses(filters);
    }
    
    public List<ExpenseCategory> listAllCategories() {
        return expenseService.getAllCategories();
    }
    
    public boolean createExpenseCategory(ExpenseCategory category) {
        return expenseService.createExpenseCategory(category);
    }
    
    public boolean updateExpenseCategory(ExpenseCategory category) {
        return expenseService.updateExpenseCategory(category);
    }
    
    public boolean deleteExpenseCategory(Integer id) {
        return expenseService.deleteExpenseCategory(id);
    }
}