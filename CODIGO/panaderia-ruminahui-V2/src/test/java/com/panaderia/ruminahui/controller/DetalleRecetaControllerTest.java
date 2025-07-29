package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DetalleRecetaControllerTest {

    @Mock
    private DetalleRecetaRepository detalleRecetaRepository;

    @Mock
    private RecetaRepository recetaRepository;

    @Mock
    private MateriaPrimaRepository materiaPrimaRepository;

    @InjectMocks
    private DetalleRecetaController detalleRecetaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createDetalleReceta_Success() {
        // Arrange
        String idReceta = "rec1";
        String idMateria = "mat1";
        double cantidad = 2.5;
        String unidadMedida = "kg";

        when(recetaRepository.findById(idReceta)).thenReturn(new Receta());
        when(materiaPrimaRepository.findById(idMateria)).thenReturn(new MateriaPrima());

        // Act
        String result = detalleRecetaController.createDetalleReceta(idReceta, idMateria, cantidad, unidadMedida);

        // Assert
        assertNull(result);
        verify(detalleRecetaRepository, times(1)).save(any(DetalleReceta.class));
    }

    @Test
    void createDetalleReceta_RecetaNotFound() {
        // Arrange
        String idReceta = "rec1";
        String idMateria = "mat1";
        double cantidad = 2.5;
        String unidadMedida = "kg";

        when(recetaRepository.findById(idReceta)).thenReturn(null);

        // Act
        String result = detalleRecetaController.createDetalleReceta(idReceta, idMateria, cantidad, unidadMedida);

        // Assert
        assertEquals("Receta no encontrada.", result);
        verify(detalleRecetaRepository, never()).save(any(DetalleReceta.class));
    }

    @Test
    void createDetalleReceta_MateriaPrimaNotFound() {
        // Arrange
        String idReceta = "rec1";
        String idMateria = "mat1";
        double cantidad = 2.5;
        String unidadMedida = "kg";

        when(recetaRepository.findById(idReceta)).thenReturn(new Receta());
        when(materiaPrimaRepository.findById(idMateria)).thenReturn(null);

        // Act
        String result = detalleRecetaController.createDetalleReceta(idReceta, idMateria, cantidad, unidadMedida);

        // Assert
        assertEquals("Materia prima no encontrada.", result);
        verify(detalleRecetaRepository, never()).save(any(DetalleReceta.class));
    }

    // ... (resto de los métodos de prueba permanecen igual)
}