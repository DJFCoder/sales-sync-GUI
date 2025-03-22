package br.com.devjf.salessync;

import br.com.devjf.salessync.view.Login;
//import br.com.devjf.salessync.view.forms.newobjectforms.NewSaleForm;
import com.formdev.flatlaf.FlatIntelliJLaf;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author devjf
 */
public class SalesSyncApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            new Login().setVisible(true);
//            new NewSaleForm().setVisible(true);
        } catch (UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Failed to initialize LaF " + e.getMessage(),
                    "ERRO",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
