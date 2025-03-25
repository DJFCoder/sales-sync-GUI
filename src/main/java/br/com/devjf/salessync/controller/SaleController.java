package br.com.devjf.salessync.controller;

import java.util.List;
import java.util.Map;
import br.com.devjf.salessync.model.Sale;
import br.com.devjf.salessync.model.SaleItem;
import br.com.devjf.salessync.service.SaleService;
import br.com.devjf.salessync.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class SaleController {
    private final SaleService saleService;

    public SaleController() {
        this.saleService = new SaleService();
    }

    public boolean createSale(Sale sale) {
        // Validar a venda antes de registrar
        if (saleService.validateSale(sale)) {
            // Registrar a venda com seus itens em uma única transação
            return saleService.registerSale(sale);
        }
        return false;
    }

    public boolean registerSale(Sale sale) {
        return saleService.registerSale(sale);
    }

    public boolean updateSale(Sale sale) {
        return saleService.updateSale(sale);
    }

    public boolean deleteSale(Integer id) {
        // Instead of using setDeleted, we should use the cancelSale method from the service
        // which internally sets the canceled flag to true
        return saleService.cancelSale(id);
    }

    /**
     * Busca uma venda pelo ID, inicializando a coleção de itens.
     *
     * @param id O ID da venda a ser buscada
     * @return A venda encontrada ou null se não existir
     */
    public Sale findSaleById(Integer id) {
        return saleService.findSaleByIdWithRelationships(id);
    }

    public List<Sale> listSales(Map<String, Object> filters) {
        // Using the correct method from SaleService
        return saleService.listSales(filters);
    }

    public List<SaleItem> getSaleItems(Integer saleId) {
        Sale sale = findSaleById(saleId);
        if (sale == null) {
            return List.of();
        }
        return sale.getItems();
    }

    public boolean addItemToSale(Integer saleId, SaleItem item) {
        Sale sale = findSaleById(saleId);
        if (sale == null) {
            return false;
        }
        sale.addItem(item);
        return saleService.updateSale(sale);
    }

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

    public boolean applySaleDiscount(Integer saleId, double discountAmount) {
        Sale sale = findSaleById(saleId);
        if (sale == null) {
            return false;
        }
        // Use the applyDiscounts method from SaleService instead
        Sale updatedSale = saleService.applyDiscounts(sale,
                discountAmount);
        // Update the sale if discount was applied
        if (updatedSale != null) {
            return saleService.updateSale(updatedSale);
        }
        return false;
    }
}
