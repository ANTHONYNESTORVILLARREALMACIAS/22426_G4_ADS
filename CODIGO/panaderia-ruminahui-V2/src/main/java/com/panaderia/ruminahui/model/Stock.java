package com.panaderia.ruminahui.model;

public class Stock {
    private String id;
    private String idMateria;
    private double cantidad;
    private String unidadMedida;
    private String fecha;

    public Stock(String id, String idMateria, double cantidad, String unidadMedida, String fecha) {
        this.id = id;
        this.idMateria = idMateria;
        this.cantidad = cantidad;
        this.unidadMedida = unidadMedida;
        this.fecha = fecha;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setIdMateria(String idMateria) {
        this.idMateria = idMateria;
    }
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public String getId() { return id; }
    public String getIdMateria() { return idMateria; }
    public double getCantidad() { return cantidad; }
    public String getUnidadMedida() { return unidadMedida; }
    public String getFecha() { return fecha; }
}