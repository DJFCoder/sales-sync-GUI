package br.com.devjf.salessync.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.devjf.salessync.dao.SaleDAO;
import br.com.devjf.salessync.dao.SaleItemDAO;
import br.com.devjf.salessync.model.Customer;
import br.com.devjf.salessync.model.PaymentMethod;
import br.com.devjf.salessync.model.Sale;
import br.com.devjf.salessync.model.SaleItem;
import br.com.devjf.salessync.model.User;

public class SaleService {
    private final SaleDAO saleDAO;
    private final SaleItemDAO saleItemDAO;

    public SaleService() {
        this.saleDAO = new SaleDAO();
        this.saleItemDAO = new SaleItemDAO();
    }

    /**
     * Creates a new sale with its items
     *
     * @param sale The sale to be created
     * @return The created sale with ID, or null if creation failed
     */
    public Sale createSale(Sale sale) {
        if (!validateSale(sale)) {
            return null;
        }
        // Log the incoming sale values for debugging
        System.out.println("Creating sale with subtotal: " + sale.getSubtotalAmount() + 
                           ", discount: " + sale.getDiscountAmount() + 
                           ", total: " + sale.getTotalAmount());
        
        // Create a new sale object to avoid detached entity issues
        Sale newSale = new Sale();
        newSale.setDate(sale.getDate());
        newSale.setCustomer(sale.getCustomer());
        newSale.setUser(sale.getUser());
        newSale.setPaymentMethod(sale.getPaymentMethod());
        newSale.setPaymentDate(sale.getPaymentDate());
        
        // Ensure these values are explicitly set
        newSale.setSubtotalAmount(sale.getSubtotalAmount());
        newSale.setDiscountAmount(sale.getDiscountAmount());
        newSale.setTotalAmount(sale.getTotalAmount());
        
        // Save the sale first
        boolean saleSuccess = saleDAO.save(newSale);
        if (!saleSuccess) {
            return null;
        }
        // Create and save each sale item as a new object
        for (SaleItem originalItem : sale.getItems()) {
            SaleItem newItem = new SaleItem();
            newItem.setDescription(originalItem.getDescription());
            newItem.setQuantity(originalItem.getQuantity());
            newItem.setUnitPrice(originalItem.getUnitPrice());
            newItem.setSale(newSale);
            boolean itemSuccess = saleItemDAO.save(newItem);
            if (!itemSuccess) {
                // If any item fails, consider the whole operation failed
                return null;
            }
            // Add to the collection
            newSale.getItems().add(newItem);
        }
        return newSale;
    }

    /**
     * Updates an existing sale with new data
     *
     * @param sale The sale to update
     * @return The updated sale, or null if update failed
     */
    public Sale updateSale(Sale sale) {
        try {
            // First, find the existing sale to get the managed entity
            Sale existingSale = saleDAO.findByIdWithRelationships(sale.getId());
            if (existingSale == null) {
                return null;
            }
            
            // Get existing items before updating
            List<SaleItem> existingItems = new ArrayList<>(existingSale.getItems());
            
            // Update the sale properties
            existingSale.setCustomer(sale.getCustomer());
            existingSale.setUser(sale.getUser());
            existingSale.setPaymentMethod(sale.getPaymentMethod());
            existingSale.setPaymentDate(sale.getPaymentDate());
            existingSale.setSubtotalAmount(sale.getSubtotalAmount());
            existingSale.setDiscountAmount(sale.getDiscountAmount());
            existingSale.setTotalAmount(sale.getTotalAmount());
            
            // Save the updated sale first
            boolean saleUpdateSuccess = saleDAO.update(existingSale);
            if (!saleUpdateSuccess) {
                return null;
            }
            
            // Delete old items
            for (SaleItem item : existingItems) {
                saleItemDAO.delete(item.getId());
            }
            
            // Create and add new items
            for (SaleItem newItem : sale.getItems()) {
                SaleItem item = new SaleItem();
                item.setDescription(newItem.getDescription());
                item.setQuantity(newItem.getQuantity());
                item.setUnitPrice(newItem.getUnitPrice());
                item.setSale(existingSale);
                boolean itemSuccess = saleItemDAO.save(item);
                if (!itemSuccess) {
                    // If any item fails, log the error but continue
                    System.err.println("Failed to save item: " + item.getDescription());
                }
                existingSale.getItems().add(item);
            }
            
            return existingSale;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Cancels a sale by setting its canceled flag to true
     *
     * @param saleId The ID of the sale to cancel
     * @return true if the sale was successfully canceled, false otherwise
     */
    public boolean cancelSale(Integer saleId) {
        Sale sale = saleDAO.findById(saleId);
        if (sale == null) {
            return false;
        }
        sale.setCanceled(true);
        return saleDAO.update(sale);
    }

    /**
     * Finds a sale by its ID
     *
     * @param id The ID of the sale to find
     * @return The sale if found, null otherwise
     */
    public Sale findSaleById(Integer id) {
        return saleDAO.findById(id);
    }

    /**
     * Finds a sale by its ID with all relationships loaded (customer, items,
     * user)
     *
     * @param id The ID of the sale to find
     * @return The sale with relationships if found, null otherwise
     */
    public Sale findSaleByIdWithRelationships(Integer id) {
        return saleDAO.findByIdWithRelationships(id);
    }

    /**
     * Lists all sales
     *
     * @return A list of all sales
     */
    public List<Sale> listAllSales() {
        return saleDAO.findAll();
    }

    /**
     * Lists all sales for a specific customer
     *
     * @param customerId The ID of the customer
     * @return A list of sales for the customer
     */
    public List<Sale> listSalesByCustomer(Integer customerId) {
        return saleDAO.findByCustomerId(customerId);
    }

    /**
     * Lists all sales within a date range
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of sales within the date range
     */
    public List<Sale> listSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleDAO.findByDateRange(startDate,
                endDate);
    }

    /**
     * Validates a sale object before saving or updating
     *
     * @param sale The sale to validate
     * @return true if the sale is valid, false otherwise
     */
    public boolean validateSale(Sale sale) {
        // Check if sale is null
        if (sale == null) {
            return false;
        }
        // Check if customer is set
        if (sale.getCustomer() == null || sale.getCustomer().getId() == null) {
            return false;
        }
        // Check if user is set
        if (sale.getUser() == null || sale.getUser().getId() == null) {
            return false;
        }
        // Check if date is set
        if (sale.getDate() == null) {
            sale.setDate(LocalDateTime.now());
        }
        // Check if payment method is set
        if (sale.getPaymentMethod() == null) {
            return false;
        }
        // Check if there are items
        if (sale.getItems() == null || sale.getItems().isEmpty()) {
            return false;
        }
        // Validate each item
        for (SaleItem item : sale.getItems()) {
            if (item.getDescription() == null || item.getDescription().trim().isEmpty()) {
                return false;
            }
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                return false;
            }
            if (item.getUnitPrice() == null || item.getUnitPrice() < 0) {
                return false;
            }
        }
        // Calculate and set totals if not already set
        double subtotal = 0.0;
        for (SaleItem item : sale.getItems()) {
            subtotal += item.getQuantity() * item.getUnitPrice();
        }
        sale.setSubtotalAmount(subtotal);
        // If discount not set, default to 0
        if (sale.getDiscountAmount() == null) {
            sale.setDiscountAmount(0.0);
        }
        // Calculate total amount
        double totalAmount = subtotal - sale.getDiscountAmount();
        sale.setTotalAmount(totalAmount);
        return true;
    }

    /**
     * Lists sales with optional filters
     *
     * @param filters Map containing filter criteria (customerName, date,
     * paymentMethod)
     * @return A filtered list of sales
     */
    public List<Sale> listSalesWithFilters(Map<String, Object> filters) {
        if (filters == null || filters.isEmpty()) {
            return listAllSales();
        }
        List<Sale> allSales = listAllSales();
        List<Sale> filteredSales = new ArrayList<>(allSales);
        // Filter by customer name if provided
        if (filters.containsKey("customerName") && filters.get("customerName") != null) {
            String customerName = ((String) filters.get("customerName")).toLowerCase();
            if (!customerName.isEmpty()) {
                filteredSales.removeIf(sale -> sale.getCustomer() == null
                        || !sale.getCustomer().getName().toLowerCase().contains(
                                customerName));
            }
        }
        // Filter by date if provided
        if (filters.containsKey("date") && filters.get("date") != null) {
            String dateStr = (String) filters.get("date");
            if (!dateStr.isEmpty()) {
                try {
                    // Parse date string in format dd/MM/yyyy
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "dd/MM/yyyy");
                    Date filterDate = dateFormat.parse(dateStr);
                    LocalDateTime filterDateTime = filterDate.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    // Keep only sales on the specified date
                    filteredSales.removeIf(sale -> {
                        if (sale.getDate() == null) {
                            return true;
                        }
                        LocalDateTime saleDate = sale.getDate();
                        return saleDate.getYear() != filterDateTime.getYear()
                                || saleDate.getMonthValue() != filterDateTime.getMonthValue()
                                || saleDate.getDayOfMonth() != filterDateTime.getDayOfMonth();
                    });
                } catch (ParseException e) {
                    // If date parsing fails, ignore this filter
                    System.err.println(
                            "Error parsing date filter: " + e.getMessage());
                }
            }
        }
        // Filter by payment method if provided
        if (filters.containsKey("paymentMethod") && filters.get("paymentMethod") != null) {
            String paymentMethod = ((String) filters.get("paymentMethod")).toUpperCase();
            if (!paymentMethod.isEmpty()) {
                filteredSales.removeIf(
                        sale -> sale.getPaymentMethod() == null
                        || !sale.getPaymentMethod().toString().contains(
                                paymentMethod));
            }
        }
        return filteredSales;
    }

    /**
     * Gets the customer name for a specific sale
     *
     * @param saleId The ID of the sale
     * @return The customer name or empty string if not found
     */
    public String getCustomerName(Integer saleId) {
        Sale sale = findSaleByIdWithRelationships(saleId);
        if (sale != null && sale.getCustomer() != null) {
            return sale.getCustomer().getName();
        }
        return "";
    }

    /**
     * Gets the sale date for a specific sale
     *
     * @param saleId The ID of the sale
     * @return The sale date or null if not found
     */
    public LocalDateTime getSaleDate(Integer saleId) {
        Sale sale = findSaleById(saleId);
        if (sale != null) {
            return sale.getDate();
        }
        return null;
    }

    /**
     * Gets the payment method for a specific sale in a safe way
     *
     * @param saleId The ID of the sale
     * @return The payment method as string or empty string if not found
     */
    public String getPaymentMethodSafe(Integer saleId) {
        Sale sale = findSaleById(saleId);
        if (sale != null && sale.getPaymentMethod() != null) {
            return sale.getPaymentMethod().toString();
        }
        return "";
    }

    /**
     * Gets the payment date for a specific sale
     *
     * @param saleId The ID of the sale
     * @return The payment date or null if not found
     */
    public LocalDateTime getPaymentDate(Integer saleId) {
        Sale sale = findSaleById(saleId);
        if (sale != null) {
            return sale.getPaymentDate();
        }
        return null;
    }

    /**
     * Gets the total amount for a specific sale
     *
     * @param saleId The ID of the sale
     * @return The total amount or 0.0 if not found
     */
    public Double getTotalAmount(Integer saleId) {
        Sale sale = findSaleById(saleId);
        if (sale != null) {
            return sale.getTotalAmount();
        }
        return 0.0;
    }

    /**
     * Deletes a sale by its ID
     *
     * @param saleId The ID of the sale to delete
     * @return true if the sale was successfully deleted, false otherwise
     */
    public boolean deleteSale(Integer saleId) {
        // Use findSaleByIdWithRelationships to ensure items are loaded
        Sale sale = findSaleByIdWithRelationships(saleId);
        if (sale == null) {
            return false;
        }
        // Delete all related items first
        for (SaleItem item : new ArrayList<>(sale.getItems())) {
            saleItemDAO.delete(item.getId());
        }
        // Then delete the sale
        return saleDAO.delete(saleId);
    }

    /**
     * Prepares a new Sale object with the provided data
     *
     * @param customer The customer for the sale
     * @param paymentMethod The payment method
     * @param paymentDate The payment date
     * @param items The list of sale items
     * @param subtotal The subtotal amount
     * @param discount The discount amount
     * @param user The user creating the sale
     * @return A prepared Sale object
     */
    public Sale prepareSaleObject(Customer customer, PaymentMethod paymentMethod,
            LocalDateTime paymentDate, List<SaleItem> items, double subtotal,
            double discount, User user) {
        
        // Create a new sale
        Sale sale = new Sale();
        
        // Set the customer
        sale.setCustomer(customer);
        
        // Set the payment method
        sale.setPaymentMethod(paymentMethod);
        
        // Set the payment date
        sale.setPaymentDate(paymentDate);
        
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
     * Prepares a Sale object for update with the provided data
     * 
     * @param saleId The ID of the existing sale
     * @param createdAt The original creation date
     * @param saleDate The original sale date
     * @param customer The customer for the sale
     * @param paymentMethod The payment method
     * @param paymentDate The payment date
     * @param items The list of sale items
     * @param subtotal The subtotal amount
     * @param discount The discount amount
     * @param user The user associated with the sale
     * @return A prepared Sale object ready for update
     */
    public Sale prepareSaleForUpdate(
            Integer saleId,
            LocalDateTime createdAt,
            LocalDateTime saleDate,
            Customer customer,
            PaymentMethod paymentMethod,
            LocalDateTime paymentDate,
            List<SaleItem> items,
            double subtotal,
            double discount,
            User user) {
        
        // Create a new Sale object with the same ID
        Sale sale = new Sale();
        sale.setId(saleId);
        sale.setDate(saleDate);
        
        // Set the updated values
        sale.setCustomer(customer);
        sale.setUser(user);
        sale.setPaymentMethod(paymentMethod);
        sale.setPaymentDate(paymentDate);
        
        // Set amounts
        sale.setSubtotalAmount(subtotal);
        sale.setDiscountAmount(discount);
        sale.setTotalAmount(subtotal - discount);
        
        // Create new items and associate them with this sale
        List<SaleItem> newItems = new ArrayList<>();
        for (SaleItem item : items) {
            SaleItem newItem = new SaleItem();
            newItem.setDescription(item.getDescription());
            newItem.setQuantity(item.getQuantity());
            newItem.setUnitPrice(item.getUnitPrice());
            newItem.setSale(sale);
            newItems.add(newItem);
        }
        sale.setItems(newItems);
        
        return sale;
    }
}
