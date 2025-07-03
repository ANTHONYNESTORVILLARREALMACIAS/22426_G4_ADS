package org.example.controller;

import org.example.dao.EstudianteDAO;
import org.example.model.Estudiante;
import java.util.List;

// Clase que coordina la lógica entre la vista y el DAO
public class EstudianteController {
    private EstudianteDAO dao = new EstudianteDAO();

    // Crea un nuevo estudiante y lo agrega a través del DAO
    public void crearEstudiante(int id, String apellidos, String nombres, int edad) {
        Estudiante e = new Estudiante(id, apellidos, nombres, edad);
        dao.agregar(e);
    }

    // Obtiene todos los estudiantes desde el DAO
    public List<Estudiante> obtenerTodos() {
        return dao.listar();
    }

    // Busca un estudiante por ID usando el DAO
    public Estudiante buscarEstudiante(int id) {
        return dao.buscarPorId(id);
    }
}