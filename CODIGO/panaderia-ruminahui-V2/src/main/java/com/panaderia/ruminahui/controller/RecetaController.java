package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.Receta;
import com.panaderia.ruminahui.model.RecetaRepository;
import com.panaderia.ruminahui.util.IDGenerator;
import com.panaderia.ruminahui.util.Validator;

import java.util.List;

public class RecetaController {
    private final RecetaRepository recetaRepository;

    public RecetaController(RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    public String createReceta(String nombre, String descripcion) {
        if (!Validator.isValidMateriaPrimaNombre(nombre)) {
            return "El nombre de la receta debe tener al menos 2 caracteres.";
        }
        Receta receta = new Receta(IDGenerator.generateID("recetas"), nombre, descripcion);
        recetaRepository.save(receta);
        return null;
    }

    public List<Receta> getAllRecetas() {
        return recetaRepository.findAll();
    }

    public String updateReceta(String id, String nombre, String descripcion) {
        if (!Validator.isValidMateriaPrimaNombre(nombre)) {
            return "El nombre de la receta debe tener al menos 2 caracteres.";
        }
        Receta receta = recetaRepository.findById(id);
        if (receta == null) {
            return "Receta no encontrada.";
        }
        receta.setNombre(nombre);
        receta.setDescripcion(descripcion);
        recetaRepository.update(receta);
        return null;
    }

    public String deleteReceta(String id) {
        Receta receta = recetaRepository.findById(id);
        if (receta == null) {
            return "Receta no encontrada.";
        }
        recetaRepository.delete(id);
        return null;
    }
}