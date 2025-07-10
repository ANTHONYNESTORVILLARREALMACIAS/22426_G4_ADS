package org.example.repository;

import org.example.model.Estudiante;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EstudianteDAO implements EstudianteRepository {
    private final List<Estudiante> estudiantes = new ArrayList<>();
    private int contadorId = 1;

    @Override
    public void guardar(Estudiante estudiante) {
        estudiante.setId(contadorId++);
        estudiantes.add(estudiante);
    }

    @Override
    public Estudiante buscarPorId(int id) {
        return estudiantes.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Estudiante> listarTodos() {
        return new ArrayList<>(estudiantes);
    }

    @Override
    public void actualizar(Estudiante estudiante) {
        Optional<Estudiante> estudianteExistente = estudiantes.stream()
                .filter(e -> e.getId() == estudiante.getId())
                .findFirst();

        estudianteExistente.ifPresent(e -> {
            e.setNombre(estudiante.getNombre());
            e.setEmail(estudiante.getEmail());
        });
    }

    @Override
    public void eliminar(int id) {
        estudiantes.removeIf(e -> e.getId() == id);
    }
}