package br.com.devjf.salessync.dto;

/**
 * Data Transfer Object for Sale Item data from the UI
 */
public class SaleItemDTO {
    private final String description;
    private final int quantity;
    private final Double price;
    
    public SaleItemDTO(String description, int quantity, Double price) {
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public Double getPrice() {
        return price;
    }
}