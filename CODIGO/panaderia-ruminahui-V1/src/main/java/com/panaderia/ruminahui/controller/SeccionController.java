package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.Seccion;
import com.panaderia.ruminahui.model.SeccionRepository;
import com.panaderia.ruminahui.util.Validator;

import java.util.List;
import java.util.UUID;

public class SeccionController {
    private final SeccionRepository seccionRepository;

    public SeccionController(SeccionRepository seccionRepository) {
        this.seccionRepository = seccionRepository;
    }

    public String createSeccion(String nombre, String descripcion) {
        if (!Validator.isValidSeccionNombre(nombre)) {
            return "El nombre de la sección debe tener al menos 2 caracteres.";
        }
        if (!Validator.isValidSeccionDescripcion(descripcion)) {
            return "La descripción no puede estar vacía.";
        }

        Seccion seccion = new Seccion(UUID.randomUUID().toString(), nombre, descripcion);
        seccionRepository.save(seccion);
        return null; // Success
    }

    public List<Seccion> getAllSecciones() {
        return seccionRepository.findAll();
    }

    public String updateSeccion(String id, String nombre, String descripcion) {
        if (!Validator.isValidSeccionNombre(nombre)) {
            return "El nombre de la sección debe tener al menos 2 caracteres.";
        }
        if (!Validator.isValidSeccionDescripcion(descripcion)) {
            return "La descripción no puede estar vacía.";
        }

        Seccion seccion = seccionRepository.findById(id);
        if (seccion == null) {
            return "Sección no encontrada.";
        }

        seccion.setNombre(nombre);
        seccion.setDescripcion(descripcion);
        seccionRepository.update(seccion);
        return null; // Success
    }

    public String deleteSeccion(String id) {
        Seccion seccion = seccionRepository.findById(id);
        if (seccion == null) {
            return "Sección no encontrada.";
        }
        seccionRepository.delete(id);
        return null; // Success
    }
}