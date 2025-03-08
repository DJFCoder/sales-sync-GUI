package br.com.devjf.salessync.service;

import br.com.devjf.salessync.dao.CustomerDAO;
import br.com.devjf.salessync.dao.SaleDAO;
import br.com.devjf.salessync.dao.ServiceOrderDAO;
import br.com.devjf.salessync.model.Customer;
import br.com.devjf.salessync.model.Sale;
import br.com.devjf.salessync.model.ServiceOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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