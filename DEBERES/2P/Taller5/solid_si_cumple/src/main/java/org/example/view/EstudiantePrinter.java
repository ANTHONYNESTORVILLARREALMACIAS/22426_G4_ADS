package org.example.view;

import org.example.model.Estudiante;
import java.util.List;

public class EstudiantePrinter {
    public void mostrarMenuPrincipal() {
        System.out.println("\n=== SISTEMA DE ESTUDIANTES ===");
        System.out.println("1. Crear estudiante");
        System.out.println("2. Listar estudiantes");
        System.out.println("3. Buscar estudiante");
        System.out.println("4. Actualizar estudiante");
        System.out.println("5. Eliminar estudiante");
        System.out.println("6. Salir");
        System.out.print("Seleccione una opción: ");
    }

    public void mostrarEstudiante(Estudiante estudiante) {
        if (estudiante != null) {
            System.out.println("\n=== DETALLE ESTUDIANTE ===");
            System.out.println("ID: " + estudiante.getId());
            System.out.println("Nombre: " + estudiante.getNombre());
            System.out.println("Email: " + estudiante.getEmail());
        } else {
            System.out.println("\nEstudiante no encontrado.");
        }
    }

    public void mostrarListaEstudiantes(List<Estudiante> estudiantes) {
        System.out.println("\n=== LISTA DE ESTUDIANTES ===");
        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
        } else {
            System.out.println("ID\tNombre\t\tEmail");
            System.out.println("----------------------------------");
            estudiantes.forEach(e ->
                    System.out.println(e.getId() + "\t" + e.getNombre() + "\t\t" + e.getEmail()));
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("\n> " + mensaje);
    }

    public void mostrarError(String error) {
        System.err.println("\nERROR: " + error);
    }

    public void pedirDatosEstudiante() {
        System.out.println("\nIngrese los datos del estudiante:");
    }

    public void pedirIdEstudiante() {
        System.out.print("\nIngrese el ID del estudiante: ");
    }
}