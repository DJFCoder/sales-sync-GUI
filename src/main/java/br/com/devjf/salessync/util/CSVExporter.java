package br.com.devjf.salessync.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class CSVExporter {
    
    public boolean exportToCSV(Map<String, Object> data, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Escreve o cabeçalho
            writer.append("Chave,Valor\n");
            
            // Escreve os dados
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                writer.append(entry.getKey())
                      .append(",")
                      .append(String.valueOf(entry.getValue()))
                      .append("\n");
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao exportar para CSV: " + e.getMessage());
            return false;
        }
    }
}