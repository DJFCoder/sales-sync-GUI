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
    @ManyToOne
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;
    @Column(name = "completion_date")
    private LocalDate completionDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ServiceStatus status;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.creationDate == null) {
            this.creationDate = LocalDate.now();
        }
        if (this.status == null) {
            this.status = ServiceStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        // Set completion date when status changes to COMPLETED
        if (this.status == ServiceStatus.COMPLETED && this.completionDate == null) {
            this.completionDate = LocalDate.now();
        }
    }

    // Constructors
    public ServiceOrder() {
    }

    public ServiceOrder(Customer customer, Sale sale, String description, LocalDate estimatedDeliveryDate) {
        this.customer = customer;
        this.sale = sale;
        this.description = description;
        this.creationDate = LocalDate.now();
        this.status = ServiceStatus.PENDING;
    }

    // Methods
    public void updateStatus(ServiceStatus newStatus) {
        ServiceStatus oldStatus = this.status;
        this.status = newStatus;
        // Set completion date when status changes to COMPLETED
        if (newStatus == ServiceStatus.COMPLETED && oldStatus != ServiceStatus.COMPLETED) {
            this.completionDate = LocalDate.now();
        }
    }

    public Integer calculateDelay() {
        if (this.completionDate == null || status != ServiceStatus.COMPLETED) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(this.creationDate,
                this.completionDate);
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

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        // Use the updateStatus method to ensure completion date is set properly
        updateStatus(status);
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
}
