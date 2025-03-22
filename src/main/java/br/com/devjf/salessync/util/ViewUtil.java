package br.com.devjf.salessync.util;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class ViewUtil {
    public static void standardCornerRadius(JPanel panel) {
        panel.putClientProperty("FlatLaf.style",
                "arc: 10");
    }

    public static void standardCornerRadius(JButton panel) {
        panel.putClientProperty("FlatLaf.style",
                "arc: 10");
    }

    public static void standardCornerRadius(JTextField panel) {
        panel.putClientProperty("FlatLaf.style",
                "arc: 10");
    }

    public static void standardCornerRadius(JScrollPane panel) {
        panel.putClientProperty("FlatLaf.style",
                "arc: 10");
    }

    public static void standardCornerRadius(JFormattedTextField table) {
        table.putClientProperty("FlatLaf.style",
                "arc: 10");
    }

    public static void standardCornerRadius(JPasswordField table) {
        table.putClientProperty("FlatLaf.style",
                "arc: 10; showRevealButton: true");
    }
}
