package com.panaderia.ruminahui.model;

public class Stock {
    private String id;
    private String idMateria;
    private double cantidad;
    private String fecha;

    public Stock(String id, String idMateria, double cantidad, String fecha) {
        this.id = id;
        this.idMateria = idMateria;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}