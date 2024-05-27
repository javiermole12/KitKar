package com.example.sample.Clases;

public class Pedidos {
    private int id;
    private String nomProv;
    private String apeProv;
    private String piezas;
    private double total;
    private int cantidad;

    public Pedidos(int id, String nomProv, String apeProv, String piezas, double total, int cantidad) {
        this.id = id;
        this.nomProv = nomProv;
        this.apeProv = apeProv;
        this.piezas = piezas;
        this.total = total;
        this.cantidad = cantidad;
    }

    public Pedidos(double precio, String nombre, int cantidad) {
        this.piezas = nombre;
        this.total = precio * cantidad;
        this.cantidad = cantidad;
        this.nomProv = "";
        this.apeProv = "";
    }

    // Getters and setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomProv() {
        return nomProv;
    }

    public void setNomProv(String nomProv) {
        this.nomProv = nomProv;
    }

    public String getApeProv() {
        return apeProv;
    }

    public void setApeProv(String apeProv) {
        this.apeProv = apeProv;
    }

    public String getPiezas() {
        return piezas;
    }

    public void setPiezas(String piezas) {
        this.piezas = piezas;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.total = this.total / this.cantidad * cantidad; // actualizar el total
    }
}
