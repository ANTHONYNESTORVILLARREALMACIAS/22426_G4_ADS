package org.example;
import org.example.presentacion.EstudiantesUI;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new EstudiantesUI().setVisible(true));
    }
}
