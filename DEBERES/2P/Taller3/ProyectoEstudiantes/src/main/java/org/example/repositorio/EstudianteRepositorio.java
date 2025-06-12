package org.example.repositorio;

import org.example.modelo.Estudiante;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona la persistencia de datos de estudiantes en memoria.
 */
public class EstudianteRepositorio {
    private List<Estudiante> estudiantes;

    /**
     * Constructor de la clase EstudianteRepositorio.
     * Inicializa la lista de estudiantes.
     */
    public EstudianteRepositorio() {
        this.estudiantes = new ArrayList<>();
    }

    /**
     * Agrega un nuevo estudiante al repositorio.
     * @param estudiante El estudiante a agregar.
     */
    public void agregarEstudiante(Estudiante estudiante) {
        estudiantes.add(estudiante);
    }

    /**
     * Obtiene todos los estudiantes registrados.
     * @return Una lista de todos los estudiantes.
     */
    public List<Estudiante> obtenerTodos() {
        return new ArrayList<>(estudiantes);
    }

    /**
     * Busca un estudiante por su número de orden.
     * @param orden El número de orden del estudiante.
     * @return El estudiante encontrado o null si no existe.
     */
    public Estudiante buscarPorOrden(int orden) {
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getOrden() == orden) {
                return estudiante;
            }
        }
        return null;
    }

    /**
     * Actualiza los datos de un estudiante existente.
     * @param estudiante El estudiante con los datos actualizados.
     * @return true si se actualizó correctamente, false si no se encontró.
     */
    public boolean actualizarEstudiante(Estudiante estudiante) {
        for (int i = 0; i < estudiantes.size(); i++) {
            if (estudiantes.get(i).getOrden() == estudiante.getOrden()) {
                estudiantes.set(i, estudiante);
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina un estudiante por su número de orden.
     * @param orden El número de orden del estudiante a eliminar.
     * @return true si se eliminó correctamente, false si no se encontró.
     */
    public boolean eliminarEstudiante(int orden) {
        return estudiantes.removeIf(estudiante -> estudiante.getOrden() == orden);
    }
}