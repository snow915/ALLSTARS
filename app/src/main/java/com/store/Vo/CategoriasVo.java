package com.store.Vo;

public class CategoriasVo {
    private String nombre;
    private int imagen;

    public CategoriasVo(){

    }

    public CategoriasVo(String nombre, int imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public String getNombreCategorias() {
        return nombre;
    }

    public void setNombreCategorias(String nombre) {
        this.nombre = nombre;
    }

    public int getImagenCategorias() {
        return imagen;
    }

    public void setImagenCategorias(int imagen) {
        this.imagen = imagen;
    }
}
