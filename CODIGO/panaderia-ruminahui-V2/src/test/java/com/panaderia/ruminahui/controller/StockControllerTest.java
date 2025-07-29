package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.MateriaPrima;
import com.panaderia.ruminahui.model.MateriaPrimaRepository;
import com.panaderia.ruminahui.model.Stock;
import com.panaderia.ruminahui.model.StockRepository;
import com.panaderia.ruminahui.util.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockControllerTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private MateriaPrimaRepository materiaPrimaRepository;

    @InjectMocks
    private StockController stockController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createStock_Success() {
        // Arrange
        String idMateria = "mat1";
        double cantidad = 10.5;
        String unidadMedida = "kg";
        String fecha = "2023-05-20";

        when(materiaPrimaRepository.findById(idMateria)).thenReturn(new MateriaPrima());

        // Act
        String result = stockController.createStock(idMateria, cantidad, unidadMedida, fecha);

        // Assert
        assertNull(result);
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    void createStock_MateriaPrimaNotFound() {
        // Arrange
        String idMateria = "mat1";
        double cantidad = 10.5;
        String unidadMedida = "kg";
        String fecha = "2023-05-20";

        when(materiaPrimaRepository.findById(idMateria)).thenReturn(null);

        // Act
        String result = stockController.createStock(idMateria, cantidad, unidadMedida, fecha);

        // Assert
        assertEquals("Materia prima no encontrada.", result);
        verify(stockRepository, never()).save(any(Stock.class));
    }

    @Test
    void createStock_InvalidCantidad() {
        // Arrange
        String idMateria = "mat1";
        double cantidad = 0;
        String unidadMedida = "kg";
        String fecha = "2023-05-20";

        // Act
        String result = stockController.createStock(idMateria, cantidad, unidadMedida, fecha);

        // Assert
        assertEquals("La cantidad debe ser mayor a 0.", result);
        verify(stockRepository, never()).save(any(Stock.class));
    }

    @Test
    void createStock_EmptyUnidadMedida() {
        // Arrange
        String idMateria = "mat1";
        double cantidad = 10.5;
        String unidadMedida = "";
        String fecha = "2023-05-20";

        // Act
        String result = stockController.createStock(idMateria, cantidad, unidadMedida, fecha);

        // Assert
        assertEquals("La unidad de medida no puede estar vacía.", result);
        verify(stockRepository, never()).save(any(Stock.class));
    }

    @Test
    void getAllStocks_Success() {
        // Arrange
        Stock s1 = new Stock("1", "mat1", 10.0, "kg", "2023-05-15");
        Stock s2 = new Stock("2", "mat2", 5.0, "l", "2023-05-16");
        List<Stock> expected = Arrays.asList(s1, s2);

        when(stockRepository.findAll()).thenReturn(expected);

        // Act
        List<Stock> result = stockController.getAllStocks();

        // Assert
        assertEquals(2, result.size());
        assertEquals(expected, result);
    }

    @Test
    void updateStock_Success() {
        // Arrange
        String id = "1";
        String idMateria = "mat1";
        double cantidad = 15.0;
        String unidadMedida = "kg";
        String fecha = "2023-05-20";

        Stock existing = new Stock(id, "matOld", 10.0, "l", "2023-05-15");

        when(materiaPrimaRepository.findById(idMateria)).thenReturn(new MateriaPrima());
        when(stockRepository.findById(id)).thenReturn(existing);

        // Act
        String result = stockController.updateStock(id, idMateria, cantidad, unidadMedida, fecha);

        // Assert
        assertNull(result);
        verify(stockRepository, times(1)).update(any(Stock.class));
    }

    @Test
    void updateStock_StockNotFound() {
        // Arrange
        String id = "1";
        String idMateria = "mat1";
        double cantidad = 15.0;
        String unidadMedida = "kg";
        String fecha = "2023-05-20";

        when(stockRepository.findById(id)).thenReturn(null);

        // Act
        String result = stockController.updateStock(id, idMateria, cantidad, unidadMedida, fecha);

        // Assert
        assertEquals("Stock no encontrado.", result);
        verify(stockRepository, never()).update(any(Stock.class));
    }

    @Test
    void deleteStock_Success() {
        // Arrange
        String id = "1";
        Stock existing = new Stock(id, "mat1", 10.0, "kg", "2023-05-15");

        when(stockRepository.findById(id)).thenReturn(existing);

        // Act
        String result = stockController.deleteStock(id);

        // Assert
        assertNull(result);
        verify(stockRepository, times(1)).delete(id);
    }

    @Test
    void deleteStock_NotFound() {
        // Arrange
        String id = "1";

        when(stockRepository.findById(id)).thenReturn(null);

        // Act
        String result = stockController.deleteStock(id);

        // Assert
        assertEquals("Stock no encontrado.", result);
        verify(stockRepository, never()).delete(id);
    }

    @Test
    void searchStocks_Success() {
        // Arrange
        String query = "harina";
        String seccionId = "sec1";
        Stock s1 = new Stock("1", "mat1", 10.0, "kg", "2023-05-15");
        List<Stock> expected = Arrays.asList(s1);

        when(stockRepository.searchStock(query, seccionId)).thenReturn(expected);

        // Act
        List<Stock> result = stockController.searchStocks(query, seccionId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(expected, result);
    }
}