package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.Seccion;
import com.panaderia.ruminahui.model.SeccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SeccionControllerTest {

    @Mock
    private SeccionRepository seccionRepository;

    @InjectMocks
    private SeccionController seccionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSeccion_Success() {
        // Arrange
        Seccion seccion = new Seccion(null, "Panadería", "Sección de panes");
        when(seccionRepository.create(any(Seccion.class))).thenReturn(null);

        // Act
        String result = seccionController.createSeccion(seccion);

        // Assert
        assertNull(result);
        verify(seccionRepository, times(1)).create(any(Seccion.class));
    }

    @Test
    void createSeccion_EmptyNombre() {
        // Arrange
        Seccion seccion = new Seccion(null, "", "Sección de panes");

        // Act
        String result = seccionController.createSeccion(seccion);

        // Assert
        assertEquals("El nombre de la sección es requerido.", result);
        verify(seccionRepository, never()).create(any(Seccion.class));
    }

    @Test
    void createSeccion_RepositoryError() {
        // Arrange
        Seccion seccion = new Seccion(null, "Panadería", "Sección de panes");
        when(seccionRepository.create(any(Seccion.class))).thenReturn("Error de base de datos");

        // Act
        String result = seccionController.createSeccion(seccion);

        // Assert
        assertEquals("Error de base de datos", result);
    }

    @Test
    void updateSeccion_Success() {
        // Arrange
        Seccion seccion = new Seccion("1", "Panadería Actualizada", "Nueva descripción");
        when(seccionRepository.update(any(Seccion.class))).thenReturn(null);

        // Act
        String result = seccionController.updateSeccion(seccion);

        // Assert
        assertNull(result);
        verify(seccionRepository, times(1)).update(any(Seccion.class));
    }

    @Test
    void updateSeccion_EmptyNombre() {
        // Arrange
        Seccion seccion = new Seccion("1", "", "Nueva descripción");

        // Act
        String result = seccionController.updateSeccion(seccion);

        // Assert
        assertEquals("El nombre de la sección es requerido.", result);
        verify(seccionRepository, never()).update(any(Seccion.class));
    }

    @Test
    void deleteSeccion_Success() {
        // Arrange
        String id = "1";
        when(seccionRepository.delete(id)).thenReturn(null);

        // Act
        String result = seccionController.deleteSeccion(id);

        // Assert
        assertNull(result);
        verify(seccionRepository, times(1)).delete(id);
    }

    @Test
    void deleteSeccion_EmptyId() {
        // Act
        String result = seccionController.deleteSeccion("");

        // Assert
        assertEquals("El ID de la sección es requerido.", result);
        verify(seccionRepository, never()).delete(anyString());
    }

    @Test
    void getSeccionById_Success() {
        // Arrange
        String id = "1";
        Seccion expected = new Seccion(id, "Panadería", "Sección de panes");
        when(seccionRepository.findById(id)).thenReturn(expected);

        // Act
        Seccion result = seccionController.getSeccionById(id);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void getAllSecciones_Success() {
        // Arrange
        Seccion s1 = new Seccion("1", "Panadería", "Panes y bollos");
        Seccion s2 = new Seccion("2", "Pastelería", "Tortas y pasteles");
        List<Seccion> expected = Arrays.asList(s1, s2);
        when(seccionRepository.findAll()).thenReturn(expected);

        // Act
        List<Seccion> result = seccionController.getAllSecciones();

        // Assert
        assertEquals(2, result.size());
        assertEquals(expected, result);
    }

    @Test
    void searchSecciones_Success() {
        // Arrange
        String searchText = "pan";
        Seccion s1 = new Seccion("1", "Panadería", "Panes y bollos");
        List<Seccion> expected = Arrays.asList(s1);
        when(seccionRepository.findByName(searchText)).thenReturn(expected);

        // Act
        List<Seccion> result = seccionController.searchSecciones(searchText);

        // Assert
        assertEquals(1, result.size());
        assertEquals(expected, result);
    }
}