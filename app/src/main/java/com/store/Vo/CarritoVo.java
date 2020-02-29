package com.store.Vo;

public class CarritoVo {
    private String nombre;
    private int imagen;

    public CarritoVo(){

    }

    public CarritoVo(String nombre, int imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public String getNombreCarrito() {
        return nombre;
    }

    public void setNombreCarrito(String nombre) {
        this.nombre = nombre;
    }

    public int getImagenCarrito() {
        return imagen;
    }

    public void setImagenCarrito(int imagen) {
        this.imagen = imagen;
    }
}
