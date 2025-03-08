package br.com.devjf.salessync.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.devjf.salessync.dao.ServiceOrderDAO;
import br.com.devjf.salessync.model.Customer;
import br.com.devjf.salessync.model.ServiceOrder;
import br.com.devjf.salessync.model.ServiceStatus;

public class ServiceOrderService {
    
    private final ServiceOrderDAO serviceOrderDAO;
    
    public ServiceOrderService() {
        this.serviceOrderDAO = new ServiceOrderDAO();
    }
    
    public boolean createServiceOrder(ServiceOrder order) {
        // Set default status if not specified
        if (order.getStatus() == null) {
            order.setStatus(ServiceStatus.PENDING);
        }
        
        return serviceOrderDAO.save(order);
    }
    
    public boolean updateStatus(Integer id, ServiceStatus status) {
        ServiceOrder order = serviceOrderDAO.findById(id);
        if (order == null) {
            return false;
        }
        
        order.setStatus(status);
        return serviceOrderDAO.update(order);
    }
    
    public double calculateExecutionTime(ServiceOrder order) {
        if (order.getStatus() != ServiceStatus.COMPLETED) {
            return -1; // Not completed yet
        }
        
        LocalDate requestDate = order.getRequestDate();
        LocalDate completionDate = LocalDate.now(); // In a real app, this would be stored in the order
        
        return ChronoUnit.DAYS.between(requestDate, completionDate);
    }
    
    public List<ServiceOrder> listServiceOrders(Map<String, Object> filters) {
        if (filters == null || filters.isEmpty()) {
            return serviceOrderDAO.findAll();
        }
        
        // Handle different filter types
        if (filters.containsKey("customerId")) {
            Customer customer = (Customer) filters.get("customer");
            return serviceOrderDAO.findByCustomer(customer);
        } else if (filters.containsKey("status")) {
            ServiceStatus status = (ServiceStatus) filters.get("status");
            return serviceOrderDAO.findByStatus(status);
        } else if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
            LocalDate startDate = (LocalDate) filters.get("startDate");
            LocalDate endDate = (LocalDate) filters.get("endDate");
            return serviceOrderDAO.findByDateRange(startDate, endDate);
        }
        
        return serviceOrderDAO.findAll();
    }
    
    public List<ServiceOrder> checkDelays() {
        List<ServiceOrder> allOrders = serviceOrderDAO.findAll();
        List<ServiceOrder> delayedOrders = new ArrayList<>();
        
        LocalDate today = LocalDate.now();
        
        for (ServiceOrder order : allOrders) {
            // Skip completed or canceled orders
            if (order.getStatus() == ServiceStatus.COMPLETED || 
                order.getStatus() == ServiceStatus.CANCELED) {
                continue;
            }
            
            // Check if estimated delivery date is past
            if (order.getEstimatedDeliveryDate() != null && 
                order.getEstimatedDeliveryDate().isBefore(today)) {
                delayedOrders.add(order);
            }
        }
        
        return delayedOrders;
    }
    
    public boolean notifyCustomer(Integer orderId, String message) {
        // This would integrate with an email or SMS service
        // For now, just checking if the order exists
        ServiceOrder order = serviceOrderDAO.findById(orderId);
        if (order == null) {
            return false;
        }
        
        // Simulate sending notification
        System.out.println("Notification sent to customer: " + order.getCustomer().getName());
        System.out.println("Message: " + message);
        
        return true;
    }
    
    public boolean generateInvoice(Integer orderId) {
        // This would generate a PDF invoice
        // For now, just checking if the order exists and is completed
        ServiceOrder order = serviceOrderDAO.findById(orderId);
        if (order == null || order.getStatus() != ServiceStatus.COMPLETED) {
            return false;
        }
        
        // Simulate generating invoice
        System.out.println("Invoice generated for order #" + orderId);
        
        return true;
    }
}