package com.panaderia.ruminahui.model;

public class DetalleReceta {
    private String id;
    private String idReceta;
    private String idMateria;
    private double cantidad;
    private String unidadMedida;

    public DetalleReceta(String id, String idReceta, String idMateria, double cantidad, String unidadMedida) {
        this.id = id;
        this.idReceta = idReceta;
        this.idMateria = idMateria;
        this.cantidad = cantidad;
        this.unidadMedida = unidadMedida;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(String idReceta) {
        this.idReceta = idReceta;
    }

    public String getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(String idMateria) {
        this.idMateria = idMateria;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
}