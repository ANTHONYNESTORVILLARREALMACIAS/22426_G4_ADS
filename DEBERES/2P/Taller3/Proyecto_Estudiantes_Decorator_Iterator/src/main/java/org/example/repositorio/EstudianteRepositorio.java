package org.example.repositorio;

import org.example.modelo.Estudiante;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EstudianteRepositorio implements Iterable<Estudiante> {
    private final List<Estudiante> estudiantes = new ArrayList<>();

    public void agregar(Estudiante estudiante) {
        estudiantes.add(estudiante);
    }

    public void eliminar(int id) {
        estudiantes.removeIf(e -> e.getId() == id);
    }

    public void actualizar(int id, String nombre, int edad) {
        for (Estudiante e : estudiantes) {
            if (e.getId() == id) {
                e.setNombre(nombre);
                e.setEdad(edad);
                break;
            }
        }
    }

    public List<Estudiante> obtenerTodos() {
        return estudiantes;
    }

    @Override
    public Iterator<Estudiante> iterator() {
        return estudiantes.iterator(); // Implementación básica
    }
}
