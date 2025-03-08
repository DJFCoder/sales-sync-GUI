package br.com.devjf.salessync.controller;

import java.time.LocalDate;
import java.util.Map;

import br.com.devjf.salessync.service.ReportService;

public class ReportController {
    private final ReportService reportService;
    
    public ReportController() {
        this.reportService = new ReportService();
    }
    
    public Map<String, Object> generateBalanceSheet(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> parameters = Map.of(
            "startDate", startDate,
            "endDate", endDate
        );
        return reportService.generateBalanceSheet(parameters);
    }
    
    public Map<String, Object> analyzeSalesByPeriod(LocalDate period1, LocalDate period2) {
        return reportService.analyzeSalesByPeriod(period1, period2);
    }
    
    public Map<String, Double> calculateProfitability(String period) {
        return reportService.calculateProfitability(period);
    }
    
    public Map<String, Object> analyzeExpensesByCategory(Integer categoryId, String period) {
        return reportService.analyzeExpensesByCategory(categoryId, period);
    }
    
    public boolean exportReport(Map<String, Object> data, String format, String filePath) {
        return reportService.exportReport(data, format, filePath);
    }
}