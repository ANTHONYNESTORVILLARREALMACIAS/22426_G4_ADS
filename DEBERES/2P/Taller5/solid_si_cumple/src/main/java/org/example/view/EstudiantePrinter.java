package org.example.view;

import org.example.model.Estudiante;

/**
 * Clase responsable de presentar información de estudiantes.
 * Cumple con SRP: única responsabilidad de mostrar datos al usuario.
 */
public class EstudiantePrinter {
    /**
     * Imprime los detalles de un estudiante en la consola.
     * @param estudiante El estudiante a imprimir.
     */
    public void imprimirDetalles(Estudiante estudiante) {
        System.out.println("\n=== Detalles del Estudiante ===");
        System.out.println("ID: " + estudiante.getId());
        System.out.println("Nombre: " + estudiante.getNombre());
        System.out.println("==============================");
    }

    /**
     * Imprime un mensaje de error.
     * @param mensaje El mensaje de error a mostrar.
     */
    public void imprimirError(String mensaje) {
        System.err.println("Error: " + mensaje);
    }

    /**
     * Imprime un mensaje informativo.
     * @param mensaje El mensaje a mostrar.
     */
    public void imprimirMensaje(String mensaje) {
        System.out.println("Info: " + mensaje);
    }
}