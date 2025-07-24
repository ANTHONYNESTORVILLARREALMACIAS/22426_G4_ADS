package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.Receta;
import com.panaderia.ruminahui.model.RecetaRepository;
import com.panaderia.ruminahui.util.Validator;

import java.util.List;
import java.util.UUID;

public class RecetaController {
    private final RecetaRepository recetaRepository;

    public RecetaController(RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    public String createReceta(String nombre, String descripcion) {
        if (!Validator.isValidRecetaNombre(nombre)) {
            return "El nombre de la receta debe tener al menos 2 caracteres.";
        }
        if (!Validator.isValidRecetaDescripcion(descripcion)) {
            return "La descripción no puede estar vacía.";
        }

        Receta receta = new Receta(UUID.randomUUID().toString(), nombre, descripcion);
        recetaRepository.save(receta);
        return null; // Success
    }

    public List<Receta> getAllRecetas() {
        return recetaRepository.findAll();
    }

    public String updateReceta(String id, String nombre, String descripcion) {
        if (!Validator.isValidRecetaNombre(nombre)) {
            return "El nombre de la receta debe tener al menos 2 caracteres.";
        }
        if (!Validator.isValidRecetaDescripcion(descripcion)) {
            return "La descripción no puede estar vacía.";
        }

        Receta receta = recetaRepository.findById(id);
        if (receta == null) {
            return "Receta no encontrada.";
        }

        receta.setNombre(nombre);
        receta.setDescripcion(descripcion);
        recetaRepository.update(receta);
        return null; // Success
    }

    public String deleteReceta(String id) {
        Receta receta = recetaRepository.findById(id);
        if (receta == null) {
            return "Receta no encontrada.";
        }
        recetaRepository.delete(id);
        return null; // Success
    }
}