package br.com.devjf.salessync.controller;

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
     * Creates a new customer in the system.
     *
     * @param customer The customer object to be created
     * @return true if the customer was successfully created, false otherwise
     */
    public boolean createCustomer(Customer customer) {
        return customerService.createCustomer(customer);
    }

    /**
     * Updates an existing customer's information.
     *
     * @param customer The customer object with updated information
     * @return true if the customer was successfully updated, false otherwise
     */
    public boolean updateCustomer(Customer customer) {
        return customerService.updateCustomer(customer);
    }

    /**
     * Deletes a customer from the system by ID.
     *
     * @param id The ID of the customer to delete
     * @return true if the customer was successfully deleted, false otherwise
     */
    public boolean deleteCustomer(Integer id) {
        return customerService.deleteCustomer(id);
    }

    /**
     * Finds a customer by their ID.
     *
     * @param id The ID of the customer to find
     * @return The customer object if found, null otherwise
     */
    public Customer findCustomerById(Integer id) {
        return customerService.findCustomerById(id);
    }

    /**
     * Finds a customer by their tax ID (CPF/CNPJ).
     *
     * @param taxId The tax ID of the customer to find
     * @return The customer object if found, null otherwise
     */
    public Customer findCustomerByTaxId(String taxId) {
        return customerService.findCustomerByTaxId(taxId);
    }

    /**
     * Searches for customers based on a search term.
     *
     * @param searchTerm The term to search for in customer records
     * @return A list of customers matching the search term
     */
    public List<Customer> searchCustomers(String searchTerm) {
        return customerService.searchCustomers(searchTerm);
    }

    /**
     * Lists all customers in the system.
     *
     * @return A list of all customers
     */
    public List<Customer> listAllCustomers() {
        return customerService.listAllCustomers();
    }

    /**
     * Gets statistics for a specific customer.
     *
     * @param customerId The ID of the customer to get statistics for
     * @return A map containing various statistics about the customer
     */
    public Map<String, Object> getCustomerStatistics(Integer customerId) {
        return customerService.getCustomerStatistics(customerId);
    }

    /**
     * Finds customers with pagination.
     *
     * @param page The page number (starting from 0)
     * @param pageSize The number of records per page
     * @return List of customers for the specified page
     */
    public List<Customer> findWithPagination(int page, int pageSize) {
        return customerService.findWithPagination(page, pageSize);
    }

    /**
     * Finds customers by name pattern with pagination.
     *
     * @param namePattern The pattern to search for in customer names
     * @param page The page number (starting from 0)
     * @param pageSize The number of records per page
     * @return List of customers matching the pattern for the specified page
     */
    public List<Customer> findByNameWithPagination(String namePattern, int page, int pageSize) {
        return customerService.findByNameWithPagination(namePattern, page, pageSize);
    }
}
