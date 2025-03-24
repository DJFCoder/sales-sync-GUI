package br.com.devjf.salessync.util;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * Editor de botão para edição de registros em uma tabela.
 * Esta classe fornece um botão que pode ser usado em uma célula de tabela
 * para acionar a edição de um registro.
 */
public class TableEditButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private final JButton button;
    private int clickedRow;
    private final Consumer<Integer> editAction;

    /**
     * Cria um novo editor de botão para células de tabela.
     *
     * @param buttonText O texto a ser exibido no botão
     * @param editAction Ação a ser executada quando o botão for clicado, recebendo o índice da linha
     */
    public TableEditButtonEditor(String buttonText, Consumer<Integer> editAction) {
        this.editAction = editAction;
        button = new JButton(buttonText);
        button.setOpaque(true);
        button.setBackground(new java.awt.Color(76, 175, 80)); // Verde para edição
        button.setForeground(new java.awt.Color(255, 255, 255));
        ViewUtil.standardCornerRadius(button);
        
        button.addActionListener((ActionEvent e) -> {
            // Verificar se a linha existe antes de editar
            if (clickedRow >= 0) {
                // Executar a ação de edição
                if (editAction != null) {
                    editAction.accept(clickedRow);
                }
            }
            // Encerrar a edição
            cancelCellEditing();
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        clickedRow = row;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
}
