package org.example.modelo;
/**
 * Clase que representa un estudiante con sus atributos básicos.
 */
public class Estudiante {
    private int orden;
    private String nombre;
    private int edad;

    /**
     * Constructor de la clase Estudiante.
     * @param orden Número de orden del estudiante.
     * @param nombre Nombre del estudiante.
     * @param edad Edad del estudiante.
     */
    public Estudiante(int orden, String nombre, int edad) {
        this.orden = orden;
        this.nombre = nombre;
        this.edad = edad;
    }

    /**
     * Obtiene el número de orden del estudiante.
     * @return El número de orden.
     */
    public int getOrden() {
        return orden;
    }

    /**
     * Establece el número de orden del estudiante.
     * @param orden El número de orden a establecer.
     */
    public void setOrden(int orden) {
        this.orden = orden;
    }

    /**
     * Obtiene el nombre del estudiante.
     * @return El nombre del estudiante.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del estudiante.
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la edad del estudiante.
     * @return La edad del estudiante.
     */
    public int getEdad() {
        return edad;
    }

    /**
     * Establece la edad del estudiante.
     * @param edad La edad a establecer.
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * Representación en cadena del estudiante.
     * @return Una cadena con los datos del estudiante.
     */
    @Override
    public String toString() {
        return "Estudiante{orden=" + orden + ", nombre='" + nombre + "', edad=" + edad + "}";
    }
}