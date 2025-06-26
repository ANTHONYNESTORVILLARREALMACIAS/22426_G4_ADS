package org.example.servicio;

import org.example.modelo.Estudiante;

/**
 * Clase abstracta que sirve como base para los decoradores de estudiantes.
 * Implementa el patrón Decorator, permitiendo extender la funcionalidad de un estudiante
 * sin modificar su clase base.
 */
public abstract class EstudianteDecorator extends Estudiante {
    // Referencia al estudiante base que se va a decorar
    protected Estudiante estudianteBase;

    /**
     * Constructor que inicializa el decorador con un estudiante base.
     * @param estudianteBase El estudiante a decorar.
     */
    public EstudianteDecorator(Estudiante estudianteBase) {
        super(estudianteBase.getId(), estudianteBase.getNombre(), estudianteBase.getEdad());
        this.estudianteBase = estudianteBase;
    }

    /**
     * Método abstracto que debe ser implementado por los decoradores concretos.
     * Por defecto, delega la llamada al estudiante base.
     * @return La información del estudiante base.
     */
    @Override
    public String getInfo() {
        return estudianteBase.getInfo();
    }
}