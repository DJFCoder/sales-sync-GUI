package br.com.devjf.salessync.view.components.table;

import java.awt.Color;

/**
 * Renderizador de botão para edição de registros em uma tabela.
 * Esta classe fornece um botão que pode ser exibido em uma célula de tabela
 * com aparência personalizável.
 */
public class TableEditButtonRenderer extends AbstractButtonRenderer {
    
    /**
     * Cria um novo renderizador de botão para células de tabela.
     * 
     * @param text O texto a ser exibido no botão
     */
    public TableEditButtonRenderer(String text) {
        super(text, new Color(156, 39, 176), Color.WHITE);
    }
}
