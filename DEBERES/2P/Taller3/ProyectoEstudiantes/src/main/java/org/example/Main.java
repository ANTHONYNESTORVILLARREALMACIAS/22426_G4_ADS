package org.example;

import org.example.presentacion.EstudiantesUI;

public class Main {
    public static void main(String[] args) {
        // Iniciar la interfaz gráfica de Swing en el hilo de eventos de Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            EstudiantesUI ventana = new EstudiantesUI();
            ventana.setVisible(true);
        });
    }
}
