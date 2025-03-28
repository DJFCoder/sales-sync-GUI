package br.com.devjf.salessync.view.components.table;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import java.awt.Color;

/**
 * Editor de botão para edição de registros em uma tabela.
 * Esta classe fornece um botão que pode ser usado em uma célula de tabela
 * para acionar a edição de um registro.
 */
public class TableEditButtonEditor extends AbstractButtonEditor {
    private final Consumer<Integer> editAction;

    /**
     * Cria um novo editor de botão para células de tabela.
     *
     * @param text O texto a ser exibido no botão
     * @param editAction Ação a ser executada quando o botão for clicado, recebendo o índice da linha
     */
    public TableEditButtonEditor(String text, Consumer<Integer> editAction) {
        super(text, new Color(156, 39, 176), Color.WHITE);
        this.editAction = editAction;
        
        button.addActionListener((ActionEvent e) -> {
            // Verificar se a linha existe antes de editar
            if (clickedRow >= 0) {
                // Executar a ação de edição
                if (editAction != null) {
                    editAction.accept(clickedRow);
                }
            }
            // Encerrar a edição
            fireEditingStopped();
        });
    }
}
