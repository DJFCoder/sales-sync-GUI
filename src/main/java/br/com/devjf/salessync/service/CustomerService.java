package br.com.devjf.salessync.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.devjf.salessync.dao.CustomerDAO;
import br.com.devjf.salessync.dao.SaleDAO;
import br.com.devjf.salessync.dao.ServiceOrderDAO;
import br.com.devjf.salessync.model.Customer;
import br.com.devjf.salessync.model.Sale;
import br.com.devjf.salessync.model.ServiceOrder;

public class CustomerService {
    
    private final CustomerDAO customerDAO;
    private final SaleDAO saleDAO;
    private final ServiceOrderDAO serviceOrderDAO;
    
    public CustomerService() {
        this.customerDAO = new CustomerDAO();
        this.saleDAO = new SaleDAO();
        this.serviceOrderDAO = new ServiceOrderDAO();
    }
    
    public boolean createCustomer(Customer customer) {
        // Check if tax ID already exists
        if (customerDAO.findByTaxId(customer.getTaxId()) != null) {
            return false;
        }
        
        return customerDAO.save(customer);
    }
    
    public boolean updateCustomer(Customer customer) {
        Customer existingCustomer = customerDAO.findById(customer.getId());
        if (existingCustomer == null) {
            return false;
        }
        
        // Check if tax ID is being changed and if it already exists
        if (!existingCustomer.getTaxId().equals(customer.getTaxId()) && 
            customerDAO.findByTaxId(customer.getTaxId()) != null) {
            return false;
        }
        
        return customerDAO.update(customer);
    }
    
    public Customer findCustomerById(Integer id) {
        return customerDAO.findById(id);
    }
    
    public Customer findCustomerByTaxId(String taxId) {
        return customerDAO.findByTaxId(taxId);
    }
    
    public List<Customer> listAllCustomers() {
        return customerDAO.findAll();
    }
    
    public Map<String, Object> getCompleteHistory(Integer customerId) {
        Customer customer = customerDAO.findById(customerId);
        if (customer == null) {
            return null;
        }
        
        Map<String, Object> history = new HashMap<>();
        history.put("customer", customer);
        
        List<Sale> sales = saleDAO.findByCustomer(customer);
        history.put("sales", sales);
        
        List<ServiceOrder> serviceOrders = serviceOrderDAO.findByCustomer(customer);
        history.put("serviceOrders", serviceOrders);
        
        return history;
    }
    
    public boolean deleteCustomer(Integer id) {
        Customer customer = customerDAO.findById(id);
        if (customer == null) {
            return false;
        }        
        // Perform direct deletion without soft delete
        return customerDAO.delete(id);
    }
    
    public List<Customer> searchCustomers(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return listAllCustomers();
        }
        
        // Search in all customer fields
        List<Customer> allCustomers = customerDAO.findAll();
        String term = searchTerm.toLowerCase().trim();
        
        return allCustomers.stream()
            .filter(customer -> 
                (customer.getName() != null && customer.getName().toLowerCase().contains(term)) ||
                (customer.getTaxId() != null && customer.getTaxId().contains(term)) ||
                (customer.getEmail() != null && customer.getEmail().toLowerCase().contains(term)) ||
                (customer.getPhone() != null && customer.getPhone().contains(term)) ||
                (customer.getAddress() != null && customer.getAddress().toLowerCase().contains(term))
            )
            .collect(Collectors.toList());
    }
    
    public Map<String, Object> getCustomerStatistics(Integer customerId) {
        Customer customer = customerDAO.findById(customerId);
        if (customer == null) {
            return null;
        }
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("customer", customer);
        
        // Get all sales for this customer
        List<Sale> sales = saleDAO.findByCustomer(customer);
        statistics.put("totalSales", sales.size());
        
        // Calculate total revenue from sales
        double totalRevenue = sales.stream()
            .mapToDouble(Sale::getTotalAmount)
            .sum();
        statistics.put("totalRevenue", totalRevenue);
        
        // Get all service orders for this customer
        List<ServiceOrder> serviceOrders = serviceOrderDAO.findByCustomer(customer);
        statistics.put("totalServiceOrders", serviceOrders.size());
        
        // Calculate average sale value
        statistics.put("averageSaleValue", sales.isEmpty() ? 0 : totalRevenue / sales.size());
        
        // Get last purchase date
        sales.stream()
            .max((s1, s2) -> s1.getDate().compareTo(s2.getDate()))
            .ifPresent(sale -> statistics.put("lastPurchaseDate", sale.getDate()));
        
        return statistics;
    }
    
    public boolean validateTaxId(String taxId) {
        // This would contain validation logic for CPF/CNPJ
        // For simplicity, just checking if it's not empty and has the right length
        if (taxId == null || taxId.trim().isEmpty()) {
            return false;
        }
        
        // Remove non-numeric characters
        String numericTaxId = taxId.replaceAll("\\D", "");
        
        // Check if it's a CPF (11 digits) or CNPJ (14 digits)
        return numericTaxId.length() == 11 || numericTaxId.length() == 14;
    }
}