package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.DetalleReceta;
import com.panaderia.ruminahui.model.DetalleRecetaRepository;
import com.panaderia.ruminahui.model.MateriaPrimaRepository;
import com.panaderia.ruminahui.model.RecetaRepository;
import com.panaderia.ruminahui.util.IDGenerator;
import com.panaderia.ruminahui.util.Validator;

import java.util.List;

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
        if (recetaRepository.findById(idReceta) == null) {
            return "Receta no encontrada.";
        }
        if (materiaPrimaRepository.findById(idMateria) == null) {
            return "Materia prima no encontrada.";
        }
        if (cantidad <= 0) {
            return "La cantidad debe ser mayor a 0.";
        }
        if (!Validator.isValidMateriaPrimaUnidadMedida(unidadMedida)) {
            return "La unidad de medida no puede estar vacía.";
        }
        DetalleReceta detalle = new DetalleReceta(IDGenerator.generateID("detalles_receta"), idReceta, idMateria, cantidad, unidadMedida);
        detalleRecetaRepository.save(detalle);
        return null;
    }

    public List<DetalleReceta> getAllDetallesReceta() {
        return detalleRecetaRepository.findAll();
    }

    public String updateDetalleReceta(String id, String idReceta, String idMateria, double cantidad, String unidadMedida) {
        if (recetaRepository.findById(idReceta) == null) {
            return "Receta no encontrada.";
        }
        if (materiaPrimaRepository.findById(idMateria) == null) {
            return "Materia prima no encontrada.";
        }
        if (cantidad <= 0) {
            return "La cantidad debe ser mayor a 0.";
        }
        if (!Validator.isValidMateriaPrimaUnidadMedida(unidadMedida)) {
            return "La unidad de medida no puede estar vacía.";
        }
        DetalleReceta detalle = detalleRecetaRepository.findById(id);
        if (detalle == null) {
            return "Detalle de receta no encontrado.";
        }
        detalleRecetaRepository.update(new DetalleReceta(id, idReceta, idMateria, cantidad, unidadMedida));
        return null;
    }

    public String deleteDetalleReceta(String id) {
        DetalleReceta detalle = detalleRecetaRepository.findById(id);
        if (detalle == null) {
            return "Detalle de receta no encontrado.";
        }
        detalleRecetaRepository.delete(id);
        return null;
    }
}