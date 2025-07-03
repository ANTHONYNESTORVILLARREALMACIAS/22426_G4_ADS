package org.example.controller;

import org.example.dao.EstudianteDAO;
import org.example.model.Estudiante;
import java.util.List;

/**
 * Clase que coordina la lógica entre la vista y el DAO.
 * Implementa las operaciones CRUD utilizando el EstudianteDAO.
 */
public class EstudianteController {
    private EstudianteDAO dao = new EstudianteDAO(); // Instancia del DAO para manejar datos

    /**
     * Crea un nuevo estudiante y lo agrega a través del DAO.
     * @param id Identificador del estudiante
     * @param apellidos Apellidos del estudiante
     * @param nombres Nombres del estudiante
     * @param edad Edad del estudiante
     */
    public void crearEstudiante(int id, String apellidos, String nombres, int edad) {
        Estudiante e = new Estudiante(id, apellidos, nombres, edad);
        dao.agregar(e);
    }

    /**
     * Obtiene la lista completa de estudiantes.
     * @return Lista de todos los estudiantes
     */
    public List<Estudiante> obtenerTodos() {
        return dao.listar();
    }

    /**
     * Busca un estudiante por su ID.
     * @param id Identificador del estudiante
     * @return Estudiante encontrado o null si no existe
     */
    public Estudiante buscarEstudiante(int id) {
        return dao.buscarPorId(id);
    }

    /**
     * Actualiza los datos de un estudiante existente.
     * @param id Identificador del estudiante
     * @param apellidos Nuevos apellidos
     * @param nombres Nuevos nombres
     * @param edad Nueva edad
     * @return true si se actualizó correctamente, false si no se encontró
     */
    public boolean actualizarEstudiante(int id, String apellidos, String nombres, int edad) {
        Estudiante e = new Estudiante(id, apellidos, nombres, edad);
        return dao.actualizar(e);
    }

    /**
     * Elimina un estudiante por su ID.
     * @param id Identificador del estudiante
     * @return true si se eliminó correctamente, false si no se encontró
     */
    public boolean eliminarEstudiante(int id) {
        return dao.eliminar(id);
    }

    /**
     * Verifica si un ID ya está registrado.
     * @param id Identificador a verificar
     * @return true si el ID existe, false si no
     */
    public boolean idExiste(int id) {
        return dao.buscarPorId(id) != null;
    }
}