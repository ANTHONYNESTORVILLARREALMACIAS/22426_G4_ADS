package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.DetalleReceta;
import com.panaderia.ruminahui.model.DetalleRecetaRepository;
import com.panaderia.ruminahui.model.MateriaPrimaRepository;
import com.panaderia.ruminahui.model.RecetaRepository;
import com.panaderia.ruminahui.util.Validator;

import java.util.List;
import java.util.UUID;

public class DetalleRecetaController {
    private final DetalleRecetaRepository detalleRecetaRepository;
    private final RecetaRepository recetaRepository;
    private final MateriaPrimaRepository materiaPrimaRepository;

    public DetalleRecetaController(DetalleRecetaRepository detalleRecetaRepository, RecetaRepository recetaRepository, MateriaPrimaRepository materiaPrimaRepository) {
        this.detalleRecetaRepository = detalleRecetaRepository;
        this.recetaRepository = recetaRepository;
        this.materiaPrimaRepository = materiaPrimaRepository;
    }

    public String createDetalleReceta(String idReceta, String idMateria, double cantidad, String unidadMedida) {
        if (!Validator.isValidDetalleRecetaCantidad(cantidad)) {
            return "La cantidad debe ser mayor a 0.";
        }
        if (!Validator.isValidDetalleRecetaUnidadMedida(unidadMedida)) {
            return "La unidad de medida no puede estar vacía.";
        }
        if (recetaRepository.findById(idReceta) == null) {
            return "Receta no encontrada.";
        }
        if (materiaPrimaRepository.findById(idMateria) == null) {
            return "Materia prima no encontrada.";
        }

        DetalleReceta detalle = new DetalleReceta(UUID.randomUUID().toString(), idReceta, idMateria, cantidad, unidadMedida);
        detalleRecetaRepository.save(detalle);
        return null; // Success
    }

    public List<DetalleReceta> getAllDetallesReceta() {
        return detalleRecetaRepository.findAll();
    }

    public String updateDetalleReceta(String id, String idReceta, String idMateria, double cantidad, String unidadMedida) {
        if (!Validator.isValidDetalleRecetaCantidad(cantidad)) {
            return "La cantidad debe ser mayor a 0.";
        }
        if (!Validator.isValidDetalleRecetaUnidadMedida(unidadMedida)) {
            return "La unidad de medida no puede estar vacía.";
        }
        if (recetaRepository.findById(idReceta) == null) {
            return "Receta no encontrada.";
        }
        if (materiaPrimaRepository.findById(idMateria) == null) {
            return "Materia prima no encontrada.";
        }

        DetalleReceta detalle = detalleRecetaRepository.findById(id);
        if (detalle == null) {
            return "Detalle de receta no encontrado.";
        }

        detalle.setIdReceta(idReceta);
        detalle.setIdMateria(idMateria);
        detalle.setCantidad(cantidad);
        detalle.setUnidadMedida(unidadMedida);
        detalleRecetaRepository.update(detalle);
        return null; // Success
    }

    public String deleteDetalleReceta(String id) {
        DetalleReceta detalle = detalleRecetaRepository.findById(id);
        if (detalle == null) {
            return "Detalle de receta no encontrado.";
        }
        detalleRecetaRepository.delete(id);
        return null; // Success
    }
}