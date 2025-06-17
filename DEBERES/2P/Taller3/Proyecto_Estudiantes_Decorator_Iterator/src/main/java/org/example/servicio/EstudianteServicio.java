package org.example.servicio;

import org.example.modelo.Estudiante;
import org.example.repositorio.EstudianteRepositorio;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que actúa como capa de servicio para gestionar operaciones sobre estudiantes.
 * Implementa el patrón de diseño Facade, proporcionando una interfaz simplificada
 * para interactuar con el repositorio de estudiantes.
 */
public class EstudianteServicio {
    // Repositorio que maneja el almacenamiento de estudiantes
    private final EstudianteRepositorio repositorio;

    /**
     * Constructor que inicializa el servicio con un repositorio.
     * @param repositorio El repositorio de estudiantes a utilizar.
     */
    public EstudianteServicio(EstudianteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Agrega un estudiante al repositorio.
     * @param e El estudiante a agregar.
     */
    public void agregarEstudiante(Estudiante e) {
        repositorio.agregar(e);
    }

    /**
     * Elimina un estudiante del repositorio por su ID.
     * @param id El ID del estudiante a eliminar.
     */
    public void eliminarEstudiante(int id) {
        repositorio.eliminar(id);
    }

    /**
     * Actualiza la información de un estudiante en el repositorio.
     * @param id El ID del estudiante a actualizar.
     * @param nombre El nuevo nombre del estudiante.
     * @param edad La nueva edad del estudiante.
     */
    public void actualizarEstudiante(int id, String nombre, int edad) {
        repositorio.actualizar(id, nombre, edad);
    }

    /**
     * Obtiene todos los estudiantes almacenados en el repositorio.
     * @return Una lista de todos los estudiantes.
     */
    public List<Estudiante> obtenerTodos() {
        return repositorio.obtenerTodos();
    }

    /**
     * Obtiene los estudiantes que tienen un decorador aplicado (como descuento).
     * @return Una lista de estudiantes con decoradores.
     */
    public List<Estudiante> obtenerConDescuento() {
        List<Estudiante> filtrados = new ArrayList<>();
        for (Estudiante e : repositorio) {
            if (e instanceof EstudianteDecorator) {
                filtrados.add(e);
            }
        }
        return filtrados;
    }
}