package org.example.model;

/**
 * Clase modelo que representa la entidad Estudiante.
 * Solo contiene datos y sus operaciones básicas (getters/setters).
 * Cumple con SRP: única responsabilidad de representar datos de un estudiante.
 */
public class Estudiante {
    private final int id;
    private String nombre;

    public Estudiante(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    // Setter solo para campos que pueden cambiar
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}