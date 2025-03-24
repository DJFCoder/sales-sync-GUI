package br.com.devjf.salessync.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import br.com.devjf.salessync.dao.SaleDAO;
import br.com.devjf.salessync.dao.SaleItemDAO;
import br.com.devjf.salessync.model.PaymentMethod;
import br.com.devjf.salessync.model.Sale;
import br.com.devjf.salessync.model.SaleItem;
import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.model.UserType;

public class SaleService {
    private final SaleDAO saleDAO;
    private final SaleItemDAO saleItemDAO;

    public SaleService() {
        this.saleDAO = new SaleDAO();
        this.saleItemDAO = new SaleItemDAO();
    }

    public boolean registerSale(Sale sale) {
        // Calculate total amount before saving
        sale.calculateTotal();
        // Garantir que cada item da venda tenha referência para a venda
        for (SaleItem item : sale.getItems()) {
            item.setSale(sale);
        }
        // Salvar a venda com cascade para os itens
        boolean success = saleDAO.save(sale);
        if (success && sale.getUser() != null) {
            logSaleOperation(sale,
                    "REGISTER",
                    sale.getUser());
        }
        return success;
    }

    public boolean updateSale(Sale sale) {
        Sale existingSale = saleDAO.findById(sale.getId());
        if (existingSale == null) {
            return false;
        }
        // Calculate total amount before updating
        sale.calculateTotal();
        boolean success = saleDAO.update(sale);
        if (success && sale.getUser() != null) {
            logSaleOperation(sale,
                    "UPDATE",
                    sale.getUser());
        }
        return success;
    }

    public boolean cancelSale(Integer id) {
        // In a real application, you might want to keep the sale record
        // and just mark it as canceled instead of deleting it
        Sale sale = saleDAO.findById(id);
        if (sale == null) {
            return false;
        }
        // Implementação de soft delete conforme as boas práticas
        // Adicionar um campo 'canceled' na entidade Sale e marcar como true
        sale.setCanceled(true);
        boolean success = saleDAO.update(sale);
        if (success && sale.getUser() != null) {
            logSaleOperation(sale,
                    "CANCEL",
                    sale.getUser());
        }
        return success;
    }

    public Sale findSaleById(Integer id) {
        return saleDAO.findById(id);
    }

    public List<Sale> listSales(Map<String, Object> filters) {
        if (filters == null || filters.isEmpty()) {
            return saleDAO.findAll();
        }
        // Handle different filter types
        if (filters.containsKey("customerId")) {
            Integer customerId = (Integer) filters.get("customerId");
            return saleDAO.findByCustomer(
                    saleDAO.findById(customerId).getCustomer());
        } else if (filters.containsKey("startDate") && filters.containsKey(
                "endDate")) {
            LocalDateTime startDate = (LocalDateTime) filters.get("startDate");
            LocalDateTime endDate = (LocalDateTime) filters.get("endDate");
            return saleDAO.findByDateRange(startDate,
                    endDate);
        }
        return saleDAO.findAll();
    }

    public Map<String, Double> calculateCommissions(Sale sale, User user) {
        Map<String, Double> commissions = new HashMap<>();
        // Only calculate commissions for employees
        // Corrigido para usar UserType.EMPLOYEE conforme a documentação
        if (user.getType() != UserType.EMPLOYEE) {
            commissions.put("commission",
                    0.0);
            return commissions;
        }
        // Simple commission calculation (5% of total)
        double commissionRate = 0.05;
        double commissionAmount = sale.getTotalAmount() * commissionRate;
        commissions.put("rate",
                commissionRate);
        commissions.put("amount",
                commissionAmount);
        return commissions;
    }

    public Sale applyDiscounts(Sale sale, double discountPercentage) {
        if (discountPercentage <= 0 || discountPercentage > 100) {
            return sale;
        }
        double discountFactor = 1 - (discountPercentage / 100);
        // Apply discount to each item
        for (SaleItem item : sale.getItems()) {
            double discountedPrice = item.getUnitPrice() * discountFactor;
            item.setUnitPrice(discountedPrice);
        }
        // Recalculate total
        sale.calculateTotal();
        return sale;
    }

    public double calculateProfitMargin(Sale sale) {
        // In a real application, this would consider the cost of goods sold
        // For simplicity, assuming a fixed cost of 70% of the sale price
        double costFactor = 0.7;
        double totalCost = sale.getTotalAmount() * costFactor;
        double profit = sale.getTotalAmount() - totalCost;
        // Return profit as a percentage of total revenue
        return (profit / sale.getTotalAmount()) * 100;
    }

    // Método adicional para atender ao requisito de histórico de compras do cliente
    public List<Sale> getCustomerPurchaseHistory(Integer customerId) {
        return saleDAO.findByCustomerId(customerId);
    }

    /**
     * Verifica se o usuário tem permissão para cancelar uma venda Apenas
     * usuários do tipo OWNER podem cancelar vendas
     */
    public boolean canCancelSale(User user) {
        return user != null && user.getType() == UserType.OWNER;
    }

    /**
     * Busca vendas por período
     */
    public List<Sale> getSalesByPeriod(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(
                1);
        return saleDAO.findByDateRange(startDateTime,
                endDateTime);
    }

    /**
     * Calcula o total de vendas por período
     */
    public double calculateTotalSalesByPeriod(LocalDate startDate, LocalDate endDate) {
        List<Sale> sales = getSalesByPeriod(startDate,
                endDate);
        return sales.stream().mapToDouble(Sale::getTotalAmount).sum();
    }

    /**
     * Processa o pagamento da venda
     */
    public boolean processSalePayment(Sale sale, PaymentMethod paymentMethod) {
        if (sale == null || paymentMethod == null) {
            return false;
        }
        sale.setPaymentMethod(paymentMethod);
        sale.setPaymentDate(LocalDateTime.now());
        boolean success = saleDAO.update(sale);
        if (success && sale.getUser() != null) {
            logSaleOperation(sale,
                    "PAYMENT",
                    sale.getUser());
        }
        return success;
    }

    /**
     * Valida os dados da venda antes de salvar
     */
    public boolean validateSale(Sale sale) {
        return sale != null
                && sale.getCustomer() != null
                && sale.getItems() != null
                && !sale.getItems().isEmpty()
                && sale.getItems().stream().allMatch(item
                        -> item.getQuantity() > 0 && item.getUnitPrice() > 0);
    }

    /**
     * Gera relatório resumido de vendas
     */
    public Map<String, Object> generateSalesSummary(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> summary = new HashMap<>();
        List<Sale> sales = getSalesByPeriod(startDate,
                endDate);
        double totalRevenue = sales.stream().mapToDouble(Sale::getTotalAmount).sum();
        int totalSales = sales.size();
        double averageTicket = totalSales > 0 ? totalRevenue / totalSales : 0;
        // Agrupamento por método de pagamento
        Map<PaymentMethod, Double> revenueByPaymentMethod = new HashMap<>();
        for (Sale sale : sales) {
            PaymentMethod method = sale.getPaymentMethod();
            revenueByPaymentMethod.merge(method,
                    sale.getTotalAmount(),
                    Double::sum);
        }
        summary.put("startDate",
                startDate);
        summary.put("endDate",
                endDate);
        summary.put("totalRevenue",
                totalRevenue);
        summary.put("totalSales",
                totalSales);
        summary.put("averageTicket",
                averageTicket);
        summary.put("revenueByPaymentMethod",
                revenueByPaymentMethod);
        return summary;
    }

    /**
     * Registra log da venda
     */
    private void logSaleOperation(Sale sale, String operation, User user) {
        // Usando os métodos existentes na classe SystemLog
        String details = "Sale " + operation + " - Total: " + sale.getTotalAmount();
        LogService logService = new LogService();
        logService.recordLog(user,
                operation,
                details);
    }
}
