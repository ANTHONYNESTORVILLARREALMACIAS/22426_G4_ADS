package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.util.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    void testIsValidMateriaPrimaNombre() {
        assertTrue(Validator.isValidMateriaPrimaNombre("Harina"));
        assertTrue(Validator.isValidMateriaPrimaNombre("Az"));
        assertFalse(Validator.isValidMateriaPrimaNombre("A"));
        assertFalse(Validator.isValidMateriaPrimaNombre(""));
        assertFalse(Validator.isValidMateriaPrimaNombre(null));
    }

    @Test
    void testIsValidMateriaPrimaStockMinimo() {
        assertTrue(Validator.isValidMateriaPrimaStockMinimo(0));
        assertTrue(Validator.isValidMateriaPrimaStockMinimo(10.5));
        assertFalse(Validator.isValidMateriaPrimaStockMinimo(-1));
        assertFalse(Validator.isValidMateriaPrimaStockMinimo(-0.1));
    }

    @Test
    void testIsValidMateriaPrimaUnidadMedida() {
        assertTrue(Validator.isValidMateriaPrimaUnidadMedida("kg"));
        assertTrue(Validator.isValidMateriaPrimaUnidadMedida(" l "));
        assertFalse(Validator.isValidMateriaPrimaUnidadMedida(""));
        assertFalse(Validator.isValidMateriaPrimaUnidadMedida(null));
    }

    @Test
    void testIsValidUsuarioUsername() {
        assertTrue(Validator.isValidUsuarioUsername("admin"));
        assertTrue(Validator.isValidUsuarioUsername("user1"));
        assertFalse(Validator.isValidUsuarioUsername("usr"));
        assertFalse(Validator.isValidUsuarioUsername(""));
        assertFalse(Validator.isValidUsuarioUsername(null));
    }

    @Test
    void testIsValidUsuarioNombre() {
        assertTrue(Validator.isValidUsuarioNombre("Juan Pérez"));
        assertTrue(Validator.isValidUsuarioNombre("Ana"));
        assertFalse(Validator.isValidUsuarioNombre("A"));
        assertFalse(Validator.isValidUsuarioNombre(""));
        assertFalse(Validator.isValidUsuarioNombre(null));
    }

    @Test
    void testIsValidUsuarioPassword() {
        assertTrue(Validator.isValidUsuarioPassword("password123"));
        assertTrue(Validator.isValidUsuarioPassword("123456"));
        assertFalse(Validator.isValidUsuarioPassword("12345"));
        assertFalse(Validator.isValidUsuarioPassword(""));
        assertFalse(Validator.isValidUsuarioPassword(null));
    }

    @Test
    void testIsValidUsername() {
        assertTrue(Validator.isValidUsername("admin"));
        assertTrue(Validator.isValidUsername("user"));
        assertFalse(Validator.isValidUsername("us"));
        assertFalse(Validator.isValidUsername(""));
        assertFalse(Validator.isValidUsername(null));
    }

    @Test
    void testIsValidPassword() {
        assertTrue(Validator.isValidPassword("secure123"));
        assertTrue(Validator.isValidPassword("123456"));
        assertFalse(Validator.isValidPassword("12345"));
        assertFalse(Validator.isValidPassword(""));
        assertFalse(Validator.isValidPassword(null));
    }

    @Test
    void testIsValidNombre() {
        assertTrue(Validator.isValidNombre("Producto"));
        assertTrue(Validator.isValidNombre("AB"));
        assertFalse(Validator.isValidNombre("A"));
        assertFalse(Validator.isValidNombre(""));
        assertFalse(Validator.isValidNombre(null));
    }

    @Test
    void testIsValidEmail() {
        assertTrue(Validator.isValidEmail("test@example.com"));
        assertTrue(Validator.isValidEmail("user.name+tag@domain.co"));
        assertFalse(Validator.isValidEmail("invalid.email"));
        assertFalse(Validator.isValidEmail("user@"));
        assertFalse(Validator.isValidEmail(""));
        assertFalse(Validator.isValidEmail(null));
    }

    @Test
    void testIsValidSeccionNombre() {
        assertTrue(Validator.isValidSeccionNombre("Panadería"));
        assertTrue(Validator.isValidSeccionNombre("AB"));
        assertFalse(Validator.isValidSeccionNombre("A"));
        assertFalse(Validator.isValidSeccionNombre(""));
        assertFalse(Validator.isValidSeccionNombre(null));
    }

    @Test
    void testIsValidSeccionDescripcion() {
        assertTrue(Validator.isValidSeccionDescripcion("Descripción válida"));
        assertTrue(Validator.isValidSeccionDescripcion(" a "));
        assertFalse(Validator.isValidSeccionDescripcion(""));
        assertFalse(Validator.isValidSeccionDescripcion(null));
    }

    @Test
    void testIsValidStockCantidad() {
        assertTrue(Validator.isValidStockCantidad(0));
        assertTrue(Validator.isValidStockCantidad(100.5));
        assertFalse(Validator.isValidStockCantidad(-1));
        assertFalse(Validator.isValidStockCantidad(-0.1));
    }

    @Test
    void testIsValidFecha() {
        assertTrue(Validator.isValidFecha("2023-05-20"));
        assertTrue(Validator.isValidFecha("1999-12-31"));
        assertFalse(Validator.isValidFecha("20-05-2023"));
        assertFalse(Validator.isValidFecha("2023/05/20"));
        assertFalse(Validator.isValidFecha(""));
        assertFalse(Validator.isValidFecha(null));
    }

    @Test
    void testIsValidRecetaNombre() {
        assertTrue(Validator.isValidRecetaNombre("Pan integral"));
        assertTrue(Validator.isValidRecetaNombre("AB"));
        assertFalse(Validator.isValidRecetaNombre("A"));
        assertFalse(Validator.isValidRecetaNombre(""));
        assertFalse(Validator.isValidRecetaNombre(null));
    }

    @Test
    void testIsValidRecetaDescripcion() {
        assertTrue(Validator.isValidRecetaDescripcion("Descripción válida"));
        assertTrue(Validator.isValidRecetaDescripcion(" a "));
        assertFalse(Validator.isValidRecetaDescripcion(""));
        assertFalse(Validator.isValidRecetaDescripcion(null));
    }

    @Test
    void testIsValidDetalleRecetaCantidad() {
        assertTrue(Validator.isValidDetalleRecetaCantidad(0.1));
        assertTrue(Validator.isValidDetalleRecetaCantidad(100));
        assertFalse(Validator.isValidDetalleRecetaCantidad(0));
        assertFalse(Validator.isValidDetalleRecetaCantidad(-1));
    }

    @Test
    void testIsValidDetalleRecetaUnidadMedida() {
        assertTrue(Validator.isValidDetalleRecetaUnidadMedida("kg"));
        assertTrue(Validator.isValidDetalleRecetaUnidadMedida(" l "));
        assertFalse(Validator.isValidDetalleRecetaUnidadMedida(""));
        assertFalse(Validator.isValidDetalleRecetaUnidadMedida(null));
    }

    @Test
    void testIsValidProduccionCantidad() {
        assertTrue(Validator.isValidProduccionCantidad(0.1));
        assertTrue(Validator.isValidProduccionCantidad(50));
        assertFalse(Validator.isValidProduccionCantidad(0));
        assertFalse(Validator.isValidProduccionCantidad(-5));
    }
}