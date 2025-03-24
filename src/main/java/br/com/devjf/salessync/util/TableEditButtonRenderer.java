package br.com.devjf.salessync.util;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Renderizador de botão para edição de registros em uma tabela.
 * Esta classe fornece um botão que pode ser exibido em uma célula de tabela
 * com aparência personalizável.
 */
public class TableEditButtonRenderer extends JButton implements TableCellRenderer {
    
    /**
     * Cria um novo renderizador de botão para células de tabela.
     * 
     * @param text O texto a ser exibido no botão
     */
    public TableEditButtonRenderer(String text) {
        setText(text);
        setOpaque(true);
        setBackground(new java.awt.Color(76, 175, 80)); // Verde para edição
        setForeground(new java.awt.Color(255, 255, 255));
        ViewUtil.standardCornerRadius(this);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}
