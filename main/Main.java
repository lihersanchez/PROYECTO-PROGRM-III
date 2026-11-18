package main;

import javax.swing.SwingUtilities;
import gui.JFramePrincipal;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFramePrincipal ventana = new JFramePrincipal();
                ventana.setVisible(true);
            }
        });
    }
}