package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.MateriaPrima;
import com.panaderia.ruminahui.model.MateriaPrimaRepository;
import com.panaderia.ruminahui.model.SeccionRepository;
import com.panaderia.ruminahui.util.Validator;

import java.util.List;
import java.util.UUID;

public class MateriaPrimaController {
    private final MateriaPrimaRepository materiaPrimaRepository;
    private final SeccionRepository seccionRepository;

    public MateriaPrimaController(MateriaPrimaRepository materiaPrimaRepository, SeccionRepository seccionRepository) {
        this.materiaPrimaRepository = materiaPrimaRepository;
        this.seccionRepository = seccionRepository;
    }

    public String createMateriaPrima(String nombre, String descripcion, double stockMinimo, String unidadMedida, String idSeccion) {
        if (!Validator.isValidMateriaPrimaNombre(nombre)) {
            return "El nombre de la materia prima debe tener al menos 2 caracteres.";
        }
        if (!Validator.isValidMateriaPrimaStockMinimo(stockMinimo)) {
            return "El stock mínimo debe ser mayor o igual a 0.";
        }
        if (!Validator.isValidMateriaPrimaUnidadMedida(unidadMedida)) {
            return "La unidad de medida no puede estar vacía.";
        }
        if (seccionRepository.findById(idSeccion) == null) {
            return "Sección no encontrada.";
        }

        MateriaPrima materia = new MateriaPrima(UUID.randomUUID().toString(), nombre, descripcion, stockMinimo, unidadMedida, idSeccion);
        materiaPrimaRepository.save(materia);
        return null; // Success
    }

    public List<MateriaPrima> getAllMateriasPrimas() {
        return materiaPrimaRepository.findAll();
    }

    public String updateMateriaPrima(String id, String nombre, String descripcion, double stockMinimo, String unidadMedida, String idSeccion) {
        if (!Validator.isValidMateriaPrimaNombre(nombre)) {
            return "El nombre de la materia prima debe tener al menos 2 caracteres.";
        }
        if (!Validator.isValidMateriaPrimaStockMinimo(stockMinimo)) {
            return "El stock mínimo debe ser mayor o igual a 0.";
        }
        if (!Validator.isValidMateriaPrimaUnidadMedida(unidadMedida)) {
            return "La unidad de medida no puede estar vacía.";
        }
        if (seccionRepository.findById(idSeccion) == null) {
            return "Sección no encontrada.";
        }

        MateriaPrima materia = materiaPrimaRepository.findById(id);
        if (materia == null) {
            return "Materia prima no encontrada.";
        }

        materia.setNombre(nombre);
        materia.setDescripcion(descripcion);
        materia.setStockMinimo(stockMinimo);
        materia.setUnidadMedida(unidadMedida);
        materia.setIdSeccion(idSeccion);
        materiaPrimaRepository.update(materia);
        return null; // Success
    }

    public String deleteMateriaPrima(String id) {
        MateriaPrima materia = materiaPrimaRepository.findById(id);
        if (materia == null) {
            return "Materia prima no encontrada.";
        }
        materiaPrimaRepository.delete(id);
        return null; // Success
    }
}