package br.com.devjf.salessync.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "service_orders")
public class ServiceOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    // Adicionar relação com Sale
    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
    
    @Column(name = "request_date", nullable = false)
    private LocalDate requestDate;
    
    @Column(name = "estimated_delivery_date")
    private LocalDate estimatedDeliveryDate;
    
    @Column
    private Double amount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status_id", nullable = false)
    private ServiceStatus status;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.requestDate == null) {
            this.requestDate = LocalDate.now();
        }
        if (this.status == null) {
            this.status = ServiceStatus.PENDING;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public ServiceOrder() {
    }
    
    public ServiceOrder(Customer customer, String description, LocalDate estimatedDeliveryDate, Double amount) {
        this.customer = customer;
        this.description = description;
        this.requestDate = LocalDate.now();
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.amount = amount;
        this.status = ServiceStatus.PENDING;
    }
    
    // Methods
    public void updateStatus(ServiceStatus newStatus) {
        this.status = newStatus;
    }
    
    public Integer calculateDelay() {
        if (estimatedDeliveryDate == null || status == ServiceStatus.COMPLETED) {
            return 0;
        }
        LocalDate compareDate = status == ServiceStatus.COMPLETED ? updatedAt.toLocalDate() : LocalDate.now();
        return (int) ChronoUnit.DAYS.between(estimatedDeliveryDate, compareDate);
    }
    
    public void print() {
        // Implementação da impressão será feita na camada de serviço
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public LocalDate getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(LocalDate estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Sale getSale() {
        return sale;
    }
    
    public void setSale(Sale sale) {
        this.sale = sale;
    }
}