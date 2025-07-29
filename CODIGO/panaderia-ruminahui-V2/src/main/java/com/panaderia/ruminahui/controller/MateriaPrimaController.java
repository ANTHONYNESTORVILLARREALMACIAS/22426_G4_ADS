package com.panaderia.ruminahui.controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.panaderia.ruminahui.util.MongoDBConfig;
import com.panaderia.ruminahui.model.MateriaPrima;
import com.panaderia.ruminahui.model.MateriaPrimaRepository;
import com.panaderia.ruminahui.model.SeccionRepository;
import com.panaderia.ruminahui.util.IDGenerator;
import com.panaderia.ruminahui.util.Validator;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class MateriaPrimaController {
    private final MateriaPrimaRepository materiaPrimaRepository;
    private final SeccionRepository seccionRepository;
    private final MongoDatabase database;

    public MateriaPrimaController(MateriaPrimaRepository materiaPrimaRepository, SeccionRepository seccionRepository) {
        this.materiaPrimaRepository = materiaPrimaRepository;
        this.seccionRepository = seccionRepository;
        this.database = MongoDBConfig.getDatabase();
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

        MateriaPrima materia = new MateriaPrima(IDGenerator.generateID("materias_primas"), nombre, descripcion, stockMinimo, unidadMedida, idSeccion);
        materiaPrimaRepository.save(materia);
        return null;
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
        return null;
    }

    public String deleteMateriaPrima(String id) {
        MateriaPrima materia = materiaPrimaRepository.findById(id);
        if (materia == null) {
            return "Materia prima no encontrada.";
        }
        materiaPrimaRepository.delete(id);
        return null;
    }

    public List<MateriaPrima> searchMateriasPrimas(String query, String seccionId) {
        MongoCollection<Document> collection = database.getCollection("materias_primas");
        Bson filter = Filters.and(
                query.isEmpty() ? Filters.empty() : Filters.regex("nombre", query, "i"),
                seccionId == null ? Filters.empty() : Filters.eq("id_seccion", seccionId)
        );
        return collection.find(filter).map(MateriaPrimaRepository::documentToMateriaPrima).into(new ArrayList<>());
    }
}