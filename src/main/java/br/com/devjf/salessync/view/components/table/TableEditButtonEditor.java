package br.com.devjf.salessync.view.components.table;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
/**
 * Editor de botão para edição de registros em uma tabela.
 * Esta classe fornece um botão que pode ser usado em uma célula de tabela
 * para acionar a edição de um registro.
 */
public class TableEditButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private int clickedRow;
    private final Consumer<Integer> editAction;

    /**
     * Cria um novo editor de botão para células de tabela.
     *
     * @param text O texto a ser exibido no botão
     * @param editAction Ação a ser executada quando o botão for clicado, recebendo o índice da linha
     */
    public TableEditButtonEditor(String text, Consumer<Integer> editAction) {
        super(new JTextField());
        this.editAction = editAction;
        button = new JButton(text);
        button.setOpaque(true);
        // Change the background color to purple (RGB: 156,39,176)
        button.setBackground(new java.awt.Color(156, 39, 176));
        button.setForeground(java.awt.Color.WHITE);
        
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
