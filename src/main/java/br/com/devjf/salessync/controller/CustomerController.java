package br.com.devjf.salessync.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import br.com.devjf.salessync.model.Customer;
import br.com.devjf.salessync.service.CustomerService;

/**
 * Controller class for managing customer-related operations.
 * Provides methods for creating, updating, deleting, and retrieving customer information.
 */
public class CustomerController {
    private final CustomerService customerService;

    /**
     * Constructs a new CustomerController with a CustomerService instance.
     */
    public CustomerController() {
        this.customerService = new CustomerService();
    }
    
    /**
     * Creates a new customer
     * 
     * @param customer The customer to be created
     * @return true if the customer was successfully created, false otherwise
     */
    public boolean createCustomer(Customer customer) {
        return customerService.createCustomer(customer);
    }
    
    /**
     * Updates an existing customer
     * 
     * @param customer The customer to be updated
     * @return true if the customer was successfully updated, false otherwise
     */
    public boolean updateCustomer(Customer customer) {
        return customerService.updateCustomer(customer);
    }
    
    /**
     * Deletes a customer by ID
     * 
     * @param id The ID of the customer to delete
     * @return true if the customer was successfully deleted, false otherwise
     */
    public boolean deleteCustomer(Integer id) {
        return customerService.deleteCustomer(id);
    }
    
    /**
     * Finds a customer by ID
     * 
     * @param id The ID of the customer to find
     * @return The customer if found, null otherwise
     */
    public Customer findCustomerById(Integer id) {
        return customerService.findCustomerById(id);
    }
    
    /**
     * Finds a customer by tax ID (CPF/CNPJ)
     * 
     * @param taxId The tax ID of the customer to find
     * @return The customer if found, null otherwise
     */
    public Customer findCustomerByTaxId(String taxId) {
        return customerService.findCustomerByTaxId(taxId);
    }
    
    /**
     * Lists all customers
     * 
     * @return A list of all customers
     */
    public List<Customer> findAll() {
        return customerService.listAllCustomers();
    }
    
    /**
     * Lists customers with pagination
     * 
     * @param page The page number (starting from 0)
     * @param pageSize The number of records per page
     * @return A list of customers for the specified page
     */
    public List<Customer> findWithPagination(int page, int pageSize) {
        return customerService.listCustomersWithPagination(page, pageSize);
    }
    
    /**
     * Searches for customers by name with pagination
     * 
     * @param namePattern The pattern to search for in customer names
     * @param page The page number (starting from 0)
     * @param pageSize The number of records per page
     * @return A list of customers matching the pattern for the specified page
     */
    public List<Customer> findByNameWithPagination(String namePattern, int page, int pageSize) {
        return customerService.searchCustomersByName(namePattern, page, pageSize);
    }
    
    /**
     * Searches for customers by multiple criteria with pagination
     * 
     * @param name The customer name to search for (optional)
     * @param taxId The tax ID to search for (optional)
     * @param page The page number (starting from 0)
     * @param pageSize The number of records per page
     * @return A list of customers matching the criteria
     */
    public List<Customer> searchCustomers(String name, String taxId, int page, int pageSize) {
        Map<String, String> criteria = new HashMap<>();
        
        if (name != null && !name.trim().isEmpty()) {
            criteria.put("name", name);
        }
        
        if (taxId != null && !taxId.trim().isEmpty()) {
            criteria.put("taxId", taxId);
        }
        
        return customerService.searchCustomers(criteria, page, pageSize);
    }
    
    /**
     * Validates a customer and returns any validation errors
     * 
     * @param customer The customer to validate
     * @return A string with validation errors or null if valid
     */
    public String validateCustomer(Customer customer) {
        return customerService.validateCustomerWithErrors(customer);
    }
    
    /**
     * Creates a new customer from form data
     * 
     * @param name The customer name
     * @param taxId The customer tax ID (CPF/CNPJ)
     * @param email The customer email (optional)
     * @param phone The customer phone (optional)
     * @param address The customer address (optional)
     * @param notes Additional notes about the customer (optional)
     * @return A string with validation errors or null if the customer was created successfully
     */
    public String createCustomerFromForm(String name, String taxId, String email, 
                                        String phone, String address, String notes) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setTaxId(taxId);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setNotes(notes);
        
        // Validate the customer
        String validationError = validateCustomer(customer);
        if (validationError != null) {
            return validationError;
        }
        
        // Try to create the customer
        boolean success = createCustomer(customer);
        if (!success) {
            return "Erro ao criar cliente. Por favor, tente novamente.";
        }
        
        return null; // No errors
    }
    
    /**
     * Updates an existing customer from form data
     * 
     * @param id The customer ID
     * @param name The customer name
     * @param taxId The customer tax ID (CPF/CNPJ)
     * @param email The customer email (optional)
     * @param phone The customer phone (optional)
     * @param address The customer address (optional)
     * @param notes Additional notes about the customer (optional)
     * @return A string with validation errors or null if the customer was updated successfully
     */
    public String updateCustomerFromForm(Integer id, String name, String taxId, 
                                        String email, String phone, String address, String notes) {
        // Check if customer exists
        Customer customer = findCustomerById(id);
        if (customer == null) {
            return "Cliente não encontrado.";
        }
        
        // Update customer data
        customer.setName(name);
        customer.setTaxId(taxId);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setNotes(notes);
        
        // Validate the customer
        String validationError = validateCustomer(customer);
        if (validationError != null) {
            return validationError;
        }
        
        // Try to update the customer
        boolean success = updateCustomer(customer);
        if (!success) {
            return "Erro ao atualizar cliente. Por favor, tente novamente.";
        }
        
        return null; // No errors
    }
    
    /**
     * Formats a tax ID (CPF/CNPJ) for display
     * 
     * @param taxId The raw tax ID
     * @return The formatted tax ID
     */
    public String formatTaxId(String taxId) {
        return customerService.formatTaxId(taxId);
    }
    
    /**
     * Gets a formatted display string for a customer
     * 
     * @param customer The customer
     * @return A formatted string with customer name and tax ID
     */
    public String getCustomerDisplayString(Customer customer) {
        if (customer == null) {
            return "";
        }
        
        String formattedTaxId = formatTaxId(customer.getTaxId());
        return customer.getName() + " (" + formattedTaxId + ")";
    }
    
    /**
     * Gets a formatted display string for a customer by ID
     * 
     * @param customerId The customer ID
     * @return A formatted string with customer name and tax ID, or empty if customer not found
     */
    public String getCustomerDisplayStringById(Integer customerId) {
        if (customerId == null) {
            return "";
        }
        
        Customer customer = findCustomerById(customerId);
        return getCustomerDisplayString(customer);
    }
    
    /**
     * Filters customers by multiple criteria
     * 
     * @param filters A map of filter criteria (name, taxId, etc.)
     * @return A list of customers matching the filters
     */
    public List<Customer> filterCustomers(Map<String, String> filters) {
        // Create a map for search criteria
        Map<String, String> searchCriteria = new HashMap<>();
        
        // Process filters
        if (filters.containsKey("name") && !filters.get("name").trim().isEmpty()) {
            searchCriteria.put("name", filters.get("name"));
        }
        
        if (filters.containsKey("taxId") && !filters.get("taxId").trim().isEmpty()) {
            searchCriteria.put("taxId", filters.get("taxId"));
        }
        
        // Add more filters as needed
        
        // Use the search method with pagination (first page, large size to get all)
        return customerService.searchCustomers(searchCriteria, 0, 1000);
    }
    
    /**
     * Gets a customer for a sale, handling null values safely
     * 
     * @param customerId The customer ID from the sale
     * @return The customer object, or null if not found
     */
    public Customer getCustomerForSale(Integer customerId) {
        if (customerId == null) {
            return null;
        }
        
        try {
            return findCustomerById(customerId);
        } catch (Exception e) {
            System.err.println("Error getting customer for sale: " + e.getMessage());
            return null;
        }
    }
}
