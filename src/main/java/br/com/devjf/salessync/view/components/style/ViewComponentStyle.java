package br.com.devjf.salessync.view.components.style;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ViewComponentStyle {
    public static void standardCornerRadius(JPanel panel) {
        panel.putClientProperty("FlatLaf.style",
                "arc: 10");
    }

    public static void standardCornerRadius(JButton button) {
        button.putClientProperty("FlatLaf.style",
                "arc: 10");
    }

    public static void standardCornerRadius(JTextField field) {
        field.putClientProperty("FlatLaf.style",
                "arc: 10");
    }

    public static void standardCornerRadius(JScrollPane scrollPanel) {
        scrollPanel.putClientProperty("FlatLaf.style",
                "arc: 10");
    }

    public static void standardCornerRadius(JFormattedTextField field) {
        field.putClientProperty("FlatLaf.style",
                "arc: 10");
    }

    public static void standardCornerRadius(JPasswordField field) {
        field.putClientProperty("FlatLaf.style",
                "arc: 10; showRevealButton: true");
    }
    
    public static void standardCornerRadius(JTextArea text) {
        text.putClientProperty("FlatLaf.style",
                "arc: 10");
    }
}
