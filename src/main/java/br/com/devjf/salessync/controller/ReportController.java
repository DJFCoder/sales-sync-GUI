package br.com.devjf.salessync.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import br.com.devjf.salessync.service.ReportService;

/**
 * Controller class for generating and managing reports.
 * Provides methods for generating various types of reports and exporting them.
 */
public class ReportController {
    private final ReportService reportService;
    
    /**
     * Constructs a new ReportController with a ReportService instance.
     */
    public ReportController() {
        this.reportService = new ReportService();
    }
    
    /**
     * Generates a balance sheet report for a specified date range.
     *
     * @param startDate The start date for the report
     * @param endDate The end date for the report
     * @return A map containing the balance sheet data
     */
    public Map<String, Object> generateBalanceSheet(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> parameters = Map.of(
            "startDate", startDate,
            "endDate", endDate
        );
        return reportService.generateBalanceSheet(parameters);
    }
    
    /**
     * Analyzes sales data comparing two time periods.
     *
     * @param period1 The first period for comparison
     * @param period2 The second period for comparison
     * @return A map containing the sales analysis data
     */
    public Map<String, Object> analyzeSalesByPeriod(LocalDate period1, LocalDate period2) {
        return reportService.analyzeSalesByPeriod(period1, period2);
    }
    
    /**
     * Calculates profitability metrics for a specified period.
     *
     * @param period The period to calculate profitability for
     * @return A map containing profitability metrics
     */
    public Map<String, Double> calculateProfitability(String period) {
        return reportService.calculateProfitability(period);
    }
    
    /**
     * Analyzes expenses by category for a specified period.
     *
     * @param categoryId The ID of the expense category to analyze
     * @param period The period to analyze expenses for
     * @return A map containing the expense analysis data
     */
    public Map<String, Object> analyzeExpensesByCategory(Integer categoryId, String period) {
        return reportService.analyzeExpensesByCategory(categoryId, period);
    }
    
    /**
     * Exports a report to a file in the specified format.
     *
     * @param data The report data to export
     * @param filePath The path to save the exported report to
     * @return true if the report was successfully exported, false otherwise
     */
    public boolean exportReport(Map<String, Object> data, String filePath) {
        return reportService.exportReport(data, filePath);
    }
    
    /**
     * Generates a sales report for a specific customer.
     *
     * @param customerId The ID of the customer to generate the report for
     * @param startDate The start date for the report
     * @param endDate The end date for the report
     * @return A map containing the customer sales report data
     */
    public Map<String, Object> generateCustomerSalesReport(Integer customerId, LocalDate startDate, LocalDate endDate) {
        Map<String, Object> parameters = Map.of(
            "customerId", customerId,
            "startDate", startDate,
            "endDate", endDate
        );
        return reportService.generateCustomerSalesReport(parameters);
    }
    
    /**
     * Generates a product performance report.
     *
     * @param startDate The start date for the report
     * @param endDate The end date for the report
     * @return A map containing the product performance data
     */
    public Map<String, Object> generateProductPerformanceReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> parameters = Map.of(
            "startDate", startDate,
            "endDate", endDate
        );
        return reportService.generateProductPerformanceReport(parameters);
    }
    
    /**
     * Generates a monthly sales summary report.
     *
     * @param year The year to generate the report for
     * @param month The month to generate the report for (1-12)
     * @return A map containing the monthly sales summary data
     */
    public Map<String, Object> generateMonthlySalesSummary(int year, int month) {
        Map<String, Object> parameters = Map.of(
            "year", year,
            "month", month
        );
        return reportService.generateMonthlySalesSummary(parameters);
    }
    
    /**
     * Generates a service order status report.
     *
     * @param startDate The start date for the report
     * @param endDate The end date for the report
     * @return A map containing the service order status data
     */
    public Map<String, Object> generateServiceOrderStatusReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> parameters = Map.of(
            "startDate", startDate,
            "endDate", endDate
        );
        return reportService.generateServiceOrderStatusReport(parameters);
    }
    
    /**
     * Generates a cash flow report.
     *
     * @param startDate The start date for the report
     * @param endDate The end date for the report
     * @return A map containing the cash flow data
     */
    public Map<String, Object> generateCashFlowReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> parameters = Map.of(
            "startDate", startDate,
            "endDate", endDate
        );
        return reportService.generateCashFlowReport(parameters);
    }
    
//    /**
//     * Generates a tax summary report.
//     *
//     * @param startDate The start date for the report
//     * @param endDate The end date for the report
//     * @return A map containing the tax summary data
//     */
//    public Map<String, Object> generateTaxSummaryReport(LocalDate startDate, LocalDate endDate) {
//        Map<String, Object> parameters = Map.of(
//            "startDate", startDate,
//            "endDate", endDate
//        );
//        return reportService.generateTaxSummaryReport(parameters);
//    }
    
}