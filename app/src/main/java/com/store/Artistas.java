package com.store;

public class Artistas {
    private String biografia;
    private String categoria;
    private String imagen;
    private String nombre;
    private String pass;
    private int puntaje;
    private String user;

    public void setBiografia(String biografia){
        this.biografia = biografia;
    }
    public String getBiografia(){
        return this.biografia;
    }
    public void setCategoria(String categoria){
        this.categoria = categoria;
    }
    public String getCategoria(){
        return this.categoria;
    }
    public void setImagen(String ruta_imagen){
        this.imagen = ruta_imagen;
    }
    public String getImagen(){
        return this.imagen;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public String getNombre(){
        return this.nombre;
    }
    public void setPass(String pass){
        this.pass = pass;
    }
    public String getPass(){
        return this.pass;
    }
    public void setPuntaje(int stars){
        this.puntaje = stars;
    }
    public int getPuntaje(){ return this.puntaje; }
    public void setUser(String user){
        this.user = user;
    }
    public String getUser(){
        return this.user;
    }
}
