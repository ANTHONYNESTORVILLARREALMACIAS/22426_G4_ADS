package org.example.dao;

import java.util.ArrayList;
import java.util.List;
import org.example.model.Estudiante;

/**
 * Clase que maneja las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para la entidad Estudiante.
 * Utiliza una lista en memoria para almacenar los datos.
 */
public class EstudianteDAO {
    private List<Estudiante> estudiantes = new ArrayList<>(); // Lista para almacenar estudiantes

    /**
     * Agrega un nuevo estudiante a la lista.
     * @param e Estudiante a agregar
     */
    public void agregar(Estudiante e) {
        estudiantes.add(e);
    }

    /**
     * Retorna la lista completa de estudiantes.
     * @return Lista de estudiantes
     */
    public List<Estudiante> listar() {
        return estudiantes;
    }

    /**
     * Busca un estudiante por su ID.
     * @param id Identificador del estudiante
     * @return Estudiante encontrado o null si no existe
     */
    public Estudiante buscarPorId(int id) {
        for (Estudiante e : estudiantes) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    /**
     * Actualiza los datos de un estudiante existente.
     * @param e Estudiante con los datos actualizados
     * @return true si se actualizó correctamente, false si no se encontró
     */
    public boolean actualizar(Estudiante e) {
        for (int i = 0; i < estudiantes.size(); i++) {
            if (estudiantes.get(i).getId() == e.getId()) {
                estudiantes.set(i, e);
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina un estudiante por su ID.
     * @param id Identificador del estudiante
     * @return true si se eliminó correctamente, false si no se encontró
     */
    public boolean eliminar(int id) {
        for (int i = 0; i < estudiantes.size(); i++) {
            if (estudiantes.get(i).getId() == id) {
                estudiantes.remove(i);
                return true;
            }
        }
        return false;
    }
}