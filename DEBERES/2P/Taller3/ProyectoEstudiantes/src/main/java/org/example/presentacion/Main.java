package org.example.presentacion;

import org.example.modelo.Estudiante;
import org.example.servicio.EstudianteServicio;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal que interactúa con el usuario y ejecuta las operaciones del CRUD.
 */
public class Main {
    private static EstudianteServicio servicio = new EstudianteServicio();
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Método principal que inicia la aplicación.
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        while (true) {
            mostrarMenu();
            int opcion = leerOpcion();
            procesarOpcion(opcion);
        }
    }

    /**
     * Muestra el menú de opciones al usuario.
     */
    private static void mostrarMenu() {
        System.out.println("\n=== Sistema de Gestión de Estudiantes ===");
        System.out.println("1. Agregar estudiante");
        System.out.println("2. Listar estudiantes");
        System.out.println("3. Buscar estudiante por orden");
        System.out.println("4. Actualizar estudiante");
        System.out.println("5. Eliminar estudiante");
        System.out.println("6. Salir");
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Lee la opción seleccionada por el usuario.
     * @return El número de opción ingresado.
     */
    private static int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Procesa la opción seleccionada por el usuario.
     * @param opcion La opción seleccionada.
     */
    private static void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                agregarEstudiante();
                break;
            case 2:
                listarEstudiantes();
                break;
            case 3:
                buscarEstudiante();
                break;
            case 4:
                actualizarEstudiante();
                break;
            case 5:
                eliminarEstudiante();
                break;
            case 6:
                System.out.println("Saliendo del sistema...");
                System.exit(0);
                break;
            default:
                System.out.println("Opción no válida. Intente de nuevo.");
        }
    }

    /**
     * Solicita datos al usuario y agrega un nuevo estudiante.
     */
    private static void agregarEstudiante() {
        try {
            System.out.print("Ingrese el número de orden: ");
            int orden = Integer.parseInt(scanner.nextLine());
            System.out.print("Ingrese el nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese la edad: ");
            int edad = Integer.parseInt(scanner.nextLine());
            servicio.agregarEstudiante(orden, nombre, edad);
            System.out.println("Estudiante agregado exitosamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Lista todos los estudiantes registrados.
     */
    private static void listarEstudiantes() {
        List<Estudiante> estudiantes = servicio.listarEstudiantes();
        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
        } else {
            System.out.println("\nLista de estudiantes:");
            for (Estudiante estudiante : estudiantes) {
                System.out.println(estudiante);
            }
        }
    }

    /**
     * Busca un estudiante por su número de orden.
     */
    private static void buscarEstudiante() {
        try {
            System.out.print("Ingrese el número de orden: ");
            int orden = Integer.parseInt(scanner.nextLine());
            Estudiante estudiante = servicio.buscarEstudiante(orden);
            if (estudiante != null) {
                System.out.println("Estudiante encontrado: " + estudiante);
            } else {
                System.out.println("No se encontró un estudiante con ese número de orden.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un valor numérico válido para el orden.");
        }
    }

    /**
     * Actualiza los datos de un estudiante existente.
     */
    private static void actualizarEstudiante() {
        try {
            System.out.print("Ingrese el número de orden del estudiante a actualizar: ");
            int orden = Integer.parseInt(scanner.nextLine());
            System.out.print("Ingrese el nuevo nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese la nueva edad: ");
            int edad = Integer.parseInt(scanner.nextLine());
            servicio.actualizarEstudiante(orden, nombre, edad);
            System.out.println("Estudiante actualizado exitosamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Elimina un estudiante por su número de orden.
     */
    private static void eliminarEstudiante() {
        try {
            System.out.print("Ingrese el número de orden del estudiante a eliminar: ");
            int orden = Integer.parseInt(scanner.nextLine());
            servicio.eliminarEstudiante(orden);
            System.out.println("Estudiante eliminado exitosamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}