package br.com.devjf.salessync.model;

public enum ServiceStatus {
    PENDING("PENDENTE"),
    IN_PROGRESS("EM ANDAMENTO"),
    COMPLETED("FINALIZADA"),
    CANCELED("CANCELADA");
    
    private final String displayName;
    
    ServiceStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Obtém o enum ServiceStatus a partir do nome de exibição em português
     * @param displayName Nome em português
     * @return O enum correspondente ou null se não encontrado
     */
    public static ServiceStatus fromDisplayName(String displayName) {
        for (ServiceStatus status : ServiceStatus.values()) {
            if (status.getDisplayName().equals(displayName)) {
                return status;
            }
        }
        return null;
    }
}