package org.example;
import org.example.presentacion.EstudiantesUI;

/**
 * Clase principal que inicia la aplicación.
 * Utiliza SwingUtilities para ejecutar la interfaz gráfica en el hilo de despacho de eventos.
 */
public class Main {
    /**
     * Método principal que lanza la aplicación.
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new EstudiantesUI().setVisible(true));
    }
}