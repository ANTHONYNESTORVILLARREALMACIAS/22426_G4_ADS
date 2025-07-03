package org.example.ui;

import org.example.controller.EstudianteController;
import org.example.model.Estudiante;
import java.util.Scanner;

/**
 * Clase principal que implementa la interfaz de usuario.
 * Proporciona un menú interactivo para realizar operaciones CRUD.
 * Inicializa dos estudiantes de ejemplo al comenzar el programa.
 */
public class Main {
    private static EstudianteController controller = new EstudianteController(); // Controlador para operaciones
    private static Scanner scanner = new Scanner(System.in); // Scanner para entrada de usuario

    /**
     * Método principal que ejecuta el programa, inicializa datos y muestra el menú.
     * @param args Argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        // Inicializar datos quemados
        inicializarDatos();

        while (true) {
            limpiarPantalla();
            mostrarMenu();
            int opcion = obtenerOpcion();
            switch (opcion) {
                case 1:
                    crearEstudiante();
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
                    limpiarPantalla();
                    System.out.println("Saliendo del programa...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
                    esperarTecla();
            }
        }
    }

    /**
     * Inicializa los datos quemados con dos estudiantes de ejemplo.
     */
    private static void inicializarDatos() {
        controller.crearEstudiante(1, "Pérez", "Ana", 20);
        controller.crearEstudiante(2, "García", "Luis", 22);
    }

    /**
     * Limpia la pantalla de la consola.
     */
    private static void limpiarPantalla() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Muestra el menú de opciones disponibles.
     */
    private static void mostrarMenu() {
        System.out.println("\n=== Sistema CRUD de Estudiantes ===");
        System.out.println("1. Crear estudiante");
        System.out.println("2. Listar estudiantes");
        System.out.println("3. Buscar estudiante por ID");
        System.out.println("4. Actualizar estudiante");
        System.out.println("5. Eliminar estudiante");
        System.out.println("6. Salir");
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Obtiene la opción seleccionada por el usuario.
     * @return Entero que representa la opción, -1 si hay error
     */
    private static int obtenerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Solicita datos al usuario para crear un nuevo estudiante.
     */
    private static void crearEstudiante() {
        limpiarPantalla();
        System.out.println("=== Crear Nuevo Estudiante ===");
        System.out.print("Ingrese ID: ");
        int id = obtenerEntero();
        while (controller.idExiste(id)) {
            System.out.print("Error: El ID ya existe. Ingrese otro ID: ");
            id = obtenerEntero();
        }
        System.out.print("Ingrese apellidos: ");
        String apellidos = validarTexto("apellidos");
        System.out.print("Ingrese nombres: ");
        String nombres = validarTexto("nombres");
        System.out.print("Ingrese edad: ");
        int edad = validarEdad();

        controller.crearEstudiante(id, apellidos, nombres, edad);
        System.out.println("Estudiante creado exitosamente!");
        esperarTecla();
    }

    /**
     * Muestra la lista de todos los estudiantes registrados.
     */
    private static void listarEstudiantes() {
        limpiarPantalla();
        System.out.println("=== Lista de Estudiantes ===");
        for (Estudiante e : controller.obtenerTodos()) {
            System.out.println(e.getId() + " - " + e.getApellidos() + " " + e.getNombres() + " - Edad: " + e.getEdad());
        }
        esperarTecla();
    }

    /**
     * Busca y muestra un estudiante por su ID.
     */
    private static void buscarEstudiante() {
        limpiarPantalla();
        System.out.println("=== Buscar Estudiante ===");
        System.out.print("Ingrese ID del estudiante a buscar: ");
        int id = obtenerEntero();
        Estudiante e = controller.buscarEstudiante(id);
        if (e != null) {
            System.out.println(e.getId() + " - " + e.getApellidos() + " " + e.getNombres() + " - Edad: " + e.getEdad());
        } else {
            System.out.println("Estudiante no encontrado.");
        }
        esperarTecla();
    }

    /**
     * Actualiza los datos de un estudiante existente.
     */
    private static void actualizarEstudiante() {
        limpiarPantalla();
        System.out.println("=== Actualizar Estudiante ===");
        System.out.print("Ingrese ID del estudiante a actualizar: ");
        int id = obtenerEntero();
        Estudiante e = controller.buscarEstudiante(id);
        if (e == null) {
            System.out.println("Estudiante no encontrado.");
            esperarTecla();
            return;
        }
        System.out.println("Estudiante actual: " + e.getId() + " - " + e.getApellidos() + " " + e.getNombres() + " - Edad: " + e.getEdad());
        System.out.print("Ingrese nuevos apellidos: ");
        String apellidos = validarTexto("apellidos");
        System.out.print("Ingrese nuevos nombres: ");
        String nombres = validarTexto("nombres");
        System.out.print("Ingrese nueva edad: ");
        int edad = validarEdad();

        if (controller.actualizarEstudiante(id, apellidos, nombres, edad)) {
            System.out.println("Estudiante actualizado exitosamente!");
        } else {
            System.out.println("Error al actualizar el estudiante.");
        }
        esperarTecla();
    }

    /**
     * Elimina un estudiante por su ID.
     */
    private static void eliminarEstudiante() {
        limpiarPantalla();
        System.out.println("=== Eliminar Estudiante ===");
        System.out.print("Ingrese ID del estudiante a eliminar: ");
        int id = obtenerEntero();
        if (controller.eliminarEstudiante(id)) {
            System.out.println("Estudiante eliminado exitosamente!");
        } else {
            System.out.println("Estudiante no encontrado.");
        }
        esperarTecla();
    }

    /**
     * Obtiene un número entero válido del usuario.
     * @return Entero ingresado por el usuario
     */
    private static int obtenerEntero() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Por favor, ingrese un número válido: ");
            }
        }
    }

    /**
     * Valida que el texto ingresado no esté vacío.
     * @param campo Nombre del campo para mensaje de error
     * @return Texto validado
     */
    private static String validarTexto(String campo) {
        String texto;
        do {
            texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.print("El campo " + campo + " no puede estar vacío. Ingrese nuevamente: ");
            }
        } while (texto.isEmpty());
        return texto;
    }

    /**
     * Valida que la edad esté entre 1 y 120 años.
     * @return Edad validada
     */
    private static int validarEdad() {
        int edad;
        do {
            edad = obtenerEntero();
            if (edad < 1 || edad > 120) {
                System.out.print("La edad debe estar entre 1 y 120 años. Ingrese nuevamente: ");
            }
        } while (edad < 1 || edad > 120);
        return edad;
    }

    /**
     * Pausa la ejecución hasta que el usuario presione una tecla.
     */
    private static void esperarTecla() {
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
}