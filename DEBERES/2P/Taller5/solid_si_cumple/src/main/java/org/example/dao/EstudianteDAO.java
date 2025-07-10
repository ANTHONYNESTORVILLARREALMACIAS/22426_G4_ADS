package org.example.dao;

import org.example.model.Estudiante;

/**
 * Clase DAO (Data Access Object) para manejar la persistencia de estudiantes.
 * Cumple con SRP: única responsabilidad de interactuar con la base de datos.
 */
public class EstudianteDAO {
    /**
     * Guarda un estudiante en la base de datos.
     * @param estudiante El estudiante a guardar.
     */
    public void guardar(Estudiante estudiante) {
        // Simulación: en una aplicación real aquí iría el código JDBC/JPA/Hibernate
        System.out.println("Guardando estudiante en la BD: " + estudiante);
    }

    /**
     * Busca un estudiante por su ID.
     * @param id El ID del estudiante a buscar.
     * @return El estudiante encontrado o null si no existe.
     */
    public Estudiante buscarPorId(int id) {
        // Simulación: en una aplicación real esto consultaría la BD
        System.out.println("Buscando estudiante con ID: " + id);
        return new Estudiante(id, "Estudiante de ejemplo");
    }

    /**
     * Elimina un estudiante de la base de datos.
     * @param id El ID del estudiante a eliminar.
     */
    public void eliminar(int id) {
        // Simulación: en una aplicación real esto eliminaría de la BD
        System.out.println("Eliminando estudiante con ID: " + id);
    }
}