package br.com.devjf.salessync.controller;

import java.util.List;
import java.util.Map;
import br.com.devjf.salessync.model.Sale;
import br.com.devjf.salessync.model.SaleItem;
import br.com.devjf.salessync.service.SaleService;

/**
 * Controller class for managing sale-related operations.
 * Provides methods for creating, updating, deleting, and retrieving sales and sale items.
 */
public class SaleController {
    private final SaleService saleService;

    /**
     * Constructs a new SaleController with a SaleService instance.
     */
    public SaleController() {
        this.saleService = new SaleService();
    }

    /**
     * Creates a new sale after validating it.
     *
     * @param sale The sale object to create
     * @return true if the sale was successfully created, false otherwise
     */
    public boolean createSale(Sale sale) {
        // Validar a venda antes de registrar
        if (saleService.validateSale(sale)) {
            // Registrar a venda com seus itens em uma única transação
            return saleService.registerSale(sale);
        }
        return false;
    }

    /**
     * Registers a new sale in the system without validation.
     *
     * @param sale The sale object to register
     * @return true if the sale was successfully registered, false otherwise
     */
    public boolean registerSale(Sale sale) {
        return saleService.registerSale(sale);
    }

    /**
     * Updates an existing sale's information.
     *
     * @param sale The sale object with updated information
     * @return true if the sale was successfully updated, false otherwise
     */
    public boolean updateSale(Sale sale) {
        return saleService.updateSale(sale);
    }

    /**
     * Deletes (cancels) a sale by ID.
     *
     * @param id The ID of the sale to delete/cancel
     * @return true if the sale was successfully canceled, false otherwise
     */
    public boolean deleteSale(Integer id) {
        // Instead of using setDeleted, we should use the cancelSale method from the service
        // which internally sets the canceled flag to true
        return saleService.cancelSale(id);
    }

    /**
     * Finds a sale by ID, initializing the collection of items.
     *
     * @param id The ID of the sale to find
     * @return The sale object with its relationships if found, null otherwise
     */
    public Sale findSaleById(Integer id) {
        return saleService.findSaleByIdWithRelationships(id);
    }

    /**
     * Lists sales based on specified filters.
     *
     * @param filters Map of filter criteria to apply
     * @return A list of sales matching the filter criteria
     */
    public List<Sale> listSales(Map<String, Object> filters) {
        // Using the correct method from SaleService
        return saleService.listSales(filters);
    }

    /**
     * Gets the items for a specific sale.
     *
     * @param saleId The ID of the sale to get items for
     * @return A list of sale items for the specified sale
     */
    public List<SaleItem> getSaleItems(Integer saleId) {
        Sale sale = findSaleById(saleId);
        if (sale == null) {
            return List.of();
        }
        return sale.getItems();
    }

    /**
     * Adds an item to an existing sale.
     *
     * @param saleId The ID of the sale to add the item to
     * @param item The sale item to add
     * @return true if the item was successfully added, false otherwise
     */
    public boolean addItemToSale(Integer saleId, SaleItem item) {
        Sale sale = findSaleById(saleId);
        if (sale == null) {
            return false;
        }
        sale.addItem(item);
        return saleService.updateSale(sale);
    }

    /**
     * Removes an item from an existing sale.
     *
     * @param saleId The ID of the sale to remove the item from
     * @param itemId The ID of the item to remove
     * @return true if the item was successfully removed, false otherwise
     */
    public boolean removeItemFromSale(Integer saleId, Integer itemId) {
        Sale sale = findSaleById(saleId);
        if (sale == null) {
            return false;
        }
        // Find the item with the given ID first
        SaleItem itemToRemove = null;
        for (SaleItem item : sale.getItems()) {
            if (item.getId().equals(itemId)) {
                itemToRemove = item;
                break;
            }
        }
        if (itemToRemove != null) {
            sale.getItems().remove(itemToRemove);
            return saleService.updateSale(sale);
        }
        return false;
    }

    /**
     * Applies a discount to an existing sale.
     *
     * @param saleId The ID of the sale to apply the discount to
     * @param discountAmount The amount of discount to apply
     * @return true if the discount was successfully applied, false otherwise
     */
    public boolean applySaleDiscount(Integer saleId, double discountAmount) {
        Sale sale = findSaleById(saleId);
        if (sale == null) {
            return false;
        }
        // Use the applyDiscounts method from SaleService instead
        Sale updatedSale = saleService.applyDiscounts(sale, discountAmount);
        // Update the sale if discount was applied
        if (updatedSale != null) {
            return saleService.updateSale(updatedSale);
        }
        return false;
    }
}
