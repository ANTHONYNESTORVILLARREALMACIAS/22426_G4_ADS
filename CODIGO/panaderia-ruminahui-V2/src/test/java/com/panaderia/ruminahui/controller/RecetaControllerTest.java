package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.Receta;
import com.panaderia.ruminahui.model.RecetaRepository;
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

class RecetaControllerTest {

    @Mock
    private RecetaRepository recetaRepository;

    @InjectMocks
    private RecetaController recetaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReceta_Success() {
        // Arrange
        String nombre = "Pan integral";
        String descripcion = "Pan de harina integral";

        // Act
        String result = recetaController.createReceta(nombre, descripcion);

        // Assert
        assertNull(result);
        verify(recetaRepository, times(1)).save(any(Receta.class));
    }

    @Test
    void createReceta_InvalidNombre() {
        // Arrange
        String nombre = "P"; // Nombre demasiado corto
        String descripcion = "Pan de harina integral";

        // Act
        String result = recetaController.createReceta(nombre, descripcion);

        // Assert
        assertEquals("El nombre de la receta debe tener al menos 2 caracteres.", result);
        verify(recetaRepository, never()).save(any(Receta.class));
    }

    @Test
    void getAllRecetas_Success() {
        // Arrange
        Receta r1 = new Receta("1", "Pan blanco", "Pan básico");
        Receta r2 = new Receta("2", "Pan integral", "Pan saludable");
        List<Receta> expected = Arrays.asList(r1, r2);

        when(recetaRepository.findAll()).thenReturn(expected);

        // Act
        List<Receta> result = recetaController.getAllRecetas();

        // Assert
        assertEquals(2, result.size());
        assertEquals(expected, result);
    }

    @Test
    void updateReceta_Success() {
        // Arrange
        String id = "1";
        String nombre = "Pan integral mejorado";
        String descripcion = "Nueva versión del pan integral";

        Receta existing = new Receta(id, "Pan integral", "Pan saludable");

        when(recetaRepository.findById(id)).thenReturn(existing);

        // Act
        String result = recetaController.updateReceta(id, nombre, descripcion);

        // Assert
        assertNull(result);
        verify(recetaRepository, times(1)).update(any(Receta.class));
    }

    @Test
    void updateReceta_InvalidNombre() {
        // Arrange
        String id = "1";
        String nombre = "P"; // Nombre demasiado corto
        String descripcion = "Nueva versión del pan integral";

        // Act
        String result = recetaController.updateReceta(id, nombre, descripcion);

        // Assert
        assertEquals("El nombre de la receta debe tener al menos 2 caracteres.", result);
        verify(recetaRepository, never()).update(any(Receta.class));
    }

    @Test
    void updateReceta_NotFound() {
        // Arrange
        String id = "1";
        String nombre = "Pan integral mejorado";
        String descripcion = "Nueva versión del pan integral";

        when(recetaRepository.findById(id)).thenReturn(null);

        // Act
        String result = recetaController.updateReceta(id, nombre, descripcion);

        // Assert
        assertEquals("Receta no encontrada.", result);
        verify(recetaRepository, never()).update(any(Receta.class));
    }

    @Test
    void deleteReceta_Success() {
        // Arrange
        String id = "1";
        Receta existing = new Receta(id, "Pan integral", "Pan saludable");

        when(recetaRepository.findById(id)).thenReturn(existing);

        // Act
        String result = recetaController.deleteReceta(id);

        // Assert
        assertNull(result);
        verify(recetaRepository, times(1)).delete(id);
    }

    @Test
    void deleteReceta_NotFound() {
        // Arrange
        String id = "1";

        when(recetaRepository.findById(id)).thenReturn(null);

        // Act
        String result = recetaController.deleteReceta(id);

        // Assert
        assertEquals("Receta no encontrada.", result);
        verify(recetaRepository, never()).delete(id);
    }
}