package org.example.repositorio;

import org.example.modelo.Estudiante;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Clase que actúa como repositorio para almacenar y gestionar estudiantes.
 * Implementa el patrón Repository y la interfaz Iterable para permitir
 * la iteración sobre la lista de estudiantes.
 */
public class EstudianteRepositorio implements Iterable<Estudiante> {
    // Lista que almacena los estudiantes
    private final List<Estudiante> estudiantes = new ArrayList<>();

    /**
     * Agrega un estudiante a la lista.
     * @param estudiante El estudiante a agregar.
     */
    public void agregar(Estudiante estudiante) {
        estudiantes.add(estudiante);
    }

    /**
     * Elimina un estudiante por su ID.
     * @param id El ID del estudiante a eliminar.
     */
    public void eliminar(int id) {
        estudiantes.removeIf(e -> e.getId() == id);
    }

    /**
     * Actualiza la información de un estudiante identificado por su ID.
     * @param id El ID del estudiante a actualizar.
     * @param nombre El nuevo nombre del estudiante.
     * @param edad La nueva edad del estudiante.
     */
    public void actualizar(int id, String nombre, int edad) {
        for (Estudiante e : estudiantes) {
            if (e.getId() == id) {
                e.setNombre(nombre);
                e.setEdad(edad);
                break;
            }
        }
    }

    /**
     * Obtiene todos los estudiantes almacenados.
     * @return Una lista de estudiantes.
     */
    public List<Estudiante> obtenerTodos() {
        return estudiantes;
    }

    /**
     * Proporciona un iterador para recorrer la lista de estudiantes.
     * @return Un iterador de estudiantes.
     */
    @Override
    public Iterator<Estudiante> iterator() {
        return estudiantes.iterator();
    }
}