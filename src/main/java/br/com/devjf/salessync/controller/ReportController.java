package br.com.devjf.salessync.controller;

import br.com.devjf.salessync.service.ReportService;
import br.com.devjf.salessync.service.SaleService;
import br.com.devjf.salessync.service.ExpenseService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controller class for generating and managing reports.
 * Provides methods for generating various types of reports and exporting them.
 */
public class ReportController {
    private final ReportService reportService;
    private final SaleService saleService;
    private final ExpenseService expenseService;

    /**
     * Enum para representar os tipos de negócio para cálculo de impostos.
     */
    public enum BusinessType {
        PRODUCT_SALES,
        SERVICE_SALES
    }

    /**
     * Constructs a new ReportController with a ReportService instance.
     */
    public ReportController() {
        this.reportService = new ReportService();
        this.saleService = new SaleService();
        this.expenseService = new ExpenseService();
    }

    /**
     * Converte o BusinessType do controller para o BusinessType do serviço.
     *
     * @param controllerBusinessType Tipo de negócio do controller
     * @return Tipo de negócio correspondente no serviço
     */
    private ReportService.BusinessType convertBusinessType(BusinessType controllerBusinessType) {
        switch (controllerBusinessType) {
            case PRODUCT_SALES:
                return ReportService.BusinessType.PRODUCT_SALES;
            case SERVICE_SALES:
                return ReportService.BusinessType.SERVICE_BASED;
            default:
                return ReportService.BusinessType.PRODUCT_SALES;
        }
    }

    /**
     * Generates a balance sheet report for a specified date range.
     *
     * @param startDate The start date for the report
     * @param endDate   The end date for the report
     * @return A map containing the balance sheet data
     */
    public Map<String, Object> generateBalanceSheet(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> parameters = Map.of(
                "startDate", startDate,
                "endDate", endDate);
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
     * @param period     The period to analyze expenses for
     * @return A map containing the expense analysis data
     */
    public Map<String, Object> analyzeExpensesByCategory(Integer categoryId, String period) {
        return reportService.analyzeExpensesByCategory(categoryId, period);
    }

    /**
     * Exports a report to a file in the specified format.
     *
     * @param data     The report data to export
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
     * @param startDate  The start date for the report
     * @param endDate    The end date for the report
     * @return A map containing the customer sales report data
     */
    public Map<String, Object> generateCustomerSalesReport(Integer customerId, LocalDate startDate, LocalDate endDate) {
        Map<String, Object> parameters = Map.of(
                "customerId", customerId,
                "startDate", startDate,
                "endDate", endDate);
        return reportService.generateCustomerSalesReport(parameters);
    }

    /**
     * Generates a product performance report.
     *
     * @param startDate The start date for the report
     * @param endDate   The end date for the report
     * @return A map containing the product performance data
     */
    public Map<String, Object> generateProductPerformanceReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> parameters = Map.of(
                "startDate", startDate,
                "endDate", endDate);
        return reportService.generateProductPerformanceReport(parameters);
    }

    /**
     * Generates a monthly sales summary report.
     *
     * @param year  The year to generate the report for
     * @param month The month to generate the report for (1-12)
     * @return A map containing the monthly sales summary data
     */
    public Map<String, Object> generateMonthlySalesSummary(int year, int month) {
        Map<String, Object> parameters = Map.of(
                "year", year,
                "month", month);
        return reportService.generateMonthlySalesSummary(parameters);
    }

    /**
     * Generates a service order status report.
     *
     * @param startDate The start date for the report
     * @param endDate   The end date for the report
     * @return A map containing the service order status data
     */
    public Map<String, Object> generateServiceOrderStatusReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> parameters = Map.of(
                "startDate", startDate,
                "endDate", endDate);
        return reportService.generateServiceOrderStatusReport(parameters);
    }

    /**
     * Generates a cash flow report.
     *
     * @param startDate The start date for the report
     * @param endDate   The end date for the report
     * @return A map containing the cash flow data
     */
    public Map<String, Object> generateCashFlowReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> parameters = Map.of(
                "startDate", startDate,
                "endDate", endDate);
        return reportService.generateCashFlowReport(parameters);
    }

    /**
     * Calcula o resumo financeiro para um período específico.
     *
     * @param startDate    Data de início do período
     * @param endDate      Data de fim do período
     * @param businessType Tipo de negócio para cálculo de impostos
     * @return Objeto com resumo financeiro detalhado
     */
    public FinancialSummary generateFinancialSummary(
            LocalDate startDate,
            LocalDate endDate,
            BusinessType businessType) {

        // Converter datas para LocalDateTime para consultas no banco
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        // Calcular total de vendas
        BigDecimal totalSales = saleService.calculateTotalSalesInPeriod(startDateTime, endDateTime);

        // Calcular total de despesas
        BigDecimal totalExpenses = expenseService.calculateTotalExpensesInPeriod(startDateTime, endDateTime);

        // Calcular lucro bruto
        BigDecimal grossProfit = totalSales.subtract(totalExpenses);

        // Calcular impostos
        ReportService.BusinessType serviceBusinessType = convertBusinessType(businessType);
        BigDecimal taxes = reportService.calculateTaxes(totalSales, serviceBusinessType);

        // Calcular lucro líquido
        BigDecimal netProfit = reportService.calculateNetProfit(grossProfit, taxes);

        // Criar e retornar resumo financeiro
        return new FinancialSummary(
                totalSales,
                totalExpenses,
                grossProfit,
                taxes,
                netProfit);
    }

    /**
     * Obtém o total de vendas atual.
     *
     * @return Total de vendas
     */
    public BigDecimal getTotalSales() {
        return saleService.calculateTotalSales();
    }

    /**
     * Obtém o total de despesas atual.
     *
     * @return Total de despesas
     */
    public BigDecimal getTotalExpenses() {
        return expenseService.calculateTotalExpenses();
    }

    /**
     * Calcula o Custo das Mercadorias Vendidas (CMV).
     *
     * @return Custo total das mercadorias vendidas
     */
    public BigDecimal getCostOfGoodsSold() {
        // Implementação básica: usa despesas diretas relacionadas à produção
        // Pode ser refinado para incluir custos específicos de produção
        return expenseService.calculateTotalExpenses();
    }

    /**
     * Calcula o lucro bruto atual.
     *
     * @return Lucro bruto total
     */
    public BigDecimal getCurrentGrossProfit() {
        BigDecimal totalSales = getTotalSales();
        BigDecimal costOfGoodsSold = getCostOfGoodsSold();
        return totalSales.subtract(costOfGoodsSold);
    }

    /**
     * Calcula o lucro líquido atual, descontando impostos.
     *
     * @param businessType Tipo de negócio para cálculo de impostos
     * @return Lucro líquido total após dedução de impostos
     */
    public BigDecimal getCurrentNetProfit(BusinessType businessType) {
        BigDecimal totalSales = getTotalSales();
        BigDecimal totalExpenses = getTotalExpenses();
        BigDecimal taxes = getTaxes(businessType);
        
        // Calcula o lucro líquido subtraindo despesas e impostos
        return totalSales.subtract(totalExpenses).subtract(taxes);
    }

    /**
     * Calcula o lucro líquido atual usando o tipo de negócio PRODUCT_SALES por padrão.
     *
     * @return Lucro líquido total após dedução de impostos
     */
    public BigDecimal getCurrentNetProfit() {
        return getCurrentNetProfit(BusinessType.PRODUCT_SALES);
    }

    /**
     * Calcula os impostos para o total de vendas atual com base no tipo de negócio.
     *
     * @param businessType Tipo de negócio para cálculo de impostos
     * @return Valor total de impostos
     */
    public BigDecimal getTaxes(BusinessType businessType) {
        // Obtém o total de vendas atual
        BigDecimal totalSales = getTotalSales();
        
        // Converte o tipo de negócio e calcula os impostos
        ReportService.BusinessType serviceBusinessType = convertBusinessType(businessType);
        return reportService.calculateTaxes(totalSales, serviceBusinessType);
    }

    /**
     * Classe interna para representar o resumo financeiro.
     */
    protected static class FinancialSummary {
        private final BigDecimal totalSales;
        private final BigDecimal totalExpenses;
        private final BigDecimal grossProfit;
        private final BigDecimal taxes;
        private final BigDecimal netProfit;

        public FinancialSummary(
                BigDecimal totalSales,
                BigDecimal totalExpenses,
                BigDecimal grossProfit,
                BigDecimal taxes,
                BigDecimal netProfit) {
            this.totalSales = totalSales;
            this.totalExpenses = totalExpenses;
            this.grossProfit = grossProfit;
            this.taxes = taxes;
            this.netProfit = netProfit;
        }

        // Getters para acesso aos valores
        public BigDecimal getTotalSales() {
            return totalSales;
        }

        public BigDecimal getTotalExpenses() {
            return totalExpenses;
        }

        public BigDecimal getGrossProfit() {
            return grossProfit;
        }

        public BigDecimal getTaxes() {
            return taxes;
        }

        public BigDecimal getNetProfit() {
            return netProfit;
        }
    }
}