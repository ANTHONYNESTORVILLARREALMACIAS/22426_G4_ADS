package org.example.model;

/**
 * Clase que representa la entidad Estudiante con sus atributos y métodos.
 * Modela un estudiante con ID, apellidos, nombres y edad.
 */
public class Estudiante {
    private int id;          // Identificador único del estudiante
    private String apellidos; // Apellidos del estudiante
    private String nombres;  // Nombres del estudiante
    private int edad;        // Edad del estudiante

    /**
     * Constructor para inicializar un objeto Estudiante.
     * @param id Identificador único del estudiante
     * @param apellidos Apellidos del estudiante
     * @param nombres Nombres del estudiante
     * @param edad Edad del estudiante
     */

    public Estudiante(int id, String apellidos, String nombres, int edad) {
        this.id = id;
        this.apellidos = apellidos;
        this.nombres = nombres;
        this.edad = edad;
    }
    /**
     * Obtiene el ID del estudiante.
     * @return ID del estudiante
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del estudiante.
     * @param id Nuevo ID del estudiante
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene los apellidos del estudiante.
     * @return Apellidos del estudiante
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos del estudiante.
     * @param apellidos Nuevos apellidos del estudiante
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Obtiene los nombres del estudiante.
     * @return Nombres del estudiante
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * Establece los nombres del estudiante.
     * @param nombres Nuevos nombres del estudiante
     */
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    /**
     * Obtiene la edad del estudiante.
     * @return Edad del estudiante
     */
    public int getEdad() {
        return edad;
    }

    /**
     * Establece la edad del estudiante.
     * @param edad Nueva edad del estudiante
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }
}