package org.example.servicio;

import org.example.modelo.Estudiante;
import org.example.repositorio.EstudianteRepositorio;
import java.util.List;

/**
 * Clase que contiene la lógica de negocio para la gestión de estudiantes.
 */
public class EstudianteServicio {
    private EstudianteRepositorio repositorio;

    /**
     * Constructor de la clase EstudianteServicio.
     * Inicializa el repositorio de estudiantes.
     */
    public EstudianteServicio() {
        this.repositorio = new EstudianteRepositorio();
    }

    /**
     * Agrega un nuevo estudiante al sistema.
     * @param orden Número de orden del estudiante.
     * @param nombre Nombre del estudiante.
     * @param edad Edad del estudiante.
     * @throws IllegalArgumentException si el orden ya existe o los datos son inválidos.
     */
    public void agregarEstudiante(int orden, String nombre, int edad) {
        if (repositorio.buscarPorOrden(orden) != null) {
            throw new IllegalArgumentException("El número de orden ya existe.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (edad <= 0) {
            throw new IllegalArgumentException("La edad debe ser mayor que 0.");
        }
        Estudiante estudiante = new Estudiante(orden, nombre, edad);
        repositorio.agregarEstudiante(estudiante);
    }

    /**
     * Obtiene todos los estudiantes registrados.
     * @return Una lista de todos los estudiantes.
     */
    public List<Estudiante> listarEstudiantes() {
        return repositorio.obtenerTodos();
    }

    /**
     * Busca un estudiante por su número de orden.
     * @param orden El número de orden del estudiante.
     * @return El estudiante encontrado o null si no existe.
     */
    public Estudiante buscarEstudiante(int orden) {
        return repositorio.buscarPorOrden(orden);
    }

    /**
     * Actualiza los datos de un estudiante existente.
     * @param orden Número de orden del estudiante.
     * @param nombre Nuevo nombre del estudiante.
     * @param edad Nueva edad del estudiante.
     * @throws IllegalArgumentException si el estudiante no existe o los datos son inválidos.
     */
    public void actualizarEstudiante(int orden, String nombre, int edad) {
        Estudiante estudiante = repositorio.buscarPorOrden(orden);
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no existe.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (edad <= 0) {
            throw new IllegalArgumentException("La edad debe ser mayor que 0.");
        }
        Estudiante actualizado = new Estudiante(orden, nombre, edad);
        repositorio.actualizarEstudiante(actualizado);
    }

    /**
     * Elimina un estudiante por su número de orden.
     * @param orden El número de orden del estudiante.
     * @throws IllegalArgumentException si el estudiante no existe.
     */
    public void eliminarEstudiante(int orden) {
        if (repositorio.buscarPorOrden(orden) == null) {
            throw new IllegalArgumentException("El estudiante no existe.");
        }
        repositorio.eliminarEstudiante(orden);
    }
}