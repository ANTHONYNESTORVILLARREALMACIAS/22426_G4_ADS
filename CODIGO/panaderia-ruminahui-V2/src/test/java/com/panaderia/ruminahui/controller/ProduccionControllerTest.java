package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.Produccion;
import com.panaderia.ruminahui.model.ProduccionRepository;
import com.panaderia.ruminahui.model.RecetaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProduccionControllerTest {

    @Mock
    private ProduccionRepository produccionRepository;

    @Mock
    private RecetaRepository recetaRepository;

    @InjectMocks
    private ProduccionController produccionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduccion_Success() {
        // Arrange
        String idReceta = "rec1";
        double cantidad = 10.0;
        String fecha = "2023-05-15";

        when(recetaRepository.findById(idReceta)).thenReturn(new Object());

        // Act
        String result = produccionController.createProduccion(idReceta, cantidad, fecha);

        // Assert
        assertNull(result);
        verify(produccionRepository, times(1)).save(any(Produccion.class));
    }

    @Test
    void createProduccion_RecetaNotFound() {
        // Arrange
        String idReceta = "rec1";
        double cantidad = 10.0;
        String fecha = "2023-05-15";

        when(recetaRepository.findById(idReceta)).thenReturn(null);

        // Act
        String result = produccionController.createProduccion(idReceta, cantidad, fecha);

        // Assert
        assertEquals("Receta no encontrada.", result);
        verify(produccionRepository, never()).save(any(Produccion.class));
    }

    @Test
    void createProduccion_InvalidCantidad() {
        // Arrange
        String idReceta = "rec1";
        double cantidad = 0; // Cantidad inválida
        String fecha = "2023-05-15";

        // Act
        String result = produccionController.createProduccion(idReceta, cantidad, fecha);

        // Assert
        assertEquals("La cantidad debe ser mayor a 0.", result);
        verify(produccionRepository, never()).save(any(Produccion.class));
    }

    @Test
    void getAllProducciones_Success() {
        // Arrange
        Produccion p1 = new Produccion("1", "rec1", 5.0, "2023-05-10");
        Produccion p2 = new Produccion("2", "rec2", 8.0, "2023-05-11");
        List<Produccion> expected = Arrays.asList(p1, p2);

        when(produccionRepository.findAll()).thenReturn(expected);

        // Act
        List<Produccion> result = produccionController.getAllProducciones();

        // Assert
        assertEquals(2, result.size());
        assertEquals(expected, result);
    }

    @Test
    void updateProduccion_Success() {
        // Arrange
        String id = "1";
        String idReceta = "rec1";
        double cantidad = 15.0;
        String fecha = "2023-05-20";

        Produccion existing = new Produccion(id, "recOld", 10.0, "2023-05-15");

        when(recetaRepository.findById(idReceta)).thenReturn(new Object());
        when(produccionRepository.findById(id)).thenReturn(existing);

        // Act
        String result = produccionController.updateProduccion(id, idReceta, cantidad, fecha);

        // Assert
        assertNull(result);
        verify(produccionRepository, times(1)).update(any(Produccion.class));
    }

    @Test
    void updateProduccion_RecetaNotFound() {
        // Arrange
        String id = "1";
        String idReceta = "rec1";
        double cantidad = 15.0;
        String fecha = "2023-05-20";

        when(recetaRepository.findById(idReceta)).thenReturn(null);

        // Act
        String result = produccionController.updateProduccion(id, idReceta, cantidad, fecha);

        // Assert
        assertEquals("Receta no encontrada.", result);
        verify(produccionRepository, never()).update(any(Produccion.class));
    }

    @Test
    void updateProduccion_ProduccionNotFound() {
        // Arrange
        String id = "1";
        String idReceta = "rec1";
        double cantidad = 15.0;
        String fecha = "2023-05-20";

        when(recetaRepository.findById(idReceta)).thenReturn(new Object());
        when(produccionRepository.findById(id)).thenReturn(null);

        // Act
        String result = produccionController.updateProduccion(id, idReceta, cantidad, fecha);

        // Assert
        assertEquals("Producción no encontrada.", result);
        verify(produccionRepository, never()).update(any(Produccion.class));
    }

    @Test
    void updateProduccion_InvalidCantidad() {
        // Arrange
        String id = "1";
        String idReceta = "rec1";
        double cantidad = -5.0; // Cantidad inválida
        String fecha = "2023-05-20";

        // Act
        String result = produccionController.updateProduccion(id, idReceta, cantidad, fecha);

        // Assert
        assertEquals("La cantidad debe ser mayor a 0.", result);
        verify(produccionRepository, never()).update(any(Produccion.class));
    }

    @Test
    void deleteProduccion_Success() {
        // Arrange
        String id = "1";
        Produccion existing = new Produccion(id, "rec1", 10.0, "2023-05-15");

        when(produccionRepository.findById(id)).thenReturn(existing);

        // Act
        String result = produccionController.deleteProduccion(id);

        // Assert
        assertNull(result);
        verify(produccionRepository, times(1)).delete(id);
    }

    @Test
    void deleteProduccion_NotFound() {
        // Arrange
        String id = "1";

        when(produccionRepository.findById(id)).thenReturn(null);

        // Act
        String result = produccionController.deleteProduccion(id);

        // Assert
        assertEquals("Producción no encontrada.", result);
        verify(produccionRepository, never()).delete(id);
    }
}