package org.example.repository;

import org.example.model.Estudiante;
import java.util.List;
/**
 * Interfaz que define las operaciones de persistencia.
 * Cumple con ISP: Solo contiene métodos esenciales.
 */

public interface EstudianteRepository {
    void guardar(Estudiante estudiante);
    Estudiante buscarPorId(int id);
    List<Estudiante> listarTodos();
    void actualizar(Estudiante estudiante);
    void eliminar(int id);
}