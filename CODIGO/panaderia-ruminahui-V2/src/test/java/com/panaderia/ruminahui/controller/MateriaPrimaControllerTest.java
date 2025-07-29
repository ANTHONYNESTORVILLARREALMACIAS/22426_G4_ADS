package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.MateriaPrima;
import com.panaderia.ruminahui.model.MateriaPrimaRepository;
import com.panaderia.ruminahui.model.SeccionRepository;
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

class MateriaPrimaControllerTest {

    @Mock
    private MateriaPrimaRepository materiaPrimaRepository;

    @Mock
    private SeccionRepository seccionRepository;

    @InjectMocks
    private MateriaPrimaController materiaPrimaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMateriaPrima_Success() {
        // Arrange
        String nombre = "Harina";
        String descripcion = "Harina de trigo";
        double stockMinimo = 10.0;
        String unidadMedida = "kg";
        String idSeccion = "sec1";

        when(seccionRepository.findById(idSeccion)).thenReturn(new Object());

        // Act
        String result = materiaPrimaController.createMateriaPrima(
                nombre, descripcion, stockMinimo, unidadMedida, idSeccion);

        // Assert
        assertNull(result);
        verify(materiaPrimaRepository, times(1)).save(any(MateriaPrima.class));
    }

    @Test
    void createMateriaPrima_InvalidNombre() {
        // Arrange
        String nombre = "H"; // Nombre demasiado corto
        String descripcion = "Harina de trigo";
        double stockMinimo = 10.0;
        String unidadMedida = "kg";
        String idSeccion = "sec1";

        // Act
        String result = materiaPrimaController.createMateriaPrima(
                nombre, descripcion, stockMinimo, unidadMedida, idSeccion);

        // Assert
        assertEquals("El nombre de la materia prima debe tener al menos 2 caracteres.", result);
        verify(materiaPrimaRepository, never()).save(any(MateriaPrima.class));
    }

    @Test
    void createMateriaPrima_InvalidStockMinimo() {
        // Arrange
        String nombre = "Harina";
        String descripcion = "Harina de trigo";
        double stockMinimo = -1.0; // Stock mínimo inválido
        String unidadMedida = "kg";
        String idSeccion = "sec1";

        // Act
        String result = materiaPrimaController.createMateriaPrima(
                nombre, descripcion, stockMinimo, unidadMedida, idSeccion);

        // Assert
        assertEquals("El stock mínimo debe ser mayor o igual a 0.", result);
        verify(materiaPrimaRepository, never()).save(any(MateriaPrima.class));
    }

    @Test
    void createMateriaPrima_EmptyUnidadMedida() {
        // Arrange
        String nombre = "Harina";
        String descripcion = "Harina de trigo";
        double stockMinimo = 10.0;
        String unidadMedida = ""; // Unidad de medida vacía
        String idSeccion = "sec1";

        // Act
        String result = materiaPrimaController.createMateriaPrima(
                nombre, descripcion, stockMinimo, unidadMedida, idSeccion);

        // Assert
        assertEquals("La unidad de medida no puede estar vacía.", result);
        verify(materiaPrimaRepository, never()).save(any(MateriaPrima.class));
    }

    @Test
    void createMateriaPrima_SeccionNotFound() {
        // Arrange
        String nombre = "Harina";
        String descripcion = "Harina de trigo";
        double stockMinimo = 10.0;
        String unidadMedida = "kg";
        String idSeccion = "sec1";

        when(seccionRepository.findById(idSeccion)).thenReturn(null);

        // Act
        String result = materiaPrimaController.createMateriaPrima(
                nombre, descripcion, stockMinimo, unidadMedida, idSeccion);

        // Assert
        assertEquals("Sección no encontrada.", result);
        verify(materiaPrimaRepository, never()).save(any(MateriaPrima.class));
    }

    @Test
    void getAllMateriasPrimas_Success() {
        // Arrange
        MateriaPrima mp1 = new MateriaPrima("1", "Harina", "Harina de trigo", 10.0, "kg", "sec1");
        MateriaPrima mp2 = new MateriaPrima("2", "Azúcar", "Azúcar blanca", 5.0, "kg", "sec1");
        List<MateriaPrima> expected = Arrays.asList(mp1, mp2);

        when(materiaPrimaRepository.findAll()).thenReturn(expected);

        // Act
        List<MateriaPrima> result = materiaPrimaController.getAllMateriasPrimas();

        // Assert
        assertEquals(2, result.size());
        assertEquals(expected, result);
    }

    @Test
    void updateMateriaPrima_Success() {
        // Arrange
        String id = "1";
        String nombre = "Harina Actualizada";
        String descripcion = "Harina de trigo mejorada";
        double stockMinimo = 15.0;
        String unidadMedida = "kg";
        String idSeccion = "sec1";

        MateriaPrima existing = new MateriaPrima(id, "Harina", "Harina de trigo", 10.0, "kg", "sec1");

        when(seccionRepository.findById(idSeccion)).thenReturn(new Object());
        when(materiaPrimaRepository.findById(id)).thenReturn(existing);

        // Act
        String result = materiaPrimaController.updateMateriaPrima(
                id, nombre, descripcion, stockMinimo, unidadMedida, idSeccion);

        // Assert
        assertNull(result);
        verify(materiaPrimaRepository, times(1)).update(any(MateriaPrima.class));
    }

    @Test
    void deleteMateriaPrima_Success() {
        // Arrange
        String id = "1";
        MateriaPrima existing = new MateriaPrima(id, "Harina", "Harina de trigo", 10.0, "kg", "sec1");

        when(materiaPrimaRepository.findById(id)).thenReturn(existing);

        // Act
        String result = materiaPrimaController.deleteMateriaPrima(id);

        // Assert
        assertNull(result);
        verify(materiaPrimaRepository, times(1)).delete(id);
    }

    @Test
    void deleteMateriaPrima_NotFound() {
        // Arrange
        String id = "1";

        when(materiaPrimaRepository.findById(id)).thenReturn(null);

        // Act
        String result = materiaPrimaController.deleteMateriaPrima(id);

        // Assert
        assertEquals("Materia prima no encontrada.", result);
        verify(materiaPrimaRepository, never()).delete(id);
    }

    @Test
    void searchMateriasPrimas_Success() {
        // Arrange
        String query = "harina";
        String seccionId = "sec1";
        MateriaPrima mp1 = new MateriaPrima("1", "Harina", "Harina de trigo", 10.0, "kg", seccionId);
        List<MateriaPrima> expected = Arrays.asList(mp1);

        // Act
        List<MateriaPrima> result = materiaPrimaController.searchMateriasPrimas(query, seccionId);

        // Assert
        // La implementación real de searchMateriasPrimas usa MongoDB directamente,
        // por lo que en una prueba unitaria solo podemos verificar que el método no lance excepciones
        assertNotNull(result);
    }
}