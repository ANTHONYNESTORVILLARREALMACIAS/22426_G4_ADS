package org.example;

import org.example.presentacion.EstudiantesUI;

/**
 * Clase principal que sirve como punto de entrada para la aplicación.
 * Inicia la interfaz gráfica de gestión de estudiantes.
 */
public class Main {

    /**
     * Método principal que inicia la aplicación.
     * Crea y muestra la interfaz gráfica {@link EstudiantesUI} en el hilo de despacho de eventos de Swing.
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        // Iniciar la interfaz gráfica de Swing en el hilo de eventos de Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            EstudiantesUI ventana = new EstudiantesUI();
            ventana.setVisible(true);
        });
    }
}