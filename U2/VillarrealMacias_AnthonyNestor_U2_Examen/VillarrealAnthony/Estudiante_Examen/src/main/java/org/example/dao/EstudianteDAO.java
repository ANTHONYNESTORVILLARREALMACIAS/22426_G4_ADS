package org.example.dao;

import java.util.ArrayList;
import java.util.List;
import org.example.model.Estudiante;

// Clase que maneja las operaciones de acceso a datos para Estudiante
public class EstudianteDAO {
    private List<Estudiante> estudiantes = new ArrayList<>();

    // Agrega un estudiante a la lista
    public void agregar(Estudiante e) {
        estudiantes.add(e);
    }

    // Retorna la lista completa de estudiantes
    public List<Estudiante> listar() {
        return estudiantes;
    }

    // Busca un estudiante por su ID
    public Estudiante buscarPorId(int id) {
        for (Estudiante e : estudiantes) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }
}