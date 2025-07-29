package com.panaderia.ruminahui.model;

public class Usuario {
    private String id;
    private String username;
    private String password;
    private String nombre;

    public Usuario(String id, String username, String password, String nombre) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}