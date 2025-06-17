package org.example.servicio;

import org.example.modelo.Estudiante;

public abstract class EstudianteDecorator extends Estudiante {
    protected Estudiante estudianteBase;

    public EstudianteDecorator(Estudiante estudianteBase) {
        super(estudianteBase.getId(), estudianteBase.getNombre(), estudianteBase.getEdad());
        this.estudianteBase = estudianteBase;
    }

    @Override
    public String getInfo() {
        return estudianteBase.getInfo();
    }
}
