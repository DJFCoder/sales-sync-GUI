package br.com.devjf.salessync.service;

import br.com.devjf.salessync.dao.SystemLogDAO;
import br.com.devjf.salessync.model.SystemLog;
import br.com.devjf.salessync.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogService {
    
    private final SystemLogDAO logDAO;
    
    public LogService() {
        this.logDAO = new SystemLogDAO();
    }
    
    public void recordLog(User user, String action, String details) {
        SystemLog log = new SystemLog();
        log.setUser(user);
        log.setDateTime(LocalDateTime.now());
        log.setAction(action);
        log.setDetails(details);
        
        logDAO.save(log);
    }
    
    public List<SystemLog> listLogs(Map<String, Object> filters) {
        if (filters == null || filters.isEmpty()) {
            return logDAO.findAll();
        }
        
        // Handle different filter types
        if (filters.containsKey("userId")) {
            User user = (User) filters.get("user");
            return logDAO.findByUser(user);
        } else if (filters.containsKey("action")) {
            String action = (String) filters.get("action");
            return logDAO.findByAction(action);
        } else if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
            LocalDateTime startDate = (LocalDateTime) filters.get("startDate");
            LocalDateTime endDate = (LocalDateTime) filters.get("endDate");
            return logDAO.findByDateRange(startDate, endDate);
        }
        
        return logDAO.findAll();
    }
    
    public boolean clearOldLogs(String period) {
        LocalDateTime cutoffDate;
        LocalDateTime now = LocalDateTime.now();
        
        // Determine cutoff date based on period
        switch (period.toLowerCase()) {
            case "month":
                cutoffDate = now.minusMonths(1);
                break;
            case "quarter":
                cutoffDate = now.minusMonths(3);
                break;
            case "year":
                cutoffDate = now.minusYears(1);
                break;
            default:
                cutoffDate = now.minusMonths(6); // Default to 6 months
        }
        
        // Get logs older than cutoff date
        List<SystemLog> oldLogs = logDAO.findByDateRange(LocalDateTime.MIN, cutoffDate);
        
        // Delete each old log
        boolean success = true;
        for (SystemLog log : oldLogs) {
            if (!logDAO.delete(log.getId())) {
                success = false;
            }
        }
        
        return success;
    }
    
    public Map<String, Object> generateActivityReport(Integer userId, String period) {
        Map<String, Object> result = new HashMap<>();
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate;
        
        // Determine date range based on period
        switch (period.toLowerCase()) {
            case "day":
                startDate = now.truncatedTo(ChronoUnit.DAYS);
                break;
            case "week":
                startDate = now.minusWeeks(1);
                break;
            case "month":
                startDate = now.minusMonths(1);
                break;
            default:
                startDate = now.minusWeeks(1); // Default to 1 week
        }
        
        // Get user logs for the period
        User user = new User();
        user.setId(userId);
        List<SystemLog> userLogs = logDAO.findByUser(user);
        
        // Filter logs by date
        List<SystemLog> periodLogs = userLogs.stream()
                .filter(log -> !log.getDateTime().isBefore(startDate))
                .toList();
        
        // Count actions by type
        Map<String, Integer> actionCounts = new HashMap<>();
        for (SystemLog log : periodLogs) {
            String action = log.getAction();
            actionCounts.put(action, actionCounts.getOrDefault(action, 0) + 1);
        }
        
        // Calculate activity metrics
        int totalActions = periodLogs.size();
        double actionsPerDay = totalActions / (double) ChronoUnit.DAYS.between(startDate, now);
        
        // Find most common action
        String mostCommonAction = actionCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No actions");
        
        // Populate result
        result.put("userId", userId);
        result.put("period", period);
        result.put("startDate", startDate);
        result.put("endDate", now);
        result.put("totalActions", totalActions);
        result.put("actionsPerDay", actionsPerDay);
        result.put("actionCounts", actionCounts);
        result.put("mostCommonAction", mostCommonAction);
        result.put("logs", periodLogs);
        
        return result;
    }
}