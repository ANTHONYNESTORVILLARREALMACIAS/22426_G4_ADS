package com.panaderia.ruminahui.util;

public class Validator {
    public static boolean isValidUsername(String username) {
        return username != null && !username.trim().isEmpty() && username.length() >= 3;
    }

    public static boolean isValidPassword(String password) {
        return password != null && !password.trim().isEmpty() && password.length() >= 6;
    }

    public static boolean isValidNombre(String nombre) {
        return nombre != null && !nombre.trim().isEmpty() && nombre.length() >= 2;
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean isValidSeccionNombre(String nombre) {
        return nombre != null && !nombre.trim().isEmpty() && nombre.length() >= 2;
    }

    public static boolean isValidSeccionDescripcion(String descripcion) {
        return descripcion != null && !descripcion.trim().isEmpty();
    }

    public static boolean isValidMateriaPrimaNombre(String nombre) {
        return nombre != null && !nombre.trim().isEmpty() && nombre.length() >= 2;
    }

    public static boolean isValidMateriaPrimaStockMinimo(double stockMinimo) {
        return stockMinimo >= 0;
    }

    public static boolean isValidMateriaPrimaUnidadMedida(String unidadMedida) {
        return unidadMedida != null && !unidadMedida.trim().isEmpty();
    }

    public static boolean isValidStockCantidad(double cantidad) {
        return cantidad >= 0;
    }

    public static boolean isValidFecha(String fecha) {
        return fecha != null && fecha.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    public static boolean isValidRecetaNombre(String nombre) {
        return nombre != null && !nombre.trim().isEmpty() && nombre.length() >= 2;
    }

    public static boolean isValidRecetaDescripcion(String descripcion) {
        return descripcion != null && !descripcion.trim().isEmpty();
    }

    public static boolean isValidDetalleRecetaCantidad(double cantidad) {
        return cantidad > 0;
    }

    public static boolean isValidDetalleRecetaUnidadMedida(String unidadMedida) {
        return unidadMedida != null && !unidadMedida.trim().isEmpty();
    }

    public static boolean isValidProduccionCantidad(double cantidad) {
        return cantidad > 0;
    }
}