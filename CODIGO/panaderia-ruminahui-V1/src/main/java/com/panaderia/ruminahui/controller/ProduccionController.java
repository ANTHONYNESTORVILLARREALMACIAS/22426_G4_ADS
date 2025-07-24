package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.Produccion;
import com.panaderia.ruminahui.model.ProduccionRepository;
import com.panaderia.ruminahui.model.RecetaRepository;
import com.panaderia.ruminahui.util.Validator;

import java.util.List;
import java.util.UUID;

public class ProduccionController {
    private final ProduccionRepository produccionRepository;
    private final RecetaRepository recetaRepository;

    public ProduccionController(ProduccionRepository produccionRepository, RecetaRepository recetaRepository) {
        this.produccionRepository = produccionRepository;
        this.recetaRepository = recetaRepository;
    }

    public String createProduccion(String idReceta, double cantidad, String fecha) {
        if (!Validator.isValidProduccionCantidad(cantidad)) {
            return "La cantidad debe ser mayor a 0.";
        }
        if (!Validator.isValidFecha(fecha)) {
            return "La fecha debe estar en formato AAAA-MM-DD.";
        }
        if (recetaRepository.findById(idReceta) == null) {
            return "Receta no encontrada.";
        }

        Produccion produccion = new Produccion(UUID.randomUUID().toString(), idReceta, cantidad, fecha);
        produccionRepository.save(produccion);
        return null; // Success
    }

    public List<Produccion> getAllProducciones() {
        return produccionRepository.findAll();
    }

    public String updateProduccion(String id, String idReceta, double cantidad, String fecha) {
        if (!Validator.isValidProduccionCantidad(cantidad)) {
            return "La cantidad debe ser mayor a 0.";
        }
        if (!Validator.isValidFecha(fecha)) {
            return "La fecha debe estar en formato AAAA-MM-DD.";
        }
        if (recetaRepository.findById(idReceta) == null) {
            return "Receta no encontrada.";
        }

        Produccion produccion = produccionRepository.findById(id);
        if (produccion == null) {
            return "Producción no encontrada.";
        }

        produccion.setIdReceta(idReceta);
        produccion.setCantidad(cantidad);
        produccion.setFecha(fecha);
        produccionRepository.update(produccion);
        return null; // Success
    }

    public String deleteProduccion(String id) {
        Produccion produccion = produccionRepository.findById(id);
        if (produccion == null) {
            return "Producción no encontrada.";
        }
        produccionRepository.delete(id);
        return null; // Success
    }
}