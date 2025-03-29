package br.com.devjf.salessync.dto;

/**
 * Data Transfer Object for Sale Item data from the UI
 */
public class SaleItemDTO {
    private final String description;
    private final Object quantity;
    private final Object price;
    
    public SaleItemDTO(String description, Object quantity, Object price) {
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Object getQuantity() {
        return quantity;
    }
    
    public Object getPrice() {
        return price;
    }
}