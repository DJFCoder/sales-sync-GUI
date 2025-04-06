package br.com.devjf.salessync.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
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
        LocalDate startDate = (LocalDate) parameters.getOrDefault("startDate",
                LocalDate.now().withDayOfMonth(1));
        LocalDate endDate = (LocalDate) parameters.getOrDefault("endDate",
                LocalDate.now());
        // Convert LocalDate to LocalDateTime for sales query
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(
                1);
        // Get sales data
        List<Sale> sales = saleDAO.findByDateRange(startDateTime,
                endDateTime);
        double totalRevenue = sales.stream().mapToDouble(Sale::getTotalAmount).sum();
        // Get expenses data
        List<Expense> expenses = expenseDAO.findByDateRange(startDate,
                endDate);
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        // Calculate profit/loss
        double netProfit = totalRevenue - totalExpenses;
        // Populate result
        result.put("startDate",
                startDate);
        result.put("endDate",
                endDate);
        result.put("totalRevenue",
                totalRevenue);
        result.put("totalExpenses",
                totalExpenses);
        result.put("netProfit",
                netProfit);
        result.put("sales",
                sales);
        result.put("expenses",
                expenses);
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
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(
                1);
        // Get sales data
        List<Sale> sales = saleDAO.findByDateRange(startDateTime,
                endDateTime);
        double totalRevenue = sales.stream().mapToDouble(Sale::getTotalAmount).sum();
        // Get expenses data
        List<Expense> expenses = expenseDAO.findByDateRange(startDate,
                endDate);
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        // Calculate metrics
        double netProfit = totalRevenue - totalExpenses;
        double profitMargin = totalRevenue > 0 ? (netProfit / totalRevenue) * 100 : 0;
        double returnOnInvestment = totalExpenses > 0 ? (netProfit / totalExpenses) * 100 : 0;
        // Populate result
        result.put("totalRevenue",
                totalRevenue);
        result.put("totalExpenses",
                totalExpenses);
        result.put("netProfit",
                netProfit);
        result.put("profitMargin",
                profitMargin);
        result.put("roi",
                returnOnInvestment);
        return result;
    }

    /**
     * Analisa dados de vendas comparando dois períodos de tempo.
     *
     * @param period1 O primeiro período para comparação
     * @param period2 O segundo período para comparação
     * @return Um mapa contendo os dados de análise de vendas
     */
    public Map<String, Object> analyzeSalesByPeriod(LocalDate period1, LocalDate period2) {
        Map<String, Object> result = new HashMap<>();
        // Converter LocalDate para LocalDateTime para consulta de vendas
        LocalDateTime period1Start = period1.atStartOfDay();
        LocalDateTime period1End = period1.plusDays(1).atStartOfDay().minusNanos(
                1);
        LocalDateTime period2Start = period2.atStartOfDay();
        LocalDateTime period2End = period2.plusDays(1).atStartOfDay().minusNanos(
                1);
        // Obter dados de vendas para o período 1
        List<Sale> salesPeriod1 = saleDAO.findByDateRange(period1Start,
                period1End);
        double totalRevenuePeriod1 = salesPeriod1.stream().mapToDouble(
                Sale::getTotalAmount).sum();
        int totalSalesPeriod1 = salesPeriod1.size();
        // Obter dados de vendas para o período 2
        List<Sale> salesPeriod2 = saleDAO.findByDateRange(period2Start,
                period2End);
        double totalRevenuePeriod2 = salesPeriod2.stream().mapToDouble(
                Sale::getTotalAmount).sum();
        int totalSalesPeriod2 = salesPeriod2.size();
        // Calcular diferenças percentuais
        double revenueChange = calculatePercentageChange(totalRevenuePeriod1,
                totalRevenuePeriod2);
        double salesCountChange = calculatePercentageChange(totalSalesPeriod1,
                totalSalesPeriod2);
        // Preencher resultado
        result.put("period1",
                period1);
        result.put("period2",
                period2);
        result.put("salesPeriod1",
                salesPeriod1);
        result.put("salesPeriod2",
                salesPeriod2);
        result.put("totalRevenuePeriod1",
                totalRevenuePeriod1);
        result.put("totalRevenuePeriod2",
                totalRevenuePeriod2);
        result.put("totalSalesPeriod1",
                totalSalesPeriod1);
        result.put("totalSalesPeriod2",
                totalSalesPeriod2);
        result.put("revenueChange",
                revenueChange);
        result.put("salesCountChange",
                salesCountChange);
        return result;
    }

    /**
     * Calcula a mudança percentual entre dois valores.
     *
     * @param oldValue O valor antigo
     * @param newValue O valor novo
     * @return A mudança percentual
     */
    private double calculatePercentageChange(double oldValue, double newValue) {
        if (oldValue == 0) {
            return newValue > 0 ? 100.0 : 0.0;
        }
        return ((newValue - oldValue) / oldValue) * 100.0;
    }

    /**
     * Analisa despesas por categoria para um período específico.
     *
     * @param categoryId O ID da categoria de despesa a ser analisada
     * @param period O período para analisar despesas
     * @return Um mapa contendo os dados de análise de despesas
     */
    public Map<String, Object> analyzeExpensesByCategory(Integer categoryId, String period) {
        Map<String, Object> result = new HashMap<>();
        LocalDate today = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate = today;
        // Determinar intervalo de datas com base no período
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
        // Obter despesas para a categoria e período especificados
        List<Expense> expenses;
        if (categoryId != null) {
            expenses = expenseDAO.findByDateRange(categoryId,
                    startDate,
                    endDate);
        } else {
            expenses = expenseDAO.findByDateRange(startDate,
                    endDate);
        }
        double totalAmount = expenses.stream().mapToDouble(Expense::getAmount).sum();
        // Preencher resultado
        result.put("categoryId",
                categoryId);
        result.put("period",
                period);
        result.put("startDate",
                startDate);
        result.put("endDate",
                endDate);
        result.put("expenses",
                expenses);
        result.put("totalAmount",
                totalAmount);
        return result;
    }

    /**
     * Exporta um relatório para um arquivo no formato especificado.
     *
     * @param data Os dados do relatório a serem exportados
     * @param filePath O caminho para salvar o relatório exportado
     * @return true se o relatório foi exportado com sucesso, false caso
     * contrário
     */
    public boolean exportReport(Map<String, Object> data, String filePath) {
        try {
            CSVExporter.exportToCSV(data,
                    filePath);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gera um relatório de vendas para um cliente específico.
     *
     * @param parameters Os parâmetros para o relatório
     * @return Um mapa contendo os dados do relatório de vendas do cliente
     */
    public Map<String, Object> generateCustomerSalesReport(Map<String, Object> parameters) {
        Map<String, Object> result = new HashMap<>();
        // Extrair parâmetros
        Integer customerId = (Integer) parameters.get("customerId");
        LocalDate startDate = (LocalDate) parameters.getOrDefault("startDate",
                LocalDate.now().minusMonths(1));
        LocalDate endDate = (LocalDate) parameters.getOrDefault("endDate",
                LocalDate.now());
        // Converter LocalDate para LocalDateTime para consulta de vendas
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(
                1);
        // Obter vendas para o cliente específico
        List<Sale> customerSales = saleDAO.findByCustomerAndDateRange(customerId,
                startDateTime,
                endDateTime);
        double totalRevenue = customerSales.stream().mapToDouble(
                Sale::getTotalAmount).sum();
        int totalSales = customerSales.size();
        // Preencher resultado
        result.put("customerId",
                customerId);
        result.put("startDate",
                startDate);
        result.put("endDate",
                endDate);
        result.put("sales",
                customerSales);
        result.put("totalRevenue",
                totalRevenue);
        result.put("totalSales",
                totalSales);
        return result;
    }

    /**
     * Gera um relatório de desempenho de produtos.
     *
     * @param parameters Os parâmetros para o relatório
     * @return Um mapa contendo os dados de desempenho dos produtos
     */
    public Map<String, Object> generateProductPerformanceReport(Map<String, Object> parameters) {
        Map<String, Object> result = new HashMap<>();
        // Extrair parâmetros
        LocalDate startDate = (LocalDate) parameters.getOrDefault("startDate",
                LocalDate.now().minusMonths(1));
        LocalDate endDate = (LocalDate) parameters.getOrDefault("endDate",
                LocalDate.now());
        // Converter LocalDate para LocalDateTime para consulta de vendas
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(
                1);
        // Obter vendas para o período
        List<Sale> sales = saleDAO.findByDateRange(startDateTime,
                endDateTime);
        // Analisar desempenho dos produtos
        Map<Integer, Integer> productSalesCount = new HashMap<>();
        Map<Integer, Double> productSalesRevenue = new HashMap<>();
        for (Sale sale : sales) {
            // Nota: Isso depende da estrutura da sua classe Sale
            // Assumindo que Sale tem um método getItems() que retorna uma lista de itens vendidos
            sale.getItems().forEach(item -> {
                Integer productId = item.getId();
                productSalesCount.put(productId,
                        productSalesCount.getOrDefault(productId,
                                0) + item.getQuantity());
                productSalesRevenue.put(productId,
                        productSalesRevenue.getOrDefault(productId,
                                0.0) + item.calculateSubtotal());
            });
        }
        // Preencher resultado
        result.put("startDate",
                startDate);
        result.put("endDate",
                endDate);
        result.put("productSalesCount",
                productSalesCount);
        result.put("productSalesRevenue",
                productSalesRevenue);
        return result;
    }

    /**
     * Gera um resumo mensal de vendas.
     *
     * @param parameters Os parâmetros para o relatório
     * @return Um mapa contendo os dados do resumo mensal de vendas
     */
    public Map<String, Object> generateMonthlySalesSummary(Map<String, Object> parameters) {
        Map<String, Object> result = new HashMap<>();
        // Extrair parâmetros
        int year = (int) parameters.getOrDefault("year",
                LocalDate.now().getYear());
        int month = (int) parameters.getOrDefault("month",
                LocalDate.now().getMonthValue());
        // Criar intervalo de datas para o mês
        LocalDate startDate = LocalDate.of(year,
                month,
                1);
        LocalDate endDate = YearMonth.of(year,
                month).atEndOfMonth();
        // Converter LocalDate para LocalDateTime para consulta de vendas
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(
                1);
        // Obter vendas para o mês
        List<Sale> monthlySales = saleDAO.findByDateRange(startDateTime,
                endDateTime);
        double totalRevenue = monthlySales.stream().mapToDouble(
                Sale::getTotalAmount).sum();
        int totalSales = monthlySales.size();
        // Agrupar vendas por dia
        Map<Integer, Double> dailyRevenue = new HashMap<>();
        Map<Integer, Integer> dailySalesCount = new HashMap<>();
        for (Sale sale : monthlySales) {
            int day = sale.getDate().getDayOfMonth();
            dailyRevenue.put(day,
                    dailyRevenue.getOrDefault(day,
                            0.0) + sale.getTotalAmount());
            dailySalesCount.put(day,
                    dailySalesCount.getOrDefault(day,
                            0) + 1);
        }
        // Preencher resultado
        result.put("year",
                year);
        result.put("month",
                month);
        result.put("startDate",
                startDate);
        result.put("endDate",
                endDate);
        result.put("sales",
                monthlySales);
        result.put("totalRevenue",
                totalRevenue);
        result.put("totalSales",
                totalSales);
        result.put("dailyRevenue",
                dailyRevenue);
        result.put("dailySalesCount",
                dailySalesCount);
        return result;
    }

    /**
     * Gera um relatório de status de ordens de serviço.
     *
     * @param parameters Os parâmetros para o relatório
     * @return Um mapa contendo os dados de status das ordens de serviço
     */
    public Map<String, Object> generateServiceOrderStatusReport(Map<String, Object> parameters) {
        Map<String, Object> result = new HashMap<>();
        // Extrair parâmetros
        LocalDate startDate = (LocalDate) parameters.getOrDefault("startDate",
                LocalDate.now().minusMonths(1));
        LocalDate endDate = (LocalDate) parameters.getOrDefault("endDate",
                LocalDate.now());
        // Converter LocalDate para LocalDateTime para consulta
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(
                1);
        // Aqui você precisaria de um DAO para ServiceOrder
        // Por enquanto, vamos criar um resultado simulado
        Map<String, Integer> statusCounts = new HashMap<>();
        statusCounts.put("PENDING",
                10);
        statusCounts.put("IN_PROGRESS",
                15);
        statusCounts.put("COMPLETED",
                25);
        statusCounts.put("CANCELLED",
                5);
        // Preencher resultado
        result.put("startDate",
                startDate);
        result.put("endDate",
                endDate);
        result.put("statusCounts",
                statusCounts);
        return result;
    }

    /**
     * Gera um relatório de fluxo de caixa.
     *
     * @param parameters Os parâmetros para o relatório
     * @return Um mapa contendo os dados de fluxo de caixa
     */
    public Map<String, Object> generateCashFlowReport(Map<String, Object> parameters) {
        Map<String, Object> result = new HashMap<>();
        // Extrair parâmetros
        LocalDate startDate = (LocalDate) parameters.getOrDefault("startDate",
                LocalDate.now().minusMonths(1));
        LocalDate endDate = (LocalDate) parameters.getOrDefault("endDate",
                LocalDate.now());
        // Converter LocalDate para LocalDateTime para consulta de vendas
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(
                1);
        // Obter vendas para o período
        List<Sale> sales = saleDAO.findByDateRange(startDateTime,
                endDateTime);
        double totalIncome = sales.stream().mapToDouble(Sale::getTotalAmount).sum();
        // Obter despesas para o período
        List<Expense> expenses = expenseDAO.findByDateRange(startDate,
                endDate);
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        // Calcular fluxo de caixa líquido
        double netCashFlow = totalIncome - totalExpenses;
        // Agrupar por dia
        Map<LocalDate, Double> dailyIncome = new HashMap<>();
        Map<LocalDateTime, Double> dailyExpenses = new HashMap<>();
        Map<LocalDate, Double> dailyNetCashFlow = new HashMap<>();
        for (Sale sale : sales) {
            LocalDate saleDate = sale.getDate().toLocalDate();
            dailyIncome.put(saleDate,
                    dailyIncome.getOrDefault(saleDate,
                            0.0) + sale.getTotalAmount());
        }
        for (Expense expense : expenses) {
            LocalDateTime expenseDate = expense.getDate();
            dailyExpenses.put(expenseDate,
                    dailyExpenses.getOrDefault(expenseDate,
                            0.0) + expense.getAmount());
        }
        // Calcular fluxo de caixa líquido diário
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(
                1)) {
            double income = dailyIncome.getOrDefault(date,
                    0.0);
            double expense = dailyExpenses.getOrDefault(date,
                    0.0);
            dailyNetCashFlow.put(date,
                    income - expense);
        }
        // Preencher resultado
        result.put("startDate",
                startDate);
        result.put("endDate",
                endDate);
        result.put("totalIncome",
                totalIncome);
        result.put("totalExpenses",
                totalExpenses);
        result.put("netCashFlow",
                netCashFlow);
        result.put("dailyIncome",
                dailyIncome);
        result.put("dailyExpenses",
                dailyExpenses);
        result.put("dailyNetCashFlow",
                dailyNetCashFlow);
        return result;
    }

    /**
     * Enum para definir o tipo de negócio para cálculo de impostos.
     */
    public enum BusinessType {
        PRODUCT_SALES,   // Venda de mercadorias e produtos
        SERVICE_BASED    // Serviços como manutenção, contabilidade, etc.
    }

    /**
     * Classe interna para representar a faixa de tributação.
     */
    private static class TaxBracket {
        final BigDecimal minValue;
        final BigDecimal maxValue;
        final BigDecimal taxRate;
        final BigDecimal deduction;

        TaxBracket(double minValue, double maxValue, double taxRate, double deduction) {
            this.minValue = BigDecimal.valueOf(minValue);
            this.maxValue = BigDecimal.valueOf(maxValue);
            this.taxRate = BigDecimal.valueOf(taxRate / 100);
            this.deduction = BigDecimal.valueOf(deduction);
        }
    }

    // Faixas de tributação para vendas de produtos
    private static final TaxBracket[] PRODUCT_TAX_BRACKETS = {
        new TaxBracket(0, 180_000.00, 4.00, 0.00),
        new TaxBracket(180_000.01, 360_000.00, 7.30, 5_940.00),
        new TaxBracket(360_000.01, 720_000.00, 9.50, 13_860.00),
        new TaxBracket(720_000.01, 1_800_000.00, 10.70, 22_500.00),
        new TaxBracket(1_800_000.01, 3_600_000.00, 14.30, 87_300.00),
        new TaxBracket(3_600_000.01, 4_800_000.00, 19.00, 378_000.00)
    };

    // Faixas de tributação para serviços
    private static final TaxBracket[] SERVICE_TAX_BRACKETS = {
        new TaxBracket(0, 180_000.00, 6.00, 0.00),
        new TaxBracket(180_000.01, 360_000.00, 11.20, 9_360.00),
        new TaxBracket(360_000.01, 720_000.00, 13.50, 17_640.00),
        new TaxBracket(720_000.01, 1_800_000.00, 16.00, 35_640.00),
        new TaxBracket(1_800_000.01, 3_600_000.00, 21.00, 125_640.00),
        new TaxBracket(3_600_000.01, 4_800_000.00, 33.00, 648_000.00)
    };

    /**
     * Calcula o valor de impostos com base no tipo de negócio e receita bruta.
     *
     * @param grossRevenue Receita bruta dos últimos 12 meses
     * @param businessType Tipo de negócio (produtos ou serviços)
     * @return Valor calculado de impostos
     */
    public BigDecimal calculateTaxes(BigDecimal grossRevenue, BusinessType businessType) {
        TaxBracket[] brackets = (businessType == BusinessType.PRODUCT_SALES) 
            ? PRODUCT_TAX_BRACKETS 
            : SERVICE_TAX_BRACKETS;

        for (TaxBracket bracket : brackets) {
            if (grossRevenue.compareTo(bracket.minValue) >= 0 && 
                grossRevenue.compareTo(bracket.maxValue) <= 0) {
                
                // Cálculo do imposto: (Receita * Alíquota) - Dedução
                BigDecimal taxAmount = grossRevenue
                    .multiply(bracket.taxRate)
                    .subtract(bracket.deduction)
                    .setScale(2, RoundingMode.HALF_UP);

                return taxAmount;
            }
        }

        // Se não encontrar uma faixa, retorna 0 ou lança exceção
        return BigDecimal.ZERO;
    }

    /**
     * Calcula o lucro líquido subtraindo impostos do lucro bruto.
     *
     * @param grossProfit Lucro bruto
     * @param taxes Valor de impostos
     * @return Lucro líquido
     */
    public BigDecimal calculateNetProfit(BigDecimal grossProfit, BigDecimal taxes) {
        return grossProfit.subtract(taxes).setScale(2, RoundingMode.HALF_UP);
    }
}
