package org.example.modelo;

public class Estudiante {
    protected int id;
    protected String nombre;
    protected int edad;

    public Estudiante(int id, String nombre, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getInfo() {
        return String.format("ID: %d | Nombre: %s | Edad: %d", id, nombre, edad);
    }
}
