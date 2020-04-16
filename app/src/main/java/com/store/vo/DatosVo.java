package com.store.vo;

public class DatosVo {
    private String nombre;
    private String precio;
    private String biografia;
    private String ruta_imagen;
    private int stars;
    private String username;

    public DatosVo(){

    }

    public DatosVo(String nombre, String precio, String biografia, String ruta_imagen, int stars, String username) {
        this.nombre = nombre;
        this.precio = precio;
        this.biografia = biografia;
        this.ruta_imagen = ruta_imagen;
        this.stars = stars;
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.nombre = username;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return this.precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getBiografia() {
        return this.biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getRutaImagen() {
        return this.ruta_imagen;
    }

    public void setRutaImagen(String ruta_imagen) {
        this.ruta_imagen = ruta_imagen;
    }

    public int getStars() {
        return this.stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
