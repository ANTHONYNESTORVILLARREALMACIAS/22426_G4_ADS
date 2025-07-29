package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.MateriaPrima;
import com.panaderia.ruminahui.model.MateriaPrimaRepository;
import com.panaderia.ruminahui.model.Stock;
import com.panaderia.ruminahui.model.StockRepository;
import com.panaderia.ruminahui.util.IDGenerator;
import com.panaderia.ruminahui.util.Validator;

import java.util.List;

public class StockController {
    private final StockRepository stockRepository;
    private final MateriaPrimaRepository materiaPrimaRepository;

    public StockController(StockRepository stockRepository, MateriaPrimaRepository materiaPrimaRepository) {
        this.stockRepository = stockRepository;
        this.materiaPrimaRepository = materiaPrimaRepository;
    }

    public String createStock(String idMateria, double cantidad, String unidadMedida, String fecha) {
        if (materiaPrimaRepository.findById(idMateria) == null) {
            return "Materia prima no encontrada.";
        }
        if (cantidad <= 0) {
            return "La cantidad debe ser mayor a 0.";
        }
        if (!Validator.isValidMateriaPrimaUnidadMedida(unidadMedida)) {
            return "La unidad de medida no puede estar vacía.";
        }
        Stock stock = new Stock(IDGenerator.generateID("stocks"), idMateria, cantidad, unidadMedida, fecha);
        stockRepository.save(stock);
        return null;
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public String updateStock(String id, String idMateria, double cantidad, String unidadMedida, String fecha) {
        if (materiaPrimaRepository.findById(idMateria) == null) {
            return "Materia prima no encontrada.";
        }
        if (cantidad <= 0) {
            return "La cantidad debe ser mayor a 0.";
        }
        if (!Validator.isValidMateriaPrimaUnidadMedida(unidadMedida)) {
            return "La unidad de medida no puede estar vacía.";
        }
        Stock stock = stockRepository.findById(id);
        if (stock == null) {
            return "Stock no encontrado.";
        }
        stockRepository.update(new Stock(id, idMateria, cantidad, unidadMedida, fecha));
        return null;
    }

    public String deleteStock(String id) {
        Stock stock = stockRepository.findById(id);
        if (stock == null) {
            return "Stock no encontrado.";
        }
        stockRepository.delete(id);
        return null;
    }

    public List<Stock> searchStocks(String query, String seccionId) {
        return stockRepository.searchStock(query, seccionId);
    }
}