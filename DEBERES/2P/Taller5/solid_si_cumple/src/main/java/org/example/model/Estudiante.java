package org.example.model;

/**
 * Clase modelo que representa la entidad Estudiante.
 * Solo contiene datos y sus operaciones básicas (getters/setters).
 * Cumple con SRP: única responsabilidad de representar datos de un estudiante.
 */

public class Estudiante {
    private int id;
    private String nombre;
    private String email;

    public Estudiante(int id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }

    public void setId(int id) {
        this.id = id;
    }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Estudiante [id=" + id + ", nombre=" + nombre + ", email=" + email + "]";
    }
}