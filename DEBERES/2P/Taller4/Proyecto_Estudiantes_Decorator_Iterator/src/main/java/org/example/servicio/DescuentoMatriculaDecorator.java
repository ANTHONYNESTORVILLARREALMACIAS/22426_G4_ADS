package org.example.servicio;

import org.example.modelo.Estudiante;

/**
 * Clase que implementa el patrón Decorator para agregar un descuento a un estudiante.
 * Extiende EstudianteDecorator para heredar la funcionalidad base y añade información
 * sobre un porcentaje de descuento en la matrícula.
 */
public class DescuentoMatriculaDecorator extends EstudianteDecorator {
    // Porcentaje de descuento aplicado al estudiante
    private final double porcentaje;

    /**
     * Constructor que inicializa el decorador con un estudiante base y un porcentaje de descuento.
     * @param estudiante El estudiante al que se le aplica el descuento.
     * @param porcentaje El porcentaje de descuento a aplicar.
     */
    public DescuentoMatriculaDecorator(Estudiante estudiante, double porcentaje) {
        super(estudiante);
        this.porcentaje = porcentaje;
    }

    /**
     * Sobrescribe el método getInfo para incluir información del estudiante base
     * y el porcentaje de descuento aplicado.
     * @return Una cadena con la información del estudiante y el descuento.
     */
    @Override
    public String getInfo() {
        return estudianteBase.getInfo() + String.format(" | Descuento: %.0f%%", porcentaje);
    }
}