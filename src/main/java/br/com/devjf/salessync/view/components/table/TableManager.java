package br.com.devjf.salessync.view.components.table;

import java.awt.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

/**
 * Classe utilitária para auxiliar na implementação e manipulação de tabelas no
 * sistema. Fornece métodos para configuração, carregamento e manipulação de
 * dados em tabelas.
 */
public class TableManager {
    // Formatador de data e hora padrão para exibição em tabelas
    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(
            "dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DEFAULT_DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern(
            "dd/MM/yyyy");

    /**
     * Configura o modelo de tabela com as colunas especificadas.
     *
     * @param table A tabela a ser configurada
     * @param columnNames Os nomes das colunas
     * @param columnClasses As classes das colunas (tipos de dados)
     * @param editableColumns Array de booleanos indicando quais colunas são
     * editáveis
     * @return O modelo de tabela configurado
     */
    public static DefaultTableModel setupTableModel(JTable table, String[] columnNames,
            Class<?>[] columnClasses, boolean[] editableColumns) {
        DefaultTableModel model = new DefaultTableModel(null,
                columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnClasses[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return editableColumns != null && editableColumns[columnIndex];
            }
        };
        table.setModel(model);
        return model;
    }

    /**
     * Configura um TableRowSorter para a tabela e adiciona filtros dinâmicos
     * baseados nos campos de texto.
     *
     * @param <T> Tipo do modelo de tabela
     * @param table A tabela a ser configurada
     * @param model O modelo da tabela
     * @param textFields Array de campos de texto para filtrar
     * @param columnIndexes Array de índices das colunas correspondentes aos
     * campos de texto
     * @return O TableRowSorter configurado
     */
    public static <T extends DefaultTableModel> TableRowSorter<T> setupTableFilter(
            JTable table, T model, JTextField[] textFields, int[] columnIndexes) {
        if (textFields.length != columnIndexes.length) {
            throw new IllegalArgumentException(
                    "O número de campos de texto deve ser igual ao número de índices de coluna");
        }
        TableRowSorter<T> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        // Adicionar listeners para cada campo de texto
        for (JTextField textField : textFields) {
            textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateFilter();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateFilter();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateFilter();
                }

                private void updateFilter() {
                    applyFilters(sorter,
                            textFields,
                            columnIndexes);
                }
            });
        }
        return sorter;
    }

    /**
     * Aplica os filtros aos campos de texto.
     *
     * @param <T> Tipo do modelo de tabela
     * @param sorter O TableRowSorter da tabela
     * @param textFields Array de campos de texto para filtrar
     * @param columnIndexes Array de índices das colunas correspondentes aos
     * campos de texto
     */
    private static <T extends DefaultTableModel> void applyFilters(
            TableRowSorter<T> sorter, JTextField[] textFields, int[] columnIndexes) {
        // Remover filtros anteriores
        sorter.setRowFilter(null);
        // Criar filtros para cada campo de texto não vazio
        List<RowFilter<Object, Object>> filters = new java.util.ArrayList<>();
        for (int i = 0; i < textFields.length; i++) {
            String text = textFields[i].getText().trim();
            if (!text.isEmpty()) {
                // Criar um filtro case-insensitive para a coluna
                RowFilter<Object, Object> filter = RowFilter.regexFilter(
                        "(?i)" + text,
                        columnIndexes[i]);
                filters.add(filter);
            }
        }
        // Aplicar filtros combinados (AND)
        if (!filters.isEmpty()) {
            RowFilter<Object, Object> combinedFilter = RowFilter.andFilter(
                    filters);
            sorter.setRowFilter(combinedFilter);
        }
    }

    /**
     * Formata uma data e hora para exibição na tabela.
     *
     * @param dateTime A data e hora a ser formatada
     * @return A string formatada ou uma string vazia se a data for nula
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DEFAULT_DATE_FORMATTER) : "";
    }

    /**
     * Formata uma data para exibição na tabela (sem a hora).
     *
     * @param dateTime A data a ser formatada
     * @return A string formatada ou uma string vazia se a data for nula
     */
    public static String formatDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DEFAULT_DATE_ONLY_FORMATTER) : "";
    }

    /**
     * Formata um valor monetário para exibição na tabela.
     *
     * @param value O valor a ser formatado
     * @return A string formatada ou uma string vazia se o valor for nulo
     */
    public static String formatCurrency(Double value) {
        return value != null ? String.format("R$ %.2f",
                value) : "";
    }

    /**
     * Limpa todos os dados da tabela.
     *
     * @param model O modelo de tabela a ser limpo
     */
    public static void clearTable(DefaultTableModel model) {
        model.setRowCount(0);
    }

    /**
     * Ajusta automaticamente a largura das colunas com base no conteúdo.
     *
     * @param table A tabela a ser ajustada
     * @param margin Margem adicional para cada coluna (em pixels)
     */
    public static void adjustColumnWidths(JTable table, int margin) {
        TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            // Get width of column header
            TableColumn tableColumn = columnModel.getColumn(column);
            Component comp = table.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(table,
                            tableColumn.getHeaderValue(),
                            false,
                            false,
                            0,
                            column);
            width = Math.max(width,
                    comp.getPreferredSize().width);
            // Get max width of cells in column
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row,
                        column);
                Component c = renderer.getTableCellRendererComponent(
                        table,
                        table.getValueAt(row,
                                column),
                        false,
                        false,
                        row,
                        column);
                width = Math.max(width,
                        c.getPreferredSize().width);
            }
            // Add margin
            width += 2 * margin;
            // Set the width
            tableColumn.setPreferredWidth(width);
        }
    }

    /**
     * Define a altura das linhas da tabela.
     *
     * @param table A tabela a ser configurada
     * @param rowHeight A altura das linhas em pixels
     */
    public static void setRowHeight(JTable table, int rowHeight) {
        table.setRowHeight(rowHeight);
    }

    /**
     * Adiciona uma linha à tabela.
     *
     * @param model O modelo de tabela
     * @param rowData Os dados da linha a ser adicionada
     */
    public static void addRow(DefaultTableModel model, Object[] rowData) {
        model.addRow(rowData);
    }

    /**
     * Configura a largura específica para uma coluna.
     *
     * @param table A tabela a ser configurada
     * @param columnIndex O índice da coluna
     * @param width A largura desejada em pixels
     */
    public static void setColumnWidth(JTable table, int columnIndex, int width) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        column.setPreferredWidth(width);
        column.setMaxWidth(width);
        column.setMinWidth(width);
    }

    /**
     * Obtém o valor de uma célula específica da tabela.
     *
     * @param table A tabela
     * @param row O índice da linha
     * @param column O índice da coluna
     * @return O valor da célula
     */
    public static Object getCellValue(JTable table, int row, int column) {
        return table.getValueAt(row,
                column);
    }

    /**
     * Obtém o índice da linha selecionada na tabela.
     *
     * @param table A tabela
     * @return O índice da linha selecionada ou -1 se nenhuma linha estiver
     * selecionada
     */
    public static int getSelectedRowIndex(JTable table) {
        return table.getSelectedRow();
    }

    /**
     * Verifica se há alguma linha selecionada na tabela.
     *
     * @param table A tabela
     * @return true se houver uma linha selecionada, false caso contrário
     */
    public static boolean hasSelectedRow(JTable table) {
        return table.getSelectedRow() != -1;
    }

    /**
     * Configura os filtros da tabela de vendas.
     *
     * @param field1 first JTextfield name
     * @param field2 second JTextfield name
     * @param field3 third JTextfield name
     * @param table table name
     */
    public static void setupFilters(JTextField field1, JTextField field2, JTextField field3, JTable table) {
        // Configurar os campos de texto para filtragem
        JTextField[] filterFields = new JTextField[]{
            field1,
            field2,
            field3
        };
        // Índices das colunas correspondentes aos campos de filtro
        int[] columnIndexes = new int[]{
            1, // Cliente
            2, // Data Venda
            3 // Forma de Pagamento
        };
        // Configurar o filtro da tabela
        setupTableFilter(table,
                (DefaultTableModel) table.getModel(),
                filterFields,
                columnIndexes);
    }
}
