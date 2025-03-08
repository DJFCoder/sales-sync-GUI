package br.com.devjf.salessync.controller;

import java.util.List;
import java.util.Map;
import br.com.devjf.salessync.model.Customer;
import br.com.devjf.salessync.service.CustomerService;

public class CustomerController {
    private final CustomerService customerService;
    
    public CustomerController() {
        this.customerService = new CustomerService();
    }
    
    public boolean createCustomer(Customer customer) {
        return customerService.createCustomer(customer);
    }
    
    public boolean updateCustomer(Customer customer) {
        return customerService.updateCustomer(customer);
    }
    
    public boolean deleteCustomer(Integer id) {
        return customerService.deleteCustomer(id);
    }
    
    public Customer findCustomerById(Integer id) {
        return customerService.findCustomerById(id);
    }
    
    public Customer findCustomerByTaxId(String taxId) {
        return customerService.findCustomerByTaxId(taxId);
    }
    
    public List<Customer> searchCustomers(String searchTerm) {
        return customerService.searchCustomers(searchTerm);
    }
    
    public List<Customer> listAllCustomers() {
        return customerService.listAllCustomers();
    }
    
    public Map<String, Object> getCustomerStatistics(Integer customerId) {
        return customerService.getCustomerStatistics(customerId);
    }
}