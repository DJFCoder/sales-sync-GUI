package br.com.devjf.salessync.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import br.com.devjf.salessync.model.Customer;
import br.com.devjf.salessync.model.PaymentMethod;
import br.com.devjf.salessync.model.Sale;
import br.com.devjf.salessync.model.SaleItem;
import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.service.CustomerService;
import br.com.devjf.salessync.service.SaleService;
import java.util.ArrayList;

/**
 * Controller class for managing sale-related operations. Provides methods for
 * creating, updating, deleting, and retrieving sales and sale items.
 */
public class SaleController {
    private final SaleService saleService;
    private final CustomerService customerService;

    /**
     * Constructs a new SaleController with a SaleService instance.
     */
    public SaleController() {
        this.saleService = new SaleService();
        this.customerService = new CustomerService();
    }

    /**
     * Creates a new sale with its items
     *
     * @param sale The sale to be created
     * @return true if the sale was successfully created, false otherwise
     */
    /**
     * Registers a new sale
     *
     * @param sale The sale to register
     * @return The created sale with ID, or null if creation failed
     */
    public Sale registerSale(Sale sale) {
        // Validate the sale before creating
        if (!saleService.validateSale(sale)) {
            return null;
        }
        // Create the sale
        return saleService.createSale(sale);
    }

    /**
     * Updates an existing sale with its items
     *
     * @param sale The sale to be updated
     * @return true if the sale was successfully updated, false otherwise
     */
    /**
     * Updates an existing sale
     *
     * @param sale The sale to update
     * @return The updated sale, or null if update failed
     */
    public Sale updateSale(Sale sale) {
        // Validate the sale before updating
        if (!saleService.validateSale(sale)) {
            return null;
        }
        // Update the sale
        return saleService.updateSale(sale);
    }

    /**
     * Cancels a sale by setting its canceled flag to true
     *
     * @param saleId The ID of the sale to cancel
     * @return true if the sale was successfully canceled, false otherwise
     */
    public boolean cancelSale(Integer saleId) {
        return saleService.cancelSale(saleId);
    }

    /**
     * Finds a sale by its ID
     *
     * @param id The ID of the sale to find
     * @return The sale if found, null otherwise
     */
    public Sale findSaleById(Integer id) {
        return saleService.findSaleById(id);
    }

    /**
     * Finds a sale by its ID with all relationships loaded (customer, items,
     * user)
     *
     * @param id The ID of the sale to find
     * @return The sale with relationships if found, null otherwise
     */
    public Sale findSaleByIdWithRelationships(Integer id) {
        return saleService.findSaleByIdWithRelationships(id);
    }

    /**
     * Lists all sales
     *
     * @return A list of all sales
     */
    public List<Sale> listAllSales() {
        return saleService.listAllSales();
    }

    /**
     * Lists sales with optional filters
     *
     * @param filters Map containing filter criteria (customerName, date,
     * paymentMethod)
     * @return A filtered list of sales
     */
    public List<Sale> listSales(Map<String, Object> filters) {
        return saleService.listSalesWithFilters(filters);
    }

    /**
     * Lists all sales for a specific customer
     *
     * @param customerId The ID of the customer
     * @return A list of sales for the customer
     */
    public List<Sale> listSalesByCustomer(Integer customerId) {
        return saleService.listSalesByCustomer(customerId);
    }

    /**
     * Lists all sales within a date range
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of sales within the date range
     */
    public List<Sale> listSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleService.listSalesByDateRange(startDate,
                endDate);
    }

    /**
     * Gets the customer name for a specific sale
     *
     * @param saleId The ID of the sale
     * @return The customer name or empty string if not found
     */
    public String getCustomerName(Integer saleId) {
        return saleService.getCustomerName(saleId);
    }

    /**
     * Gets the sale date for a specific sale
     *
     * @param saleId The ID of the sale
     * @return The sale date or null if not found
     */
    public LocalDateTime getSaleDate(Integer saleId) {
        return saleService.getSaleDate(saleId);
    }

    /**
     * Gets the payment method for a specific sale in a safe way
     *
     * @param saleId The ID of the sale
     * @return The payment method as string or empty string if not found
     */
    public String getPaymentMethodSafe(Integer saleId) {
        return saleService.getPaymentMethodSafe(saleId);
    }

    /**
     * Gets the payment date for a specific sale
     *
     * @param saleId The ID of the sale
     * @return The payment date or null if not found
     */
    public LocalDateTime getPaymentDate(Integer saleId) {
        return saleService.getPaymentDate(saleId);
    }

    /**
     * Gets the total amount for a specific sale
     *
     * @param saleId The ID of the sale
     * @return The total amount or 0.0 if not found
     */
    public Double getTotalAmount(Integer saleId) {
        return saleService.getTotalAmount(saleId);
    }

    /**
     * Deletes a sale by its ID
     *
     * @param saleId The ID of the sale to delete
     * @return true if the sale was successfully deleted, false otherwise
     */
    public boolean deleteSale(Integer saleId) {
        return saleService.deleteSale(saleId);
    }

    /**
     * Finds a customer by ID
     *
     * @param customerId The ID of the customer to find
     * @return The customer if found, null otherwise
     */
    public Customer findCustomerById(Integer customerId) {
        return customerService.findCustomerById(customerId);
    }

    /**
     * Finds a customer by tax ID
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
    public List<Customer> listAllCustomers() {
        return customerService.listAllCustomers();
    }

    /**
     * Converts a date string to LocalDateTime
     *
     * @param dateStr The date string in format dd/MM/yyyy
     * @return The LocalDateTime object or null if parsing fails
     */
    public LocalDateTime convertStringToLocalDateTime(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = dateFormat.parse(dateStr);
            return date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Formats a LocalDateTime to a string in format dd/MM/yyyy
     *
     * @param dateTime The LocalDateTime to format
     * @return The formatted date string or empty string if dateTime is null
     */
    public String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        Date date = Date.from(
                dateTime.atZone(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    /**
     * Prepares a Sale object with the provided data
     *
     * @param customer The customer for the sale
     * @param paymentMethodStr The payment method as a string
     * @param paymentDateStr The payment date as a string (dd/MM/yyyy)
     * @param items The list of sale items
     * @param subtotal The subtotal amount
     * @param discount The discount amount
     * @param user The user creating the sale
     * @return A prepared Sale object
     * @throws ParseException If the payment date cannot be parsed
     */
    public Sale prepareSaleObject(Customer customer, String paymentMethodStr,
            String paymentDateStr, List<SaleItem> items, double subtotal,
            double discount, User user) throws ParseException {
        // Create a new sale or use existing one if ID is present
        Sale sale = new Sale();
        // Set the customer
        sale.setCustomer(customer);
        // Set the payment method
        PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentMethodStr);
        sale.setPaymentMethod(paymentMethod);
        // Parse and set the payment date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date paymentDate = dateFormat.parse(paymentDateStr);
        // Convert to LocalDateTime
        LocalDateTime paymentDateTime = paymentDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        sale.setPaymentDate(paymentDateTime);
        // Set the current date as the sale date
        sale.setDate(LocalDateTime.now());
        // Set the user
        sale.setUser(user);
        // Set the amounts
        sale.setSubtotalAmount(subtotal);
        sale.setDiscountAmount(discount);
        sale.setTotalAmount(subtotal - discount);
        // Set the items
        List<SaleItem> saleItems = new ArrayList<>();
        for (SaleItem item : items) {
            SaleItem newItem = new SaleItem();
            newItem.setDescription(item.getDescription());
            newItem.setQuantity(item.getQuantity());
            newItem.setUnitPrice(item.getUnitPrice());
            newItem.setSale(sale);
            saleItems.add(newItem);
        }
        sale.setItems(saleItems);
        return sale;
    }

    /**
     * Creates a SaleItem from table data
     *
     * @param description Item description
     * @param quantityObj Quantity as Object (Integer or String)
     * @param priceObj Price as Object (Double or String)
     * @return The created SaleItem
     * @throws IllegalArgumentException If data is invalid
     */
    public SaleItem createSaleItem(String description, Object quantityObj, Object priceObj)
            throws IllegalArgumentException {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Descrição do item não pode ser vazia");
        }
        // Parse quantity
        Integer quantity = 1;
        if (quantityObj instanceof Integer) {
            quantity = (Integer) quantityObj;
        } else if (quantityObj instanceof String) {
            try {
                quantity = Integer.valueOf(quantityObj.toString().trim());
            } catch (NumberFormatException e) {
                // Use default quantity of 1
            }
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantidade deve ser maior que zero");
        }
        // Parse price
        Double unitPrice = 0.0;
        if (priceObj instanceof Double) {
            unitPrice = (Double) priceObj;
        } else if (priceObj instanceof String) {
            try {
                String priceStr = priceObj.toString().replaceAll("[^\\d,.]",
                        "").replace(",",
                                ".");
                unitPrice = Double.valueOf(priceStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Preço inválido");
            }
        }
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }
        // Create and return the sale item
        SaleItem item = new SaleItem();
        item.setDescription(description.trim());
        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);
        return item;
    }

    /**
     * Finds a sale by ID with all its items loaded (for editing)
     *
     * @param id The ID of the sale to find
     * @return The sale with all items loaded if found, null otherwise
     */
    public Sale findSaleByIdForEdit(Integer id) {
        // Use the service method that loads all relationships
        Sale sale = saleService.findSaleByIdWithRelationships(id);
        // Additional safety check to ensure items are loaded
        if (sale != null && sale.getItems() != null) {
            // Force initialization of each item in the collection
            for (SaleItem item : sale.getItems()) {
                // Access properties to ensure they're loaded
                item.getId();
                item.getDescription();
                item.getQuantity();
                item.getUnitPrice();
            }
            // Ensure customer is loaded
            if (sale.getCustomer() != null) {
                sale.getCustomer().getName();
            }
            // Ensure user is loaded
            if (sale.getUser() != null) {
                sale.getUser().getName();
            }
        }
        return sale;
    }
}
