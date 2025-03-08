package br.com.devjf.salessync.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.devjf.salessync.model.SystemLog;
import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.service.LogService;

public class LogController {
    private final LogService logService;
    
    public LogController() {
        this.logService = new LogService();
    }
    
    public void recordLog(User user, String action, String details) {
        logService.recordLog(user, action, details);
    }
    
    public List<SystemLog> listLogs(Map<String, Object> filters) {
        return logService.listLogs(filters);
    }
    
    public List<SystemLog> listLogsByUser(User user) {
        Map<String, Object> filters = new HashMap<>();
        filters.put("user", user);
        return logService.listLogs(filters);
    }
    
    public List<SystemLog> listLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> filters = new HashMap<>();
        filters.put("startDate", startDate);
        filters.put("endDate", endDate);
        return logService.listLogs(filters);
    }
    
    public List<SystemLog> listLogsByAction(String action) {
        Map<String, Object> filters = new HashMap<>();
        filters.put("action", action);
        return logService.listLogs(filters);
    }
    
    public boolean clearOldLogs(String period) {
        return logService.clearOldLogs(period);
    }
}