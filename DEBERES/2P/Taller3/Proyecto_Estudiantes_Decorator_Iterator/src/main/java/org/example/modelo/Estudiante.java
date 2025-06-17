package org.example.modelo;

/**
 * Clase base que representa a un estudiante con atributos básicos.
 * Proporciona métodos para acceder y modificar los datos del estudiante.
 */
public class Estudiante {
    // Identificador único del estudiante
    protected int id;
    // Nombre del estudiante
    protected String nombre;
    // Edad del estudiante
    protected int edad;

    /**
     * Constructor que inicializa un estudiante con su ID, nombre y edad.
     * @param id Identificador único del estudiante.
     * @param nombre Nombre del estudiante.
     * @param edad Edad del estudiante.
     */
    public Estudiante(int id, String nombre, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
    }

    /**
     * Obtiene el ID del estudiante.
     * @return El ID del estudiante.
     */
    public int getId() { return id; }

    /**
     * Obtiene el nombre del estudiante.
     * @return El nombre del estudiante.
     */
    public String getNombre() { return nombre; }

    /**
     * Obtiene la edad del estudiante.
     * @return La edad del estudiante.
     */
    public int getEdad() { return edad; }

    /**
     * Establece el nombre del estudiante.
     * @param nombre El nuevo nombre del estudiante.
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Establece la edad del estudiante.
     * @param edad La nueva edad del estudiante.
     */
    public void setEdad(int edad) { this.edad = edad; }

    /**
     * Devuelve una representación en cadena de la información del estudiante.
     * @return Una cadena con el ID, nombre y edad del estudiante.
     */
    public String getInfo() {
        return String.format("ID: %d | Nombre: %s | Edad: %d", id, nombre, edad);
    }
}