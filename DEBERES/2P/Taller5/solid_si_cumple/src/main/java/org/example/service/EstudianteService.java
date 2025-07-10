package org.example.service;

import org.example.model.Estudiante;
import org.example.repository.EstudianteRepository;
import java.util.List;

public class EstudianteService {
    private final EstudianteRepository repository;

    public EstudianteService(EstudianteRepository repository) {
        this.repository = repository;
    }

    public void crearEstudiante(String nombre, String email) {
        Estudiante nuevo = new Estudiante(0, nombre, email);
        repository.guardar(nuevo);
    }

    public Estudiante obtenerEstudiante(int id) {
        return repository.buscarPorId(id);
    }

    public List<Estudiante> listarEstudiantes() {
        return repository.listarTodos();
    }

    public void actualizarEstudiante(int id, String nombre, String email) {
        Estudiante estudiante = new Estudiante(id, nombre, email);
        repository.actualizar(estudiante);
    }

    public void eliminarEstudiante(int id) {
        repository.eliminar(id);
    }
}