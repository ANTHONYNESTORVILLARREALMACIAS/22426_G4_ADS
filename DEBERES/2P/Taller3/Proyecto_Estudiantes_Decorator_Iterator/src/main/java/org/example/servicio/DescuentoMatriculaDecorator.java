package org.example.servicio;

import org.example.modelo.Estudiante;

public class DescuentoMatriculaDecorator extends EstudianteDecorator {
    private final double porcentaje;

    public DescuentoMatriculaDecorator(Estudiante estudiante, double porcentaje) {
        super(estudiante);
        this.porcentaje = porcentaje;
    }

    @Override
    public String getInfo() {
        return estudianteBase.getInfo() + String.format(" | Descuento: %.0f%%", porcentaje);
    }
}
