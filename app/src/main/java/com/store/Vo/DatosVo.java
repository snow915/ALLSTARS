package com.store.Vo;

public class DatosVo {
    private String nombre;
    private String precio;
    private int imagen;
    private int stars;

    public DatosVo(){

    }

    public DatosVo(String nombre, String precio, int imagen, int stars) {
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
        this.stars = stars;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = imagen;
    }
}
