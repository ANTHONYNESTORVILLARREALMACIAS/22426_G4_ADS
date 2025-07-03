package org.example.ui;

import org.example.controller.EstudianteController;
import org.example.model.Estudiante;

// Clase principal que actúa como vista y muestra los datos
public class Main {
    public static void main(String[] args) {
        EstudianteController controller = new EstudianteController();

        // Crear estudiantes de ejemplo
        controller.crearEstudiante(1, "Pérez", "Ana", 20);
        controller.crearEstudiante(2, "García", "Luis", 22);

        // Mostrar todos los estudiantes registrados
        for (Estudiante e : controller.obtenerTodos()) {
            System.out.println(e.getId() + " - " + e.getApellidos() + " " + e.getNombres() + " - Edad: " + e.getEdad());
        }
    }
}