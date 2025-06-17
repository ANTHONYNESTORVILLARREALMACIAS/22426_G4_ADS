package org.example.servicio;

import org.example.modelo.Estudiante;
import org.example.repositorio.EstudianteRepositorio;

import java.util.ArrayList;
import java.util.List;

public class EstudianteServicio {
    private final EstudianteRepositorio repositorio;

    public EstudianteServicio(EstudianteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public void agregarEstudiante(Estudiante e) {
        repositorio.agregar(e);
    }

    public void eliminarEstudiante(int id) {
        repositorio.eliminar(id);
    }

    public void actualizarEstudiante(int id, String nombre, int edad) {
        repositorio.actualizar(id, nombre, edad);
    }

    public List<Estudiante> obtenerTodos() {
        return repositorio.obtenerTodos();
    }

    public List<Estudiante> obtenerConDescuento() {
        List<Estudiante> filtrados = new ArrayList<>();
        for (Estudiante e : repositorio) {
            if (e instanceof EstudianteDecorator) {
                filtrados.add(e);
            }
        }
        return filtrados;
    }
}
