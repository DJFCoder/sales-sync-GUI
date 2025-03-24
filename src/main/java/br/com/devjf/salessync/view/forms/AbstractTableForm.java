package br.com.devjf.salessync.view.forms;

import br.com.devjf.salessync.util.TableUtil;
import java.time.LocalDateTime;
import java.util.function.Consumer;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Classe abstrata que serve como base para formulários que contêm tabelas.
 * Fornece métodos comuns para manipulação de tabelas e implementa um padrão
 * consistente para todos os formulários.
 */
public abstract class AbstractTableForm extends javax.swing.JFrame {
    
    /**
     * Configura o modelo de tabela com as colunas especificadas.
     * 
     * @param table A tabela a ser configurada
     * @param columnNames Os nomes das colunas
     * @param columnClasses As classes das colunas (tipos de dados)
     * @param editableColumns Array de booleanos indicando quais colunas são editáveis
     * @return O modelo de tabela configurado
     */
    protected DefaultTableModel setupTable(JTable table, String[] columnNames, 
            Class<?>[] columnClasses, boolean[] editableColumns) {
        return TableUtil.setupTableModel(table, columnNames, columnClasses, editableColumns);
    }
    
    /**
     * Limpa todos os dados da tabela.
     * 
     * @param model O modelo de tabela a ser limpo
     */
    protected void clearTable(DefaultTableModel model) {
        TableUtil.clearTable(model);
    }
    
    /**
     * Ajusta automaticamente a largura das colunas com base no conteúdo.
     * 
     * @param table A tabela a ser ajustada
     * @param margin Margem adicional para cada coluna (em pixels)
     */
    protected void adjustColumnWidths(JTable table, int margin) {
        TableUtil.adjustColumnWidths(table, margin);
    }
    
    /**
     * Define a altura das linhas da tabela.
     * 
     * @param table A tabela a ser configurada
     * @param rowHeight A altura das linhas em pixels
     */
    protected void setRowHeight(JTable table, int rowHeight) {
        TableUtil.setRowHeight(table, rowHeight);
    }
    
    /**
     * Adiciona uma linha à tabela.
     * 
     * @param model O modelo de tabela
     * @param rowData Os dados da linha a ser adicionada
     */
    protected void addRow(DefaultTableModel model, Object[] rowData) {
        TableUtil.addRow(model, rowData);
    }
    
    /**
     * Configura a largura específica para uma coluna.
     * 
     * @param table A tabela a ser configurada
     * @param columnIndex O índice da coluna
     * @param width A largura desejada em pixels
     */
    protected void setColumnWidth(JTable table, int columnIndex, int width) {
        TableUtil.setColumnWidth(table, columnIndex, width);
    }
    
    /**
     * Obtém o valor de uma célula específica da tabela.
     * 
     * @param table A tabela
     * @param row O índice da linha
     * @param column O índice da coluna
     * @return O valor da célula
     */
    protected Object getCellValue(JTable table, int row, int column) {
        return TableUtil.getCellValue(table, row, column);
    }
    
    /**
     * Obtém o índice da linha selecionada na tabela.
     * 
     * @param table A tabela
     * @return O índice da linha selecionada ou -1 se nenhuma linha estiver selecionada
     */
    protected int getSelectedRowIndex(JTable table) {
        return TableUtil.getSelectedRowIndex(table);
    }
    
    /**
     * Verifica se há alguma linha selecionada na tabela.
     * 
     * @param table A tabela
     * @return true se houver uma linha selecionada, false caso contrário
     */
    protected boolean hasSelectedRow(JTable table) {
        return TableUtil.hasSelectedRow(table);
    }
    
    /**
     * Configura a filtragem dinâmica da tabela com base em campos de texto.
     * 
     * @param <T> Tipo do modelo de tabela
     * @param table A tabela a ser configurada
     * @param model O modelo da tabela
     * @param textFields Array de campos de texto para filtrar
     * @param columnIndexes Array de índices das colunas correspondentes aos campos de texto
     * @return O TableRowSorter configurado
     */
    protected <T extends DefaultTableModel> TableRowSorter<T> setupTableFilter(
            JTable table, T model, JTextField[] textFields, int[] columnIndexes) {
        return TableUtil.setupTableFilter(table, model, textFields, columnIndexes);
    }
    
    /**
     * Adiciona um listener de clique duplo à tabela.
     * 
     * @param table A tabela
     * @param action A ação a ser executada quando ocorrer um clique duplo em uma linha
     */
    protected void addDoubleClickListener(JTable table, Consumer<Integer> action) {
        TableUtil.addDoubleClickListener(table, action);
    }
    
    /**
     * Formata uma data e hora para exibição na tabela.
     * 
     * @param dateTime A data e hora a ser formatada
     * @return A string formatada ou uma string vazia se a data for nula
     */
    protected String formatDateTime(LocalDateTime dateTime) {
        return TableUtil.formatDateTime(dateTime);
    }
    
    /**
     * Formata uma data para exibição na tabela (sem a hora).
     * 
     * @param dateTime A data a ser formatada
     * @return A string formatada ou uma string vazia se a data for nula
     */
    protected String formatDate(LocalDateTime dateTime) {
        return TableUtil.formatDate(dateTime);
    }
    
    /**
     * Formata um valor monetário para exibição na tabela.
     * 
     * @param value O valor a ser formatado
     * @return A string formatada ou uma string vazia se o valor for nulo
     */
    protected String formatCurrency(Double value) {
        return TableUtil.formatCurrency(value);
    }
    
    /**
     * Método abstrato que deve ser implementado pelas classes filhas para
     * carregar os dados na tabela.
     */
    protected abstract void loadTableData();
    
    /**
     * Método abstrato que deve ser implementado pelas classes filhas para
     * configurar a tabela (colunas, tipos, etc).
     */
    protected abstract void setupTableColumns();
    
    /**
     * Método abstrato que deve ser implementado pelas classes filhas para
     * tratar a seleção de uma linha na tabela.
     * 
     * @param selectedRow O índice da linha selecionada
     */
    protected abstract void handleRowSelection(int selectedRow);
}
