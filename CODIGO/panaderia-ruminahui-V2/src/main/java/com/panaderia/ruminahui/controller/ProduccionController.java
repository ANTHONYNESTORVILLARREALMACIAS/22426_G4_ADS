package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.Produccion;
import com.panaderia.ruminahui.model.ProduccionRepository;
import com.panaderia.ruminahui.model.RecetaRepository;
import com.panaderia.ruminahui.util.IDGenerator;

import java.util.List;

public class ProduccionController {
    private final ProduccionRepository produccionRepository;
    private final RecetaRepository recetaRepository;

    public ProduccionController(ProduccionRepository produccionRepository, RecetaRepository recetaRepository) {
        this.produccionRepository = produccionRepository;
        this.recetaRepository = recetaRepository;
    }

    public String createProduccion(String idReceta, double cantidad, String fecha) {
        if (recetaRepository.findById(idReceta) == null) {
            return "Receta no encontrada.";
        }
        if (cantidad <= 0) {
            return "La cantidad debe ser mayor a 0.";
        }
        Produccion produccion = new Produccion(IDGenerator.generateID("producciones"), idReceta, cantidad, fecha);
        produccionRepository.save(produccion);
        return null;
    }

    public List<Produccion> getAllProducciones() {
        return produccionRepository.findAll();
    }

    public String updateProduccion(String id, String idReceta, double cantidad, String fecha) {
        if (recetaRepository.findById(idReceta) == null) {
            return "Receta no encontrada.";
        }
        if (cantidad <= 0) {
            return "La cantidad debe ser mayor a 0.";
        }
        Produccion produccion = produccionRepository.findById(id);
        if (produccion == null) {
            return "Producción no encontrada.";
        }
        produccionRepository.update(new Produccion(id, idReceta, cantidad, fecha));
        return null;
    }

    public String deleteProduccion(String id) {
        Produccion produccion = produccionRepository.findById(id);
        if (produccion == null) {
            return "Producción no encontrada.";
        }
        produccionRepository.delete(id);
        return null;
    }
}