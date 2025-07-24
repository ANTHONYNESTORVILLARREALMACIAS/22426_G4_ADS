package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.MateriaPrimaRepository;
import com.panaderia.ruminahui.model.Stock;
import com.panaderia.ruminahui.model.StockRepository;
import com.panaderia.ruminahui.util.Validator;

import java.util.List;
import java.util.UUID;

public class StockController {
    private final StockRepository stockRepository;
    private final MateriaPrimaRepository materiaPrimaRepository;

    public StockController(StockRepository stockRepository, MateriaPrimaRepository materiaPrimaRepository) {
        this.stockRepository = stockRepository;
        this.materiaPrimaRepository = materiaPrimaRepository;
    }

    public String createStock(String idMateria, double cantidad, String fecha) {
        if (!Validator.isValidStockCantidad(cantidad)) {
            return "La cantidad debe ser mayor o igual a 0.";
        }
        if (!Validator.isValidFecha(fecha)) {
            return "La fecha debe estar en formato AAAA-MM-DD.";
        }
        if (materiaPrimaRepository.findById(idMateria) == null) {
            return "Materia prima no encontrada.";
        }

        Stock stock = new Stock(UUID.randomUUID().toString(), idMateria, cantidad, fecha);
        stockRepository.save(stock);
        return null; // Success
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public String updateStock(String id, String idMateria, double cantidad, String fecha) {
        if (!Validator.isValidStockCantidad(cantidad)) {
            return "La cantidad debe ser mayor o igual a 0.";
        }
        if (!Validator.isValidFecha(fecha)) {
            return "La fecha debe estar en formato AAAA-MM-DD.";
        }
        if (materiaPrimaRepository.findById(idMateria) == null) {
            return "Materia prima no encontrada.";
        }

        Stock stock = stockRepository.findById(id);
        if (stock == null) {
            return "Stock no encontrado.";
        }

        stock.setIdMateria(idMateria);
        stock.setCantidad(cantidad);
        stock.setFecha(fecha);
        stockRepository.update(stock);
        return null; // Success
    }

    public String deleteStock(String id) {
        Stock stock = stockRepository.findById(id);
        if (stock == null) {
            return "Stock no encontrado.";
        }
        stockRepository.delete(id);
        return null; // Success
    }
}