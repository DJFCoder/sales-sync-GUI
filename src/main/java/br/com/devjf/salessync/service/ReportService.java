package br.com.devjf.salessync.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.devjf.salessync.dao.ExpenseDAO;
import br.com.devjf.salessync.dao.SaleDAO;
import br.com.devjf.salessync.model.Expense;
import br.com.devjf.salessync.model.Sale;
import br.com.devjf.salessync.util.CSVExporter;

public class ReportService {
    
    private final SaleDAO saleDAO;
    private final ExpenseDAO expenseDAO;
    
    public ReportService() {
        this.saleDAO = new SaleDAO();
        this.expenseDAO = new ExpenseDAO();
    }
    
    public Map<String, Object> generateBalanceSheet(Map<String, Object> parameters) {
        Map<String, Object> result = new HashMap<>();
        
        // Extract parameters
        LocalDate startDate = (LocalDate) parameters.getOrDefault("startDate", LocalDate.now().withDayOfMonth(1));
        LocalDate endDate = (LocalDate) parameters.getOrDefault("endDate", LocalDate.now());
        
        // Convert LocalDate to LocalDateTime for sales query
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(1);
        
        // Get sales data
        List<Sale> sales = saleDAO.findByDateRange(startDateTime, endDateTime);
        double totalRevenue = sales.stream().mapToDouble(Sale::getTotalAmount).sum();
        
        // Get expenses data
        List<Expense> expenses = expenseDAO.findByDateRange(startDate, endDate);
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        
        // Calculate profit/loss
        double netProfit = totalRevenue - totalExpenses;
        
        // Populate result
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("totalRevenue", totalRevenue);
        result.put("totalExpenses", totalExpenses);
        result.put("netProfit", netProfit);
        result.put("sales", sales);
        result.put("expenses", expenses);
        
        return result;
    }
    
    public Map<String, Double> calculateProfitability(String period) {
        Map<String, Double> result = new HashMap<>();
        
        LocalDate today = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate = today;
        
        // Determine date range based on period
        switch (period.toLowerCase()) {
            case "daily":
                startDate = today;
                break;
            case "weekly":
                startDate = today.minusWeeks(1);
                break;
            case "monthly":
                startDate = today.withDayOfMonth(1);
                break;
            case "quarterly":
                startDate = today.minusMonths(3).withDayOfMonth(1);
                break;
            case "yearly":
                startDate = today.withDayOfYear(1);
                break;
            default:
                startDate = today.minusMonths(1);
        }
        
        // Convert LocalDate to LocalDateTime for sales query
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(1);
        
        // Get sales data
        List<Sale> sales = saleDAO.findByDateRange(startDateTime, endDateTime);
        double totalRevenue = sales.stream().mapToDouble(Sale::getTotalAmount).sum();
        
        // Get expenses data
        List<Expense> expenses = expenseDAO.findByDateRange(startDate, endDate);
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        
        // Calculate metrics
        double netProfit = totalRevenue - totalExpenses;
        double profitMargin = totalRevenue > 0 ? (netProfit / totalRevenue) * 100 : 0;
        double returnOnInvestment = totalExpenses > 0 ? (netProfit / totalExpenses) * 100 : 0;
        
        // Populate result
        result.put("totalRevenue", totalRevenue);
        result.put("totalExpenses", totalExpenses);
        result.put("netProfit", netProfit);
        result.put("profitMargin", profitMargin);
        result.put("roi", returnOnInvestment);
        
        return result;
    }
    
    public Map<String, Object> analyzeSalesByPeriod(LocalDate start, LocalDate end) {
        Map<String, Object> result = new HashMap<>();
        
        // Convert LocalDate to LocalDateTime for sales query
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.plusDays(1).atStartOfDay().minusNanos(1);
        
        // Get sales data
        List<Sale> sales = saleDAO.findByDateRange(startDateTime, endDateTime);
        
        // Calculate metrics
        double totalRevenue = sales.stream().mapToDouble(Sale::getTotalAmount).sum();
        int totalSales = sales.size();
        double averageSaleValue = totalSales > 0 ? totalRevenue / totalSales : 0;
        
        // Group sales by day
        Map<LocalDate, Double> dailySales = new HashMap<>();
        for (Sale sale : sales) {
            LocalDate saleDate = sale.getDate().toLocalDate();
            dailySales.put(saleDate, dailySales.getOrDefault(saleDate, 0.0) + sale.getTotalAmount());
        }
        
        // Populate result
        result.put("startDate", start);
        result.put("endDate", end);
        result.put("totalRevenue", totalRevenue);
        result.put("totalSales", totalSales);
        result.put("averageSaleValue", averageSaleValue);
        result.put("dailySales", dailySales);
        
        return result;
    }
    
    public Map<String, Object> analyzeExpensesByCategory(Integer categoryId, String period) {
        Map<String, Object> result = new HashMap<>();
        
        LocalDate today = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate = today;
        
        // Determine date range based on period
        switch (period.toLowerCase()) {
            case "monthly":
                startDate = today.withDayOfMonth(1);
                break;
            case "quarterly":
                startDate = today.minusMonths(3).withDayOfMonth(1);
                break;
            case "yearly":
                startDate = today.withDayOfYear(1);
                break;
            default:
                startDate = today.minusMonths(1);
        }
        
        // Get expenses for the category
        List<Expense> allExpenses = expenseDAO.findByDateRange(startDate, endDate);
        List<Expense> categoryExpenses = new ArrayList<>();
        
        for (Expense expense : allExpenses) {
            if (expense.getCategory().getId().equals(categoryId)) {
                categoryExpenses.add(expense);
            }
        }
        
        // Calculate metrics
        double totalAmount = categoryExpenses.stream().mapToDouble(Expense::getAmount).sum();
        
        // Group expenses by day
        Map<LocalDate, Double> dailyExpenses = new HashMap<>();
        for (Expense expense : categoryExpenses) {
            LocalDate expenseDate = expense.getDate();
            dailyExpenses.put(expenseDate, dailyExpenses.getOrDefault(expenseDate, 0.0) + expense.getAmount());
        }
        
        // Populate result
        result.put("categoryId", categoryId);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("totalAmount", totalAmount);
        result.put("expenseCount", categoryExpenses.size());
        result.put("dailyExpenses", dailyExpenses);
        
        return result;
    }
    
    public boolean exportReport(Map<String, Object> data, String format, String path) {
        // Currently only supporting CSV export
        if (!"csv".equalsIgnoreCase(format)) {
            return false;
        }

        // Export data to CSV file
        try {
            CSVExporter exporter = new CSVExporter();
            return exporter.exportToCSV(data, path);
        } catch (Exception e) {
            System.err.println("Error exporting report: " + e.getMessage());
            return false;
        }
    }
    
    public Map<String, Object> generateTaxReports(String period) {
        Map<String, Object> result = new HashMap<>();
        
        LocalDate today = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate;
        
        // Determine date range based on period
        switch (period.toLowerCase()) {
            case "monthly":
                YearMonth currentMonth = YearMonth.from(today);
                startDate = currentMonth.atDay(1);
                endDate = currentMonth.atEndOfMonth();
                break;
            case "quarterly":
                int currentQuarter = (today.getMonthValue() - 1) / 3 + 1;
                int startMonth = (currentQuarter - 1) * 3 + 1;
                startDate = LocalDate.of(today.getYear(), startMonth, 1);
                endDate = startDate.plusMonths(3).minusDays(1);
                break;
            case "yearly":
                startDate = LocalDate.of(today.getYear(), 1, 1);
                endDate = LocalDate.of(today.getYear(), 12, 31);
                break;
            default:
                startDate = today.withDayOfMonth(1);
                endDate = today;
        }
        
        // Convert LocalDate to LocalDateTime for sales query
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(1);
        
        // Get sales data
        List<Sale> sales = saleDAO.findByDateRange(startDateTime, endDateTime);
        double totalRevenue = sales.stream().mapToDouble(Sale::getTotalAmount).sum();
        
        // Calculate tax estimates (simplified)
        double incomeTax = totalRevenue * 0.15; // 15% income tax
        double salesTax = totalRevenue * 0.10; // 10% sales tax
        double totalTax = incomeTax + salesTax;
        
        // Populate result
        result.put("period", period);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("totalRevenue", totalRevenue);
        result.put("incomeTax", incomeTax);
        result.put("salesTax", salesTax);
        result.put("totalTax", totalTax);
        
        return result;
    }
}