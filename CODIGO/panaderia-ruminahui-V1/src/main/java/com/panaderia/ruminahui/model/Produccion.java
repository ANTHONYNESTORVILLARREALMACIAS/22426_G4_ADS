package com.panaderia.ruminahui.model;

public class Produccion {
    private String id;
    private String idReceta;
    private double cantidad;
    private String fecha;

    public Produccion(String id, String idReceta, double cantidad, String fecha) {
        this.id = id;
        this.idReceta = idReceta;
        this.cantidad = cantidad;
        this.fecha = fecha;
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

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}