package br.com.devjf.salessync.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.devjf.salessync.dao.CustomerDAO;
import br.com.devjf.salessync.model.Customer;

public class CustomerService {
    private final CustomerDAO customerDAO;

    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }
    
    /**
     * Creates a new customer
     * 
     * @param customer The customer to be created
     * @return true if the customer was successfully created, false otherwise
     */
    public boolean createCustomer(Customer customer) {
        // Validate customer data
        if (!validateCustomer(customer)) {
            return false;
        }
        
        // Check if tax ID already exists
        Customer existingCustomer = customerDAO.findByTaxId(customer.getTaxId());
        if (existingCustomer != null) {
            return false;
        }
        
        return customerDAO.save(customer);
    }
    
    /**
     * Updates an existing customer
     * 
     * @param customer The customer to be updated
     * @return true if the customer was successfully updated, false otherwise
     */
    public boolean updateCustomer(Customer customer) {
        // Validate customer data
        if (!validateCustomer(customer) || customer.getId() == null) {
            return false;
        }
        
        // Check if tax ID already exists for another customer
        Customer existingCustomer = customerDAO.findByTaxId(customer.getTaxId());
        if (existingCustomer != null && !existingCustomer.getId().equals(customer.getId())) {
            return false;
        }
        
        return customerDAO.update(customer);
    }
    
    /**
     * Deletes a customer by ID
     * 
     * @param id The ID of the customer to delete
     * @return true if the customer was successfully deleted, false otherwise
     */
    public boolean deleteCustomer(Integer id) {
        return customerDAO.delete(id);
    }
    
    /**
     * Finds a customer by ID
     * 
     * @param id The ID of the customer to find
     * @return The customer if found, null otherwise
     */
    public Customer findCustomerById(Integer id) {
        return customerDAO.findById(id);
    }
    
    /**
     * Finds a customer by tax ID
     * 
     * @param taxId The tax ID of the customer to find
     * @return The customer if found, null otherwise
     */
    public Customer findCustomerByTaxId(String taxId) {
        return customerDAO.findByTaxId(taxId);
    }
    
    /**
     * Lists all customers
     * 
     * @return A list of all customers
     */
    public List<Customer> listAllCustomers() {
        return customerDAO.findAll();
    }
    
    /**
     * Lists customers with pagination
     * 
     * @param page The page number (starting from 0)
     * @param pageSize The number of records per page
     * @return A list of customers for the specified page
     */
    public List<Customer> listCustomersWithPagination(int page, int pageSize) {
        return customerDAO.findWithPagination(page, pageSize);
    }
    
    /**
     * Searches for customers by name with pagination
     * 
     * @param namePattern The pattern to search for in customer names
     * @param page The page number (starting from 0)
     * @param pageSize The number of records per page
     * @return A list of customers matching the pattern for the specified page
     */
    public List<Customer> searchCustomersByName(String namePattern, int page, int pageSize) {
        return customerDAO.findByNameWithPagination(namePattern, page, pageSize);
    }
    
    /**
     * Searches for customers by multiple criteria
     * 
     * @param criteria Map containing search criteria (name, taxId, email, phone)
     * @param page The page number (starting from 0)
     * @param pageSize The number of records per page
     * @return A list of customers matching the criteria
     */
    public List<Customer> searchCustomers(Map<String, String> criteria, int page, int pageSize) {
        // This would require implementation in the DAO layer
        // For now, we'll use the existing methods
        
        if (criteria.containsKey("name") && !criteria.get("name").trim().isEmpty()) {
            return searchCustomersByName(criteria.get("name"), page, pageSize);
        }
        
        if (criteria.containsKey("taxId") && !criteria.get("taxId").trim().isEmpty()) {
            Customer customer = findCustomerByTaxId(criteria.get("taxId"));
            if (customer != null) {
                List<Customer> result = new ArrayList<>();
                result.add(customer);
                return result;
            }
            return new ArrayList<>();
        }
        
        // Default to pagination
        return listCustomersWithPagination(page, pageSize);
    }
    
    /**
     * Validates a customer object
     * 
     * @param customer The customer to validate
     * @return true if the customer is valid, false otherwise
     */
    private boolean validateCustomer(Customer customer) {
        if (customer == null) {
            return false;
        }
        
        // Name is required and must not be empty
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            return false;
        }
        
        // Tax ID is required and must not be empty
        if (customer.getTaxId() == null || customer.getTaxId().trim().isEmpty()) {
            return false;
        }
        
        // Email validation - if provided, must be valid format
        if (customer.getEmail() != null && !customer.getEmail().trim().isEmpty()) {
            // Simple email validation
            String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (!customer.getEmail().matches(emailRegex)) {
                return false;
            }
        }
        
        // Phone validation - if provided, must be valid format
        if (customer.getPhone() != null && !customer.getPhone().trim().isEmpty()) {
            // Remove non-numeric characters for validation
            String phoneDigits = customer.getPhone().replaceAll("[^0-9]", "");
            // Check if phone has a reasonable number of digits
            if (phoneDigits.length() < 8 || phoneDigits.length() > 15) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Validates a customer object and returns validation errors
     * 
     * @param customer The customer to validate
     * @return A string with validation errors or null if valid
     */
    public String validateCustomerWithErrors(Customer customer) {
        if (customer == null) {
            return "Cliente não pode ser nulo";
        }
        
        // Name validation
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            return "Nome do cliente é obrigatório";
        }
        
        // Tax ID validation
        if (customer.getTaxId() == null || customer.getTaxId().trim().isEmpty()) {
            return "CPF/CNPJ é obrigatório";
        }
        
        // Email validation
        if (customer.getEmail() != null && !customer.getEmail().trim().isEmpty()) {
            String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (!customer.getEmail().matches(emailRegex)) {
                return "Formato de e-mail inválido";
            }
        }
        
        // Phone validation
        if (customer.getPhone() != null && !customer.getPhone().trim().isEmpty()) {
            String phoneDigits = customer.getPhone().replaceAll("[^0-9]", "");
            if (phoneDigits.length() < 8 || phoneDigits.length() > 15) {
                return "Formato de telefone inválido";
            }
        }
        
        // Check if tax ID already exists (for new customers)
        if (customer.getId() == null) {
            Customer existingCustomer = findCustomerByTaxId(customer.getTaxId());
            if (existingCustomer != null) {
                return "CPF/CNPJ já cadastrado no sistema";
            }
        } else {
            // For existing customers, check if tax ID belongs to another customer
            Customer existingCustomer = findCustomerByTaxId(customer.getTaxId());
            if (existingCustomer != null && !existingCustomer.getId().equals(customer.getId())) {
                return "CPF/CNPJ já cadastrado para outro cliente";
            }
        }
        
        return null; // No validation errors
    }
    
    /**
     * Formats a tax ID (CPF/CNPJ) for display
     * 
     * @param taxId The raw tax ID
     * @return The formatted tax ID
     */
    public String formatTaxId(String taxId) {
        if (taxId == null || taxId.trim().isEmpty()) {
            return "";
        }
        
        // Remove non-numeric characters
        String digits = taxId.replaceAll("[^0-9]", "");
        
        // Format as CPF (xxx.xxx.xxx-xx)
        if (digits.length() <= 11) {
            if (digits.length() < 11) {
                // Pad with zeros if needed
                digits = String.format("%011d", Long.parseLong(digits));
            }
            return digits.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        } 
        // Format as CNPJ (xx.xxx.xxx/xxxx-xx)
        else {
            if (digits.length() < 14) {
                // Pad with zeros if needed
                digits = String.format("%014d", Long.parseLong(digits));
            }
            return digits.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        }
    }
}
