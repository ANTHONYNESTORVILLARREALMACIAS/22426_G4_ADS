package org.example.ui;

import org.example.dao.EstudianteDAO;
import org.example.model.Estudiante;
import org.example.view.EstudiantePrinter;

/**
 * Clase principal que demuestra el uso de las diferentes componentes.
 * Aquí podríamos implementar un patrón como Service o Controller
 * para manejar la lógica de negocio y coordinar las otras clases.
 */
public class App {
    public static void main(String[] args) {
        // Crear componentes
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        EstudiantePrinter printer = new EstudiantePrinter();

        // Crear un nuevo estudiante
        Estudiante estudiante = new Estudiante(1, "Juan Pérez");
        printer.imprimirMensaje("Creando nuevo estudiante...");

        // Guardar estudiante
        estudianteDAO.guardar(estudiante);

        // Mostrar detalles
        printer.imprimirDetalles(estudiante);

        // Buscar estudiante
        printer.imprimirMensaje("Buscando estudiante...");
        Estudiante estudianteBuscado = estudianteDAO.buscarPorId(1);
        printer.imprimirDetalles(estudianteBuscado);

        // Actualizar estudiante
        printer.imprimirMensaje("Actualizando nombre del estudiante...");
        estudianteBuscado.setNombre("Juan Pérez Actualizado");
        estudianteDAO.guardar(estudianteBuscado);
        printer.imprimirDetalles(estudianteBuscado);

        // Eliminar estudiante
        printer.imprimirMensaje("Eliminando estudiante...");
        estudianteDAO.eliminar(1);
    }
}