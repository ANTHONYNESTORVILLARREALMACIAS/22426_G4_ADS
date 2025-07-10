package org.example.view;

import org.example.model.Estudiante;
import org.example.service.EstudianteService;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner;
    private final EstudianteService service;
    private final EstudiantePrinter printer;

    public Menu(EstudianteService service, EstudiantePrinter printer) {
        this.scanner = new Scanner(System.in);
        this.service = service;
        this.printer = printer;
    }

    public void iniciar() {
        boolean salir = false;

        while (!salir) {
            printer.mostrarMenuPrincipal();
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    crearEstudiante();
                    break;
                case "2":
                    listarEstudiantes();
                    break;
                case "3":
                    buscarEstudiante();
                    break;
                case "4":
                    actualizarEstudiante();
                    break;
                case "5":
                    eliminarEstudiante();
                    break;
                case "6":
                    salir = true;
                    printer.mostrarMensaje("Saliendo del sistema...");
                    break;
                default:
                    printer.mostrarError("Opción no válida");
            }
        }
        scanner.close();
    }

    private void crearEstudiante() {
        printer.pedirDatosEstudiante();

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        if (!nombre.isEmpty() && !email.isEmpty()) {
            service.crearEstudiante(nombre, email);
            printer.mostrarMensaje("Estudiante creado con éxito");
        } else {
            printer.mostrarError("Nombre y email son requeridos");
        }
    }

    private void listarEstudiantes() {
        printer.mostrarListaEstudiantes(service.listarEstudiantes());
    }

    private void buscarEstudiante() {
        printer.pedirIdEstudiante();
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Estudiante encontrado = service.obtenerEstudiante(id);
            printer.mostrarEstudiante(encontrado);
        } catch (NumberFormatException e) {
            printer.mostrarError("Debe ingresar un número válido");
        }
    }

    private void actualizarEstudiante() {
        printer.pedirIdEstudiante();
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Estudiante estudiante = service.obtenerEstudiante(id);

            if (estudiante != null) {
                printer.mostrarEstudiante(estudiante);

                System.out.print("\nNuevo nombre (actual: " + estudiante.getNombre() + "): ");
                String nombre = scanner.nextLine();

                System.out.print("Nuevo email (actual: " + estudiante.getEmail() + "): ");
                String email = scanner.nextLine();

                if (!nombre.isEmpty() && !email.isEmpty()) {
                    service.actualizarEstudiante(id, nombre, email);
                    printer.mostrarMensaje("Estudiante actualizado con éxito");
                } else {
                    printer.mostrarError("Nombre y email son requeridos");
                }
            } else {
                printer.mostrarError("Estudiante no encontrado");
            }
        } catch (NumberFormatException e) {
            printer.mostrarError("Debe ingresar un número válido");
        }
    }

    private void eliminarEstudiante() {
        printer.pedirIdEstudiante();
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Estudiante estudiante = service.obtenerEstudiante(id);

            if (estudiante != null) {
                printer.mostrarEstudiante(estudiante);
                System.out.print("\n¿Está seguro que desea eliminar este estudiante? (s/n): ");
                String confirmacion = scanner.nextLine();

                if (confirmacion.equalsIgnoreCase("s")) {
                    service.eliminarEstudiante(id);
                    printer.mostrarMensaje("Estudiante eliminado con éxito");
                } else {
                    printer.mostrarMensaje("Operación cancelada");
                }
            } else {
                printer.mostrarError("Estudiante no encontrado");
            }
        } catch (NumberFormatException e) {
            printer.mostrarError("Debe ingresar un número válido");
        }
    }
}