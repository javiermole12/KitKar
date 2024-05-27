package com.example.sample.Clases;

public class Usuarios {
    private int id;
    private String usuario;
    private String contra;
    private String rol;

    public Usuarios(int id, String usuario, String contra, String rol) {
        this.id = id;
        this.usuario = usuario;
        this.contra = contra;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
