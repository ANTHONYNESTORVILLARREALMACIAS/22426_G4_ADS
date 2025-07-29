package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.Seccion;
import com.panaderia.ruminahui.model.SeccionRepository;
import com.panaderia.ruminahui.util.IDGenerator;

import java.util.List;

public class SeccionController {
    private final SeccionRepository seccionRepository;

    public SeccionController(SeccionRepository seccionRepository) {
        this.seccionRepository = seccionRepository;
    }

    public String createSeccion(Seccion seccion) {
        if (seccion.getNombre() == null || seccion.getNombre().isEmpty()) {
            return "El nombre de la sección es requerido.";
        }
        // Generate ID for new section
        Seccion newSeccion = new Seccion(
                IDGenerator.generateID("secciones"),
                seccion.getNombre(),
                seccion.getDescripcion()
        );
        return seccionRepository.create(newSeccion);
    }

    public String updateSeccion(Seccion seccion) {
        if (seccion.getNombre() == null || seccion.getNombre().isEmpty()) {
            return "El nombre de la sección es requerido.";
        }
        return seccionRepository.update(seccion);
    }

    public String deleteSeccion(String id) {
        if (id == null || id.isEmpty()) {
            return "El ID de la sección es requerido.";
        }
        return seccionRepository.delete(id);
    }

    public Seccion getSeccionById(String id) {
        return seccionRepository.findById(id);
    }

    public List<Seccion> getAllSecciones() {
        return seccionRepository.findAll();
    }

    public List<Seccion> searchSecciones(String searchText) {
        return seccionRepository.findByName(searchText);
    }
}